package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

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
    protected void initialize() { //do we need a thread here?!
        int currentTick = 0;
        while (currentTick < Duration) {
            long currentTimeMillis = System.currentTimeMillis();
            if (System.currentTimeMillis() == currentTimeMillis + TickTime) //sends new tick broadcast every tickTime
                sendBroadcast(new TickBroadcast(this.getName(), currentTick));
            currentTick ++;
        }
        sendBroadcast(new TerminatedBroadcast(this.getName()));
        //do we need this here or we can do it in tne microservice in the terminated
        terminate();
    }
}