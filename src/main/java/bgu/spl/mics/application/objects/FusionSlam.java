package bgu.spl.mics.application.objects;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    // Singleton instance holder
    private static class FusionSlamHolder {
        private List<StampedCloudPoints> stampedCloudPoints;

        public FusionSlam(List<StampedCloudPoints> stampedCloudPoints) {
            this.stampedCloudPoints = stampedCloudPoints;
        }

        public List<StampedCloudPoints> getStampedCloudPoints() {
            return stampedCloudPoints;
        }

        public void processSlamData() {
            // Implement SLAM logic here
        }
    }
}
