package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Camera;

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
        super("CameraService" + camera.getId());
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        // Subscribe to TickBroadcast to process ticks
        subscribeBroadcast(TickBroadcast.class, tick -> {
            if (camera.getStatus() == Camera.CameraStatus.UP) {
                // Simulate object detection
                StampedDetectedObjects detectedObjects = camera.detectObjects(tick.getCurrentTick());
                if (detectedObjects != null) {
                    // Create and send a DetectObjectsEvent
                    DetectObjectsEvent event = new DetectObjectsEvent(detectedObjects);
                    sendEvent(event);
                }
            }
        });

        // Register the microservice in the message bus
        System.out.println(getName() + " initialized and ready.");
    }
}
