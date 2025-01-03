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
        System.out.println("eden and may are the bestx2");
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

            // Parse Pose data from pose JSON file
            Path poseFilePath = configDir.resolve(configFile.getPoseJsonFile());
            Reader poseReader = Files.newBufferedReader(poseFilePath);
            Type poseListType = new TypeToken<List<Pose>>() {}.getType();
            List<Pose> stumpedPoses = gson.fromJson(poseReader, poseListType);
            poseReader.close();

            int numOfThreads = 0;
            LiDarDataBase.getInstance(configFile.getLidarWorkers().getLidars_data_path());
            // creating PoseService
            List<Thread> threads = new ArrayList<>();
            //creating CameraServices
            List<Camera> camerasObj = new ArrayList<>();
            for (Camera cameraInfo : configFile.getCameras().getCamerasConfigurations()){
                List<StampedDetectedObjects> detectedObjects = cameras.get(cameraInfo.getCamera_key());
                Camera cameraObj = new Camera(cameraInfo.getId() , cameraInfo.getFrequency() , STATUS.UP , cameraInfo.getCamera_key() , detectedObjects);
                camerasObj.add(cameraObj);
                threads.add(new Thread(new CameraService(cameraObj)));
                numOfThreads++;
            }

            //creating Lidar workers
            List <LiDarWorkerTracker> LiDarWorkers = new ArrayList<>();
            LiDarWorkerTracker[] LidarConfigurations = configFile.getLidarWorkers().getLidarConfigurations();
            for (LiDarWorkerTracker lidar : LidarConfigurations){
                LiDarWorkerTracker lidarObj = new LiDarWorkerTracker(lidar.getId() , lidar.getFrequency() , STATUS.UP , lidar.getLastTrackedObjects());
                LiDarWorkers.add(lidarObj);
                threads.add(new Thread(new LiDarService(lidarObj)));
                numOfThreads++;
            }

            //initialize the time service at the end
            threads.add(new Thread(new TimeService(configFile.getTickTime()*1000, configFile.getDuration())));
            GPSIMU gps = new GPSIMU(0, STATUS.UP , stumpedPoses);
            threads.add(new Thread(new PoseService(gps)));
            numOfThreads++;

            // creating fusion slam
            FusionSlam fusionSlam = FusionSlam.getInstance();
            System.out.println(numOfThreads);
            fusionSlam.setNumOfUpThreads(numOfThreads); //for the counter
            threads.add(new Thread(new FusionSlamService(fusionSlam)));
            fusionSlam.setOutputFilePath(Paths.get(args[1])); //reset the outputFile

            // Start the simulation.
            System.out.println("Simulation started successfully.");
            // Start the timer
            long startTime = System.currentTimeMillis();
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // After all threads finish
            System.out.println("Simulation finished successfully.");
            // End the timer
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Simulation took: " + elapsedTime + " milliseconds.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Print the error details
        }
    }
}
