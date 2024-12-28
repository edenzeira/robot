package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private String senderId;
    private int currentTick;
    private int tickDuration;

    public TickBroadcast(String senderId, int currentTick) {
        this.senderId = senderId;
        this.currentTick = currentTick;
    }

    public String getSenderId() {
        return senderId;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getTickDuration() {
        return tickDuration;
    }
}
