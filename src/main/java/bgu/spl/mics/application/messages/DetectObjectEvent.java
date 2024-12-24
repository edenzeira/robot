package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;

import java.util.List;

public class DetectObjectEvent implements Event<Boolean> {
    private String senderName;
    private  int time;
    private List<DetectedObject> detectedObjects;

    public DetectObjectEvent(String senderName) {
        this.senderName = senderName;
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
