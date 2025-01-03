package bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {
    private final List<StampedCloudPoints> cloudPoints;

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
    public static LiDarDataBase getInstance(String filePath) {
        try {
            if (LiDarDataBaseHolder.instance == null) {
                Gson gson = new Gson();
                // Parse LiDAR data from lidar_data.json
                Path lidarFilePath = Paths.get(filePath);
                Reader lidarReader = Files.newBufferedReader(lidarFilePath);
                Type lidarMapType = new TypeToken<StampedCloudPoints[]>() {
                }.getType();
                StampedCloudPoints[] lidarData = gson.fromJson(lidarReader, lidarMapType);
                lidarReader.close();
                //GSON
                LiDarDataBaseHolder.instance = new LiDarDataBase(Arrays.asList(lidarData));
            }
        } catch (IOException e){}
        return LiDarDataBaseHolder.instance;
    }

    public List<StampedCloudPoints> getCloudPoints() {
        return cloudPoints;
    }

    public List<TrackedObject> processDetectedObjects(int eventTime, List<DetectedObject> detectedObjects) {
        List<TrackedObject> trackedObjects = new ArrayList<TrackedObject>();
        for(DetectedObject detectedObject : detectedObjects) {
            for(StampedCloudPoints points : cloudPoints) {
                if(detectedObject.getId().equals(points.getId()) && eventTime == points.getTime() ) {
                    trackedObjects.add(new TrackedObject(detectedObject.getId(), eventTime, detectedObject.getDescription(), points.getPoints()));
                }
            }
        }
        return trackedObjects;
    }
}


