package bgu.spl.mics.application.objects;

import java.util.Arrays;

public class Cameras {
    private Camera[] CamerasConfigurations;
    private String camera_datas_path;

    public Camera[] getCamerasConfigurations() {
        return CamerasConfigurations;
    }

    public String getCamera_datas_path() {
        return camera_datas_path;
    }

    public void setCamerasConfigurations(Camera[] camerasConfigurations) {
        CamerasConfigurations = camerasConfigurations;
    }

    public void setCamera_datas_path(String camera_datas_path) {
        this.camera_datas_path = camera_datas_path;
    }

    @Override
    public String toString() {
        return "Cameras{" +
                "CamerasConfigurations=" + Arrays.toString(CamerasConfigurations) +
                ", camera_datas_path='" + camera_datas_path + '\'' +
                '}';
    }
}
