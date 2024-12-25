package bgu.spl.mics.application.objects;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private double latitude;
    private double longitude;
    private double altitude;
    private double orientation;

    public GPSIMU(double latitude, double longitude, double altitude, double orientation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.orientation = orientation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getOrientation() {
        return orientation;
    }

    public void updatePosition(double newLatitude, double newLongitude, double newAltitude) {
        this.latitude = newLatitude;
        this.longitude = newLongitude;
        this.altitude = newAltitude;
    }
}
