package bgu.spl.mics.application.services;

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
                List<DetectedObject> detectedObjects = camera.handle_tick(tick.getCurrentTick());
                if (detectedObjects != null && !detectedObjects.isEmpty()) {
                    //update the statisticalFolder
                    int NumDetectedObjects = StatisticalFolder.getInstance().getNumDetectedObjects();
                    StatisticalFolder.getInstance().setNumDetectedObjects(NumDetectedObjects + detectedObjects.size());
                    //send detected objects event
                    DetectObjectsEvent e = new DetectObjectsEvent(detectedObjects, this.getName(), tick.getCurrentTick());
                    sendEvent(e);
                }
            }
    });

        //subscribe to TerminatedBroadcast
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            terminate();
        });

    }
}
