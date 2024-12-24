package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class PoseEvent implements Event<String> {
    private String senderName;

    public PoseEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getObjectId() {
        return senderName;
    }
}
