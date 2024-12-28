package bgu.spl.mics.application.objects;

public class ConfigFile {
    private Cameras Cameras;
    private LidarWorkers LidarWorkers;
    private String poseJsonFile;
    private int TickTime;
    private int Duration;

    public Cameras getCameras() {
        return Cameras;
    }

    public LidarWorkers getLidarWorkers() {
        return LidarWorkers;
    }

    public String getPoseJsonFile() {
        return poseJsonFile;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }

    public void setCameras(Cameras cameras) {
        Cameras = cameras;
    }

    public void setLidarWorkers(LidarWorkers lidarWorkers) {
        LidarWorkers = lidarWorkers;
    }

    public void setPoseJsonFile(String poseJsonFile) {
        this.poseJsonFile = poseJsonFile;
    }

    public void setTickTime(int tickTime) {
        TickTime = tickTime;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    @Override
    public String toString() {
        return "ConfigFile{" +
                "Cameras=" + Cameras +
                ", LidarWorkers=" + LidarWorkers +
                ", poseJsonFile='" + poseJsonFile + '\'' +
                ", TickTime=" + TickTime +
                ", Duration=" + Duration +
                '}';
    }
}
