package bgu.spl.mics.application.objects;

import java.util.Arrays;

public class LidarWorkers {
    private LiDarWorkerTracker[] LidarConfigurations;
    private String lidars_data_path;

    public LiDarWorkerTracker[] getLidarConfigurations() {
        return LidarConfigurations;
    }

    public String getLidars_data_path() {
        return lidars_data_path;
    }

    public void setLidarConfigurations(LiDarWorkerTracker[] lidarConfigurations) {
        LidarConfigurations = lidarConfigurations;
    }

    public void setLidars_data_path(String lidars_data_path) {
        this.lidars_data_path = lidars_data_path;
    }

    @Override
    public String toString() {
        return "LidarWorkers{" +
                "LidarConfigurations=" + Arrays.toString(LidarConfigurations) +
                ", lidars_data_path='" + lidars_data_path + '\'' +
                '}';
    }
}
