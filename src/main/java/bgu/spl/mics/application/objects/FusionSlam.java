package bgu.spl.mics.application.objects;

import java.util.*;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    private List<LandMark> landMarks;
    private List<Pose> Poses;
    // Singleton instance holder
    private static class FusionSlamHolder {
        private static FusionSlam instance = new FusionSlam();

    }

        private FusionSlam() {
            this.landMarks = new ArrayList<LandMark>() ;
            this.Poses = new ArrayList<Pose>() ;
        }

        public FusionSlam getInstance() {
            return FusionSlamHolder.instance;
        }
}
