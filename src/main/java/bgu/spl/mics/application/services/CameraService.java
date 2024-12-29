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
    private Camera camera;
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
                List<StampedDetectedObjects> detectedObjects = camera.getDetectedObjectsList();
                if (detectedObjects != null) {
                    //finds the correct object according to the time
                    for (StampedDetectedObjects object : detectedObjects) {
                        if (object.getTime() == tick.getCurrentTick() + camera.getFrequency()) {
                            //send detected objects event
                            DetectObjectsEvent event = new DetectObjectsEvent(object.getDetectedObjects(), this.getName(), tick.getCurrentTick());
                            sendEvent(event);
                            break;
                        }
                    }
                }
            }
    });
        //subscribe to TerminatedBroadcast //do we need to classify for each service ?? how to do it?!?!?!?!
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            terminate();
        });

    }
}
