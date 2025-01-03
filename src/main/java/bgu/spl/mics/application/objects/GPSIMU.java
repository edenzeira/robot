package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.PoseEvent;

import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    private STATUS status;
    private List<Pose> stumpedPoses;

    public GPSIMU(int currentTick, STATUS status, List<Pose> stumpedPoses) {
        this.currentTick = currentTick;
        this.status = status;
        this.stumpedPoses = stumpedPoses;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public STATUS getStatus() {
        return status;
    }

    public void updateStatus(int currentTick) {
        if (stumpedPoses.size() == currentTick)
            this.status = STATUS.DOWN;
    }

    public List<Pose> getStumpedPoses() {
        return stumpedPoses;
    }

    public void setStumpedPoses(List<Pose> stumpedPoses) {
        this.stumpedPoses = stumpedPoses;
    }

    public Pose getCurrentPose(int currentTick){
        if (getStatus() == STATUS.UP) {
            List<Pose> PoseList = getStumpedPoses();
            if (PoseList != null) {
                //finds the correct pose according to the time
                for (Pose object : PoseList) {
                    if (object.getTime() == currentTick) {
                        //send pose event
                        return object;
                    }
                }
            }
        }
        return null;
    }
    public String toString() {
        return "curruntTick " + currentTick +" " +  "stumpedPoses " + stumpedPoses;
    }
}
