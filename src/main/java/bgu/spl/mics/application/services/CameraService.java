package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private final Camera camera;
    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("Cam" + camera.getId());
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        //Subscribe to TickBroadcast to process ticks
        subscribeBroadcast(TickBroadcast.class, tick -> {
            //if camera is operational, retrieve list of stamped objects
            if (camera.getStatus() == STATUS.UP) {
                StampedDetectedObjects s = camera.handle_tick(tick.getCurrentTick());

                if (s != null && !s.getDetectedObjects().isEmpty()) {
                    //handle errors
                    int counter = 0; //count the objects until the error
                    for (DetectedObject o : s.getDetectedObjects()) {
                        if (o.getId().equals("ERROR")){
                            camera.setStatus(STATUS.ERROR);
                            CrashedBroadcast b = new CrashedBroadcast(camera.getId(), o.getDescription(), "Camera" + camera.getId());
                            sendBroadcast(b);
                            break;
                        }
                        else{ counter++; }
                    }
                    //update the statisticalFolder
                    StatisticalFolder folder = StatisticalFolder.getInstance();
                    int NumDetectedObjects = folder.getNumDetectedObjects();
                    folder.setNumDetectedObjects(NumDetectedObjects + counter);
                    //send detected objects event
                    if (camera.getStatus() == STATUS.UP) {
                        camera.setLastFrame(s);
                        DetectObjectsEvent e = new DetectObjectsEvent(s.getDetectedObjects(), this.getName(), tick.getCurrentTick());
                        sendEvent(e);
                        camera.updateStatus();
                    }
                    if (camera.getStatus() == STATUS.DOWN){
                        FusionSlam.getInstance().reduceNumOfUpThreads();
                        if (FusionSlam.getInstance().getNumOfUpThreads() == 0) {
                            Broadcast b = new TerminatedBroadcast("Cam");
                            sendBroadcast(b);
                        }
                        terminate();
                    }
                }
            }
    });

        //subscribe to TerminatedBroadcast
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            StatisticalFolder.getInstance().updateCamerasLastFrame(camera.getLastFrame() , camera.getCamera_key());
            terminate();
        });

    }
}
