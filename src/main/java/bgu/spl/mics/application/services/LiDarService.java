package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;

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
            StatisticalFolder folder = StatisticalFolder.getInstance();
            if(!trackedObjects.isEmpty()){
                //handle errors
                int counter = 0;
                for (TrackedObject o : trackedObjects) {
                    if (o.getId().equals("ERROR")){
                        LiDarWorkerTracker.setStatus(STATUS.ERROR);
                        CrashedBroadcast b = new CrashedBroadcast(LiDarWorkerTracker.getId(), "LiDar disconnection", "LiDar" + LiDarWorkerTracker.getId());
                        sendBroadcast(b);
                        break;
                    }
                    else{ counter++; }
                }
                //update statistical folder
                int NumTrackedObjects = folder.getNumTrackedObjects();
                folder.setNumTrackedObjects(NumTrackedObjects + counter);
            }
                //send event
                if(LiDarWorkerTracker.getStatus() == STATUS.UP) {
                    TrackedObjectsEvent e = new TrackedObjectsEvent(getName(), trackedObjects);
                    sendEvent(e);
                    LiDarWorkerTracker.setLidarsLastFrame(trackedObjects);
                    LiDarWorkerTracker.updateStatus();
                }
                if(LiDarWorkerTracker.getStatus() == STATUS.DOWN) {
                    FusionSlam.getInstance().reduceNumOfUpThreads();
                    if (FusionSlam.getInstance().getNumOfUpThreads() == 0) {
                        Broadcast b = new TerminatedBroadcast("Lidar");
                        sendBroadcast(b);
                    }
                    terminate();
                }
        });

        //subscribe to TerminatedBroadcast
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            StatisticalFolder.getInstance().updateLidarsLastFrame(LiDarWorkerTracker.getLastTrackedObjects() , LiDarWorkerTracker.getId());
            terminate();
        });
    }
}
