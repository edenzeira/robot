package bgu.spl.mics.application.objects;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a landmark in the environment map.
 * Landmarks are identified and updated by the FusionSlam service.
 */
public class LandMark {
    private String id;
    private String description;
    private List<CloudPoint> Coordinates;

    public LandMark(String id, String description, ArrayList<CloudPoint> coordinates) {
        this.id = id;
        this.description = description;
        this.Coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<CloudPoint> getCoordinates() {
        return Coordinates;
  }
}
