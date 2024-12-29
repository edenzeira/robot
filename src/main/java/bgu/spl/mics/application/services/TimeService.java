package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.List;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {
    private final int TickTime;
    private final int Duration;

    /**
     * Constructor for TimeService.
     *
     * @param TickTime  The duration of each tick in milliseconds.
     * @param Duration  The total number of ticks before the service terminates.
     */
    public TimeService(int TickTime, int Duration) {
        super("Time");
        this.TickTime = TickTime;
        this.Duration = Duration;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {
        //subscribe for time tick -> waits ticktime then sends a time tick
        //Subscribe to TickBroadcast to process ticks
        subscribeBroadcast(TickBroadcast.class, tick -> {
            if (tick.getCurrentTick() <= Duration) {
                try {
                    Thread.sleep(TickTime);
                } catch (InterruptedException ignored) {}
                sendBroadcast(new TickBroadcast(this.getName(), tick.getCurrentTick() + 1));
                StatisticalFolder.getInstance().setSystemRuntime(tick.getCurrentTick()); //update the statisticalFolder
            } else {
                sendBroadcast(new TerminatedBroadcast(this.getName()));
            }
        });
        //subscribe to TerminatedBroadcast
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
            terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
            terminate();
        });

        //start the timer
        sendBroadcast(new TickBroadcast(this.getName(), 1));
    }
}