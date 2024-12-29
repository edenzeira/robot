package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The main entry point for the GurionRock Pro Max Ultra Over 9000 simulation.
 * <p>
 * This class initializes the system and starts the simulation by setting up
 * services, objects, and configurations.
 * </p>
 */
public class GurionRockRunner {

    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the path to the configuration file.
     */
    public static void main(String[] args) {
        System.out.println("eden and may are the best");
        try {
            //Parse configuration file.
            ConfigFile configFile;
            Gson gson = new Gson();
            String configFilePath = "./configuration_file.json";
            Reader reader = Files.newBufferedReader(Paths.get(configFilePath));
            configFile = gson.fromJson(reader, ConfigFile.class);
            reader.close();
            Path configDir = Paths.get(configFilePath).getParent();


            // Parse camera data from camera JSON file
            Path cameraFilePath = configDir.resolve(configFile.getCameras().getCamera_datas_path());
            Reader cameraReader = Files.newBufferedReader(cameraFilePath);
            Type cameraMapType = new TypeToken<Map<String, List<StampedDetectedObjects>>>() {}.getType();
            Map<String, List<StampedDetectedObjects>> cameras = gson.fromJson(cameraReader, cameraMapType);
            cameraReader.close();
            System.out.println("Parsed Camera Data: " + cameras);

            // Parse Pose data from pose JSON file
            Path poseFilePath = configDir.resolve(configFile.getPoseJsonFile());
            Reader poseReader = Files.newBufferedReader(poseFilePath);
            Type poseListType = new TypeToken<List<Pose>>() {}.getType();
            List<Pose> stumpedPoses = gson.fromJson(poseReader, poseListType);
            poseReader.close();
            System.out.println("Parsed Pose Data: " + stumpedPoses);

            // Initialize system components and services.

            for (Pose pose : stumpedPoses) {
                System.out.println("Pose " + pose.toString() + '\'' );
            }

            // creating TimeService and PoseService
            List<Thread> threads = new ArrayList<>();
            threads.add(new Thread(new TimeService(configFile.getTickTime(), configFile.getDuration())));
            GPSIMU gps = new GPSIMU(0, STATUS.UP , stumpedPoses);
            System.out.println("gps " + gps.toString());
            threads.add(new Thread(new PoseService(gps)));

            //creating CameraServices
            List<Camera> camerasObj = new ArrayList<>();
            for (Camera cameraInfo : configFile.getCameras().getCamerasConfigurations()){
                Camera cameraObj = new Camera(cameraInfo.getId() , cameraInfo.getFrequency() , STATUS.UP , cameraInfo.getCamera_key() , cameraInfo.getStampedDetectedObjects());
                camerasObj.add(cameraObj);
                System.out.println(cameraObj.toString());
                threads.add(new Thread(new CameraService(cameraObj)));
            }

            /*
            // Parse LiDAR data from lidar_data.json
            Path lidarFilePath = configDir.resolve(configFile.getLidarWorkers().getLidars_data_path());
            Reader lidarReader = Files.newBufferedReader(lidarFilePath);
            Type lidarListType = new TypeToken<List<LiDarWorkerTracker>>() {}.getType();
            List<LiDarWorkerTracker> lidarData = gson.fromJson(lidarReader, lidarListType);
            lidarReader.close();
            System.out.println("Parsed LiDAR Data: " + lidarData);

             */


           /*
            //creating Lidar workers
            List <LiDarWorkerTracker> LiDarWorkers = new ArrayList<>();
            LiDarWorkerTracker[] LidarConfigurations = configFile.getLidarWorkers().getLidarConfigurations();
            for (LiDarWorkerTracker lidar : LidarConfigurations){
                LiDarWorkerTracker lidarObj = new LiDarWorkerTracker(lidar.getId() , lidar.getFrequency() , STATUS.UP , lidar.getLastTrackedObjects());
                LiDarWorkers.add(lidarObj);
                System.out.println(lidarObj.toString());
                for (TrackedObject obj: lidar.getLastTrackedObjects())
                    System.out.println(obj.toString());
                threads.add(new Thread(new LiDarService(lidarObj)));
            }

            // creating fusion slam
            FusionSlam fusionSlam = FusionSlam.getInstance();
            threads.add(new Thread(new FusionSlamService(fusionSlam)));

            // Start the simulation.
            System.out.println("Simulation started successfully.");
            // Start the timer
            long startTime = System.currentTimeMillis();
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join(); // do we need this??
            }

            // After all threads finish
            System.out.println("Simulation finished successfully.");
            // End the timer
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Simulation took: " + elapsedTime + " milliseconds.");

*/
//| InterruptedException e
        } catch (IOException e) { //the second interrupted is because of join - we need to check if we need this
            e.printStackTrace(); // Print the error details
        }
    }
}
