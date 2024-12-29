package bgu.spl.mics.application.objects;

import java.util.*;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    private List<LandMark> landMarks;
    private List<Pose> Poses;
    // Singleton instance holder
    private static class FusionSlamHolder {
        private static FusionSlam instance = new FusionSlam();

    }

        private FusionSlam() {
            this.landMarks = new ArrayList<LandMark>() ;
            this.Poses = new ArrayList<Pose>() ;
        }

    public static FusionSlam getInstance() {
        return FusionSlamHolder.instance;
    }

    public String toString() {
        return "FusionSlam{" +
                "landMarks=" + landMarks +
                ", Poses=" + poses +
                '}';
    }

    public List<LandMark> getLandMarks() {
        return landMarks;
    }

    public List<Pose> getPoses() {
        return poses;
    }

    public void handle_trackedObjectEvent(TrackedObject o) {
        //finds the relevant pose of the robot according to the time
        double xRobot = 0;
        double yRobot = 0;
        double yawDeg = 0;
        for (Pose p : poses){
            if (o.getTime() == p.getTime()){
                xRobot = p.getX();
                yRobot = p.getY();
                yawDeg = p.getYaw();
                break;
            }
        }

        //calculates for each cloud point of the object the global point
        ArrayList<CloudPoint> globalCoordinates = new ArrayList<CloudPoint>();
        for (CloudPoint cp : o.getCoordinates()){
            double xLocal = cp.getX();
            double yLocal = cp.getY();
            double yawRad = yawDeg*(Math.PI/180);
            double cos = Math.cos(yawRad);
            double sin = Math.sin(yawRad);
            double xGlobal = cos * xLocal - sin * yLocal + xRobot;
            double yGlobal = sin * xLocal + cos * yLocal + yRobot;
            globalCoordinates.add(new CloudPoint(xGlobal, yGlobal));
        }

        //checks if object already mapped before
        boolean objectExists = false;
        for (LandMark landMark : landMarks) {
            if (landMark.getId().equals(o.getId())) {
                objectExists = true;
                //calculate and update the relevant landmark
                ArrayList<CloudPoint> oldCoordinates = landMark.getCoordinates();
                landMark.setAvarageCounter(landMark.getAvarageCounter() + 1);
                //calculate the avarage for x and y in each cloudPoint of the existing object with the new data
                for (int i = 0; i < globalCoordinates.size(); i++) {
                    oldCoordinates.get(i).setX((oldCoordinates.get(i).getX() * landMark.getAvarageCounter() + globalCoordinates.get(i).getX()) / (landMark.getAvarageCounter() +1));
                    oldCoordinates.get(i).setY((oldCoordinates.get(i).getY() * landMark.getAvarageCounter() + globalCoordinates.get(i).getY()) / (landMark.getAvarageCounter() +1));
                }
                break;
            }
        }
        //if it is a new object, adds new landmark and update statisticalFolder
        if (!objectExists) {
            landMarks.add(new LandMark(o.getId(), o.getDescription(), globalCoordinates));
            int NumLandmarks = StatisticalFolder.getInstance().getNumLandmarks();
            StatisticalFolder.getInstance().setNumLandmarks(NumLandmarks + 1);
        }
    }
}
