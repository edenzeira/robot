package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.List;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 * <p>
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */
public class FusionSlamService extends MicroService {
    private FusionSlam fusionSlam;
    int currentTime = 0;
    int tickDuration = 0;;

    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("FusionSlam");
        this.fusionSlam = fusionSlam;
    }

    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        //Subscribe to TickBroadcast to process ticks
        subscribeBroadcast(TickBroadcast.class, tick -> {
            currentTime = tick.getCurrentTick();
            tickDuration = tick.getTickDuration();
        });

        //Subscribe to TrackedObjectsEvent
        subscribeEvent(TrackedObjectsEvent.class, event -> {
            List<TrackedObject> list = event.getTrackedObjects();
            if (list != null && list.size() > 0) {
                Pose robotP = fusionSlam.isPoseExist(list.get(0));
                if (robotP.equals(new Pose(-1,-1,-1,-1))) {  //check if the pose already updated
                    sendEvent(event);
                }
                else {
                    for (TrackedObject o : list)
                        fusionSlam.handle_trackedObjectEvent(robotP, o);
                }
            }
        });

        //Subscribe to PoseEvent
        subscribeEvent(PoseEvent.class, event -> {
            fusionSlam.getPoses().add(event.getPose());
        });



        //Subscribe to TerminatedBroadcast to process ticks
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
                StatisticalFolder.getInstance().setPosesLastFrame();
                StatisticalFolder.getInstance().setCamerasLastFrame();
                StatisticalFolder.getInstance().setLidarsLastFrame();
                StatisticalFolder.getInstance().setFaultySensor(null);
                StatisticalFolder.getInstance().setErrorDescription(null);
                StatisticalFolder.getInstance().setSystemRuntime(currentTime);
                fusionSlam.outputFile();
                terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            StatisticalFolder.getInstance().setErrorDescription(crashed.getErrorDescription());
            StatisticalFolder.getInstance().setFaultySensor(crashed.getFaultySensor());
            fusionSlam.outputFile();
            terminate();
        });
    }
}
