package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;

import java.util.List;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {
    private final GPSIMU gpsimu;

    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("Pose");
        this.gpsimu = gpsimu;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {
        //Subscribe to TickBroadcast to process ticks
        subscribeBroadcast(TickBroadcast.class, tick -> {
            //send pose event
            if (gpsimu.getStatus() == STATUS.UP){
                Pose p = gpsimu.getCurrentPose(tick.getCurrentTick());
                StatisticalFolder.getInstance().updatePosesLastFrame(p);
                if(p != null) {
                    PoseEvent poseEvent = new PoseEvent(this.getName(), p);
                    sendEvent(poseEvent);
                    gpsimu.updateStatus(tick.getCurrentTick());
              }
            }
            if (gpsimu.getStatus() == STATUS.DOWN) {
                FusionSlam.getInstance().reduceNumOfUpThreads();
                if (FusionSlam.getInstance().getNumOfUpThreads() == 0) {
                    Broadcast b = new TerminatedBroadcast("Pose");
                    sendBroadcast(b);
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
