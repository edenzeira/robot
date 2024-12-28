package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {
    private List<StampedCloudPoints> cloudPoints;

    public List<TrackedObject> processDetectedObjects(int eventTime, List<DetectedObject> detectedObjects) {
        List<TrackedObject> trackedObjects = new ArrayList<TrackedObject>();
        for(DetectedObject detectedObject : detectedObjects) {
            for(StampedCloudPoints cloudPoint : cloudPoints) {
                if(detectedObject.getId().equals(cloudPoint.getId()) && eventTime == cloudPoint.getTime() ) {
                    trackedObjects.add(new TrackedObject(detectedObject.getId(), eventTime, detectedObject.getDescription(), cloudPoint.getPoints()));
                }
            }
        }
        return trackedObjects;
    }

    private static class LiDarDataBaseHolder {
        private static LiDarDataBase instance = null;
    }

    private LiDarDataBase(List<StampedCloudPoints> cloudPoints) {
        this.cloudPoints = cloudPoints;
    }
    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public synchronized static LiDarDataBase getInstance(String filePath) {
        if(LiDarDataBaseHolder.instance == null){
            //GSON
            List<StampedCloudPoints> cloudPoints = null;


            LiDarDataBaseHolder.instance = new LiDarDataBase(cloudPoints);
        }
        return LiDarDataBaseHolder.instance;
    }

    public List<StampedCloudPoints> getCloudPoints() {
        return cloudPoints;
    }
}


