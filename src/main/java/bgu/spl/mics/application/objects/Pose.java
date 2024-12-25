package bgu.spl.mics.application.objects;

/**
 * Represents the robot's pose (position and orientation) in the environment.
 * Includes x, y coordinates and the yaw angle relative to a global coordinate system.
 */
public class Pose {
    private double x;
    private double y;
    private double orientation;

    public Pose(double x, double y, double orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getOrientation() {
        return orientation;
    }

    public void updatePose(double newX, double newY, double newOrientation) {
        this.x = newX;
        this.y = newY;
        this.orientation = newOrientation;
    }
}
