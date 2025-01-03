package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.DetectObjectsEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private int systemRuntime;
    private int numDetectedObjects;
    private int numTrackedObjects;
    private int numLandmarks;
    private ConcurrentHashMap<String, LandMark> landMarks;
    private ConcurrentHashMap<String, StampedDetectedObjects> camerasLastFrame;
    private ConcurrentHashMap<Integer, List<TrackedObject>> lidarsLastFrame;
    private CopyOnWriteArrayList<Pose> posesLastFrame;
    private String errorDescription;
    private String faultySensor;

    private static class StatisticalFolderHolder {
        private static StatisticalFolder instance = new StatisticalFolder(0, 0, 0, 0);
    }

    public StatisticalFolder(int systemRuntime, int numDetectedObjects, int numTrackedObjects, int numLandmarks) {
        this.systemRuntime = systemRuntime;
        this.numDetectedObjects = numDetectedObjects;
        this.numTrackedObjects = numTrackedObjects;
        this.numLandmarks = numLandmarks;
        this.camerasLastFrame = new ConcurrentHashMap<>();
        this.lidarsLastFrame = new ConcurrentHashMap<>();
        this.posesLastFrame = new CopyOnWriteArrayList<>();
        this.landMarks = new ConcurrentHashMap<>();
    }

    public static StatisticalFolder getInstance() {
        return StatisticalFolderHolder.instance;
    }

    public void setFaultySensor(String faultySensor) {
        this.faultySensor = faultySensor;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public void setCamerasLastFrame(){
        camerasLastFrame=null;
    }

    public void setLidarsLastFrame(){
        lidarsLastFrame=null;
    }

    public void setPosesLastFrame() {
        posesLastFrame=null;
    }


    public void updateCamerasLastFrame(StampedDetectedObjects e , String camera_id) {
        if(e == null) return;
        if (camerasLastFrame.containsKey(camera_id))
            camerasLastFrame.put(camera_id, e);
        else {
            camerasLastFrame.putIfAbsent(camera_id, e);
        }
    }

    public void updateLidarsLastFrame(List<TrackedObject> trackedObjects , int lidar_id) {
        if (lidarsLastFrame.containsKey(lidar_id))
            lidarsLastFrame.put(lidar_id, trackedObjects);
        else {
            lidarsLastFrame.putIfAbsent(lidar_id, trackedObjects);
        }
    }

    public void updatePosesLastFrame(Pose pose) {
        posesLastFrame.add(pose);
    }

    public void updateLandMarks(List<LandMark> lst) {
        for(LandMark land:lst){
            landMarks.put(land.getId(), land);
        }
    }

    public ConcurrentHashMap<String, LandMark> landMarks() {
        return landMarks;
    }

    public ConcurrentHashMap<String, StampedDetectedObjects> getCamerasLastFrame() {
        return camerasLastFrame;
    }

    public ConcurrentHashMap<Integer, List<TrackedObject>> getLidarsLastFrame() {
        return lidarsLastFrame;
    }

    public CopyOnWriteArrayList<Pose> getPosesLastFrame() {
        return posesLastFrame;
    }


    public int getSystemRuntime() {
        return systemRuntime;
    }

    public int getNumDetectedObjects() {
        return numDetectedObjects;
    }

    public int getNumTrackedObjects() {
        return numTrackedObjects;
    }

    public int getNumLandmarks() {
        return numLandmarks;
    }

    public void setSystemRuntime(int systemRuntime) {
        this.systemRuntime = systemRuntime;
    }

    public void setNumDetectedObjects(int numDetectedObjects) {
        this.numDetectedObjects = numDetectedObjects;
    }

    public void setNumTrackedObjects(int numTrackedObjects) {
        this.numTrackedObjects = numTrackedObjects;
    }

    public void setNumLandmarks(int numLandmarks) {
        this.numLandmarks = numLandmarks;
    }
}
