package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Pose;

public class PoseEvent implements Event<String> {
    private String senderName;
    private Pose pose;

    public PoseEvent(String senderName, Pose pose) {
        this.senderName = senderName;
        this.pose = pose;
    }

    public String getSenderName() {
        return senderName;
    }

    public Pose getPose() {
        return pose;
    }
}
