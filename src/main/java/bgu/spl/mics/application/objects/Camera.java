package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.DetectObjectsEvent;

import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private int id;
    private int frequency;
    private STATUS status;
    private List<StampedDetectedObjects> detectedObjectsList;

    public Camera(int id, int frequency, STATUS status, List<StampedDetectedObjects> detectedObjectsList) {
        this.id = id;
        this.frequency = frequency;
        this.status = status;
        this.detectedObjectsList = detectedObjectsList;
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


    public List<StampedDetectedObjects> getDetectedObjectsList() {
        return detectedObjectsList;
    }
}
    public List<DetectedObject> handle_tick(int currentTime, int tickDuration) {
        if (currentTime < frequency) {
            try {
                Thread.sleep(eventTime * tickDuration + frequency - currentTime * tickDuration);
            } catch (InterruptedException e) {
            }
        }
        // Retrieve cloud points from the database
        List<TrackedObject> trackedObjects = LiDarDataBase.getInstance("").processDetectedObjects(eventTime, detectedObjects);

        return trackedObjects;




        if (camera.getStatus() == STATUS.UP) {
            List<StampedDetectedObjects> detectedObjects = camera.getDetectedObjectsList();
            if (detectedObjects != null) {
                //finds the correct object according to the time
                for (StampedDetectedObjects object : detectedObjects) {
                    if (object.getTime() == tick.getCurrentTick() + camera.getFrequency()) {
                        //send detected objects event
                        DetectObjectsEvent event = new DetectObjectsEvent(object.getDetectedObjects(), this.getName(), tick.getCurrentTick());
    }
}