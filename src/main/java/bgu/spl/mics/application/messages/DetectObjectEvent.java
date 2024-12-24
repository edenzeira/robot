package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DetectObjectEvent implements Event<String> {
    private String senderName;

    public DetectObjectEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getObjectId() {
        return senderName;
    }
}
