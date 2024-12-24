package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Pose;

public class PoseEvent implements Event<String> {
    private String senderName;
    private Pose pose;

    public PoseEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getObjectId() {
        return senderName;
    }
}
