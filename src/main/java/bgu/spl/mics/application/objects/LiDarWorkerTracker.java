package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.TrackedObjectsEvent;

import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
    private int id;
    private int frequency;
    private STATUS status;
    private List<TrackedObject> lastTrackedObjects;

    public LiDarWorkerTracker(int id, int frequency, STATUS status, List<TrackedObject> lastTrackedObjects) {
        this.id = id;
        this.frequency = frequency;
        this.status = status;
        this.lastTrackedObjects = lastTrackedObjects;
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public List<TrackedObject> getLastTrackedObjects() {
        return lastTrackedObjects;
    }

    public List<TrackedObject> handle_detect(int currentTime, int eventTime, int tickDuration, List<DetectedObject> detectedObjects) {
        // Check if the tick matches the LiDAR frequency
        if (currentTime < eventTime + frequency) {
            try {
                Thread.sleep(((long) (eventTime + frequency - currentTime) )* tickDuration);
            } catch (InterruptedException e) {
            }
        }
        // Retrieve cloud points from the database
        List<TrackedObject> trackedObjects = LiDarDataBase.getInstance("").processDetectedObjects(eventTime, detectedObjects);

        return trackedObjects;
    }

    @Override
    public String toString() {
        return "LiDarWorkerTracker{" +
                "id=" + id +
                ", frequency=" + frequency +
                ", status=" + status +
                ", lastTrackedObjects=" + lastTrackedObjects +
                '}';
    }
}
