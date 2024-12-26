package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

import java.util.List;

public class DetectObjectsEvent implements Event<Boolean> {
    private String senderName;
    private  int time;
    private List<DetectedObject> detectedObjects;

    public DetectObjectsEvent(List<DetectedObject> detectedObjects, String senderName, int time) {
        this.senderName = senderName;
        this.time = time;
        this.detectedObjects = detectedObjects;
    }

    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }

    public int getTime() {
        return time;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getObjectId() {
        return senderName;
    }

}