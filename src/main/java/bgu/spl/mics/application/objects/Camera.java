package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.DetectObjectsEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private final int id;
    private final int frequency;
    private STATUS status;
    private String camera_key;
    private final List<StampedDetectedObjects> stampedDetectedObjects;
    private StampedDetectedObjects lastFrame;
    private int taskCounter;

    public Camera(int id, int frequency, STATUS status,String camera_key, List<StampedDetectedObjects> stampedDetectedObjects) {
        this.id = id;
        this.frequency = frequency;
        this.status = status;
        this.camera_key = camera_key;
        this.stampedDetectedObjects = stampedDetectedObjects;
        this.taskCounter = 0;

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


    public List<StampedDetectedObjects> getStampedDetectedObjects() {
        return stampedDetectedObjects;
    }

    public StampedDetectedObjects handle_tick(int currentTime) {
        if (stampedDetectedObjects != null) {
            // Retrieves a detectedObjectsList according to the time and frequancy
            for (StampedDetectedObjects list : stampedDetectedObjects) {
                if (list.getTime() + frequency == currentTime) {
                    taskCounter++;
                    return list;
                }
            }
        }
        return null;
    }

    public String getCamera_key() {
        return camera_key;
    }


    public void setCamera_key(String camera_key) {
        this.camera_key = camera_key;
    }
    public String toString() {
        String s = "";
        if (stampedDetectedObjects != null) {
            for (StampedDetectedObjects stampedDetectedObjects : stampedDetectedObjects)
                s = s + stampedDetectedObjects.toString();
        }
        return "CameraInfo{" +
                "id=" + id +
                ", frequency=" + frequency +
                ", camera_key='" + camera_key + '\'' +
                s + '\'' +
                '}';
    }

    public StampedDetectedObjects getCurrentStampted(int currentTick) {
        for(StampedDetectedObjects obj: stampedDetectedObjects) {
            if(obj.getTime() + frequency == currentTick){
                return obj;
            }
        }
        return null;
    }

    public void updateStatus(){
        if(taskCounter == stampedDetectedObjects.size()){
            status = STATUS.DOWN;
        }
    }

    public StampedDetectedObjects getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(StampedDetectedObjects s) {
        lastFrame = s;
    }
}
