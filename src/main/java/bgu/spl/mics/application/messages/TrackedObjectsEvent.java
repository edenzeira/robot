package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

public class TrackedObjectsEvent implements Event<String> {
    private String senderName;
    private List<TrackedObject> trackedObjects;
    public TrackedObjectsEvent(String senderName, List<TrackedObject> trackedObjects) {
        this.senderName = senderName;
        this.trackedObjects = trackedObjects;
    }

    public List<TrackedObject> getTrackedObjects() {
        return trackedObjects;
    }

    public String isTrackingStatus() {
        return senderName;
    }
}
