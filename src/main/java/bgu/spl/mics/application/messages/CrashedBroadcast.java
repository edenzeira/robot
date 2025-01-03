package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class CrashedBroadcast implements Broadcast {
    private int senderId;
    private String errorDescription;
    private String faultySensor;

    public CrashedBroadcast(int senderId, String errorDescription, String faultySensor) {
        this.senderId = senderId;
        this.errorDescription = errorDescription;
        this.faultySensor = faultySensor;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getFaultySensor() {
        return faultySensor;
    }

    public int getSenderId() {
        return senderId;
    }

}
