package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class CrashedBroadcast implements Broadcast {
    private String senderId;
    private String errorDescription;
    private String faultySensor;

    public CrashedBroadcast(String senderId, String errorDescription, String faultySensor) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

}
