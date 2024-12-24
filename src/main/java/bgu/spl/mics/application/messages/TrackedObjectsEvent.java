package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class TrackedObjectsEvent implements Event<String> {
    private String senderName;

    public TrackedObjectsEvent(String senderName) {
        this.senderName = senderName;
    }

    public String isTrackingStatus() {
        return senderName;
    }
}
