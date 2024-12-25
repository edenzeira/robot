package bgu.spl.mics.application.objects;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {
    private int time;
    private List<CloudPoint> cloudPoints;

    public StampedCloudPoints(int time, List<CloudPoint> cloudPoints) {
        this.time = time;
        this.cloudPoints = cloudPoints;
    }

    public int getTime() {
        return time;
    }

    public List<CloudPoint> getCloudPoints() {
        return cloudPoints;
    }
}
