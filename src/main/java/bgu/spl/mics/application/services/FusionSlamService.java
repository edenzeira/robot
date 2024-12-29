package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.CloudPoint;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.StatisticalFolder;
import bgu.spl.mics.application.objects.TrackedObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 * <p>
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */
public class FusionSlamService extends MicroService {
    private FusionSlam fusionSlam;
    int currentTime = 0;
    int tickDuration = 0;
    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("FusionSlam");
        this.fusionSlam = fusionSlam;
    }

    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        //Subscribe to TickBroadcast to process ticks
        subscribeBroadcast(TickBroadcast.class, tick -> {
            currentTime = tick.getCurrentTick();
            tickDuration = tick.getTickDuration();
        });

        //Subscribe to TrackedObjectsEvent
        subscribeEvent(TrackedObjectsEvent.class, event -> {
            List<TrackedObject> list = event.getTrackedObjects();
            for (TrackedObject o : list) {
                fusionSlam.handle_trackedObjectEvent(o);
            }
        });

        //Subscribe to PoseEvent
        subscribeEvent(PoseEvent.class, event -> {
            fusionSlam.getPoses().add(event.getPose());
        });



        //Subscribe to TerminatedBroadcast to process ticks
        subscribeBroadcast(TerminatedBroadcast.class, terminated -> {
           /* try {
                Path configDir = Paths.get(configuration_file.json).getParent();
                Path outputFilePath = configDir.resolve("output_file.json");
                fusionSlam.writeToJson(configFilePath);
                StatisticalFolder.getInstance().writeToJson(configFilePath);
                //is it ok to write from 2 different places to the same json file??
                //add the errors when we will figure in out
                System.out.println("JSON file written successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to write JSON file.");
            }*/
            terminate();
        });

        //subscribe to CrushedBroadcast
        subscribeBroadcast(CrashedBroadcast.class, crashed -> {
        //outputFile
        });
    }
}
