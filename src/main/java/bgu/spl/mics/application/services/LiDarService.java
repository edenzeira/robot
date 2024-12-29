package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.CloudPoint;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.StatisticalFolder;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 * 
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {
    private final LiDarWorkerTracker LiDarWorkerTracker;
    int currentTime = 0;
    int tickDuration = 0;
    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker LiDarWorkerTracker) {
        super("Lidar" + LiDarWorkerTracker.getId());
        this.LiDarWorkerTracker = LiDarWorkerTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        // Subscribe to TickBroadcast
        subscribeBroadcast(TickBroadcast.class, tick -> {
            currentTime = tick.getCurrentTick();
            tickDuration = tick.getTickDuration();
        });

        // Subscribe to DetectObjectsEvent
        subscribeEvent(DetectObjectsEvent.class, event -> {
            List<TrackedObject> trackedObjects =  LiDarWorkerTracker.handle_detect(currentTime, event.getTime(), tickDuration, event.getDetectedObjects());
            if(!trackedObjects.isEmpty()){
                //update statistical folder
                int NumTrackedObjects = StatisticalFolder.getInstance().getNumTrackedObjects();
                StatisticalFolder.getInstance().setNumTrackedObjects(NumTrackedObjects + trackedObjects.size());
                //send event
                TrackedObjectsEvent e = new TrackedObjectsEvent(getName(), trackedObjects);
                sendEvent(e);
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
