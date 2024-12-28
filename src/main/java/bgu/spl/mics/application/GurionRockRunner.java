package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.ConfigFile;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.services.PoseService;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        // TODO: Parse configuration file.
            ConfigFile configFile;
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(args[0]));
            configFile = gson.fromJson(reader, ConfigFile.class);
            reader.close();
            // TODO: Initialize system components and services.

            List<Thread> threads = new ArrayList<>();
            threads.add(new Thread(new TimeService(configFile.getTickTime(), configFile.getDuration())));
            //GPSIMU gps = new GPSIMU(0, STATUS.UP,)
            //threads.add(new Thread(new PoseService(configFile.getTickTime(), configFile.getDuration())));

        } catch (IOException e) {}
        // TODO: Start the simulation.
    }
}
