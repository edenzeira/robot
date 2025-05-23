package bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    private ArrayList<LandMark> landMarks;
    private List<Pose> poses;
    int numOfUpThreads;
    private Path outputFilePath;
    //ConcurrentHashMap<LandMark, Integer> avarageCounterMap = new ConcurrentHashMap<>();

    // Singleton instance holder
    private static class FusionSlamHolder {
        private static FusionSlam instance = new FusionSlam();

    }

    private FusionSlam() {
        this.landMarks = new ArrayList<LandMark>() ;
        this.poses = new ArrayList<Pose>() ;
        this.numOfUpThreads = 0;
        outputFilePath = null;
    }

    public static FusionSlam getInstance() {
        return FusionSlamHolder.instance;
    }

    public void setOutputFilePath(Path path){
        this.outputFilePath = path;
    }

    public void setNumOfUpThreads(int numOfDownThreads){
        this.numOfUpThreads = numOfDownThreads;
    }

    public void reduceNumOfUpThreads(){
        numOfUpThreads--;
    }

    public String toString() {
        return "FusionSlam{" +
                "landMarks=" + landMarks +
                ", Poses=" + poses +
                '}';
    }

    public List<LandMark> getLandMarks() {
        return landMarks;
    }

    public int getNumOfUpThreads(){
        return numOfUpThreads;
    }

    public List<Pose> getPoses() {
        return poses;
    }

    public void outputFile(){
        try {
            StatisticalFolder.getInstance().updateLandMarks(landMarks);
            Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(outputFilePath);
            writer.write(gson.toJson(StatisticalFolder.getInstance()));
            writer.close();
        }
        catch (IOException e) {}
    }

    public List<CloudPoint> calculateAvarage(LandMark landMark, List<CloudPoint> globalCoordinates){
        List<CloudPoint> oldCoordinates = landMark.getCoordinates();
        //calculate the avarage for x and y in each cloudPoint of the existing object with the new data
        List<CloudPoint> newCoordinates = new ArrayList<>();
        for (int i = 0; i < oldCoordinates.size() & i < globalCoordinates.size(); i++) {
            double newX = (oldCoordinates.get(i).getX() + globalCoordinates.get(i).getX())/2;
            double newY = (oldCoordinates.get(i).getY() + globalCoordinates.get(i).getY())/2;
            newCoordinates.add(new CloudPoint(newX, newY));
        }
        return newCoordinates;
    }

    public CloudPoint calculateGlobalCoordinates(Pose p, CloudPoint localCP){
        double xRobot = p.getX();
        double yRobot = p.getY();
        double yawDeg = p.getYaw();
        double xLocal = localCP.getX();
        double yLocal = localCP.getY();
        double yawRad = yawDeg*(Math.PI/180);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        double xGlobal = cos * xLocal - sin * yLocal + xRobot;
        double yGlobal = sin * xLocal + cos * yLocal + yRobot;
        return new CloudPoint(xGlobal, yGlobal);
    }

    public Pose isPoseExist(TrackedObject o){
        //finds the relevant pose of the robot according to the time
        Pose robotP = new Pose(-1,-1,-1,-1);
        for (Pose p : poses){
            if (o.getTime() == p.getTime()){
                robotP = p;
                break;
            }
        }
        return robotP;
    }


    public void handle_trackedObjectEvent(Pose robotP , TrackedObject o) {
        //calculates for each cloud point of the object the global point
        ArrayList<CloudPoint> globalCoordinates = new ArrayList<CloudPoint>();
        for (CloudPoint cp : o.getCoordinates()){
            globalCoordinates.add((calculateGlobalCoordinates(robotP, cp)));
        }

        //checks if object already mapped before
        boolean objectExists = false;
        for (LandMark landMark : landMarks) {
            if (landMark.getId().equals(o.getId())) {
                objectExists = true;
                //calculate and update the relevant landmark
                landMark.setCoordinates(calculateAvarage(landMark, globalCoordinates));
                break;
            }
        }
        //if it is a new object, adds new landmark and update statisticalFolder
        if (!objectExists) {
            LandMark newLandMark = new LandMark(o.getId(), o.getDescription(), globalCoordinates);
            landMarks.add(newLandMark);
            int NumLandmarks = StatisticalFolder.getInstance().getNumLandmarks();
            StatisticalFolder.getInstance().setNumLandmarks(NumLandmarks + 1);
        }
    }
}
