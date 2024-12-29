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
            Reader reader = Files.newBufferedReader(Paths.get(args[0]));
            configFile = gson.fromJson(reader, ConfigFile.class);
            reader.close();

            System.out.println(configFile.toString());


            Path configDir = Paths.get(args[0]).getParent();

            // Step 3: Resolve the poseJsonFile path
            Path poseFilePath = configDir.resolve(configFile.getPoseJsonFile());
            System.out.println("Resolved Pose JSON Path: " + poseFilePath);

            // Step 4: Read the pose data from the resolved path
            Reader poseReader = Files.newBufferedReader(poseFilePath);


            Type poseListType = new TypeToken<List<Pose>>() {}.getType();
            List<Pose> poses = gson.fromJson(poseReader, poseListType);
            poseReader.close();

            // Step 5: Print the parsed pose data for verification
            System.out.println("Parsed Pose Data: " + poses);


           /*
            // Read camera data from `camera_data.json`
           // String cameraDataPath = Paths.get(args[0]).getParent().resolve(configFile.getCameras().getCamera_datas_path()).toString();
          //  FileReader cameraReader = new FileReader(cameraDataPath);

            // Parse Pose data from pose JSON file
            FileReader poseReader = new FileReader(configFile.getPoseJsonFile());
            Pose initialPose = gson.fromJson(poseReader, Pose.class);
            Type poseListType = new TypeToken<List<Pose>>() {}.getType();
            List<Pose> stumpedPoses = gson.fromJson(poseReader, poseListType);
            poseReader.close();

            // Parse LiDAR data from lidar_data.json
            String lidarDataPath = configFile.getLidarWorkers().getLidars_data_path();
            FileReader lidarReader = new FileReader(lidarDataPath);

            // Initialize system components and services.

            // creating TimeService and PoseService
            List<Thread> threads = new ArrayList<>();
            threads.add(new Thread(new TimeService(configFile.getTickTime(), configFile.getDuration())));
            GPSIMU gps = new GPSIMU(0, STATUS.UP , stumpedPoses);
            threads.add(new Thread(new PoseService(gps)));

            //creating CameraServices
            List <Camera> cameras = new ArrayList<>();
            Camera[] cameraConfigurations = configFile.getCameras().getCamerasConfigurations();
            for (Camera cameraInfo : cameraConfigurations){
                Camera cameraObj = new Camera(cameraInfo.getId() , cameraInfo.getFrequency() , STATUS.UP , cameraInfo.getCamera_key() , cameraInfo.getStampedDetectedObjects());
                cameras.add(cameraObj);
                System.out.println(cameraObj.toString());
                threads.add(new Thread(new CameraService(cameraObj)));
            }

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
