package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.application.objects.STATUS;

import java.util.List;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {
    private GPSIMU gpsimu;

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
            //if gpsimu is operational, retrieve list of stamped objects
            if (gpsimu.getStatus() == STATUS.UP) {
                List<Pose> PoseList = gpsimu.getStumpedPoses();
                if (PoseList != null) {
                    //finds the correct pose according to the time
                    for (Pose object : PoseList) {
                        if (object.getTime() == TickBroadcast.getCurrentTick()) {
                            //send pose event
                            PoseEvent poseEvent = new PoseEvent(this.getName(), object);
                            sendEvent(poseEvent);
                            break;
                        }
                    }
                }
            }

        });
    }
}
