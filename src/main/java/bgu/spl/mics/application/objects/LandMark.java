package bgu.spl.mics.application.objects;

/**
 * Represents a landmark in the environment map.
 * Landmarks are identified and updated by the FusionSlam service.
 */
public class LandMark {
    private String id;
    private String description;
    private CloudPoint location;

    public LandMark(String id, String description, CloudPoint location) {
        this.id = id;
        this.description = description;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public CloudPoint getLocation() {
        return location;
    }
}
