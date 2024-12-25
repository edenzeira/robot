package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private int processedFrames;
    private double successRate;

    public StatisticalFolder(int processedFrames, double successRate) {
        this.processedFrames = processedFrames;
        this.successRate = successRate;
    }

    public int getProcessedFrames() {
        return processedFrames;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void updateStatistics(int newFrames, double newSuccessRate) {
        this.processedFrames += newFrames;
        this.successRate = newSuccessRate;
    }
}
