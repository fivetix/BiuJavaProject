package configs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import graph.Agent;
import graph.ParallelAgent;

public class GenericConfig implements Config {
    private String confFile;
    private final List<ParallelAgent> agents = new ArrayList<>();

    public void setConfFile(String confFile) {
        this.confFile = confFile;
    }

    @Override
    public void create() {
        List<String> lines = new ArrayList<>();

        // Read configuration file
        try (BufferedReader reader = new BufferedReader(new FileReader(confFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Validate the configuration file
        if (lines.size() % 3 != 0) {
            throw new IllegalArgumentException("Invalid configuration file format");
        }

        // Process each agent configuration
        for (int i = 0; i < lines.size(); i += 3) {
            String className = lines.get(i).trim();
            String[] subs = lines.get(i + 1).trim().split(",");
            String[] pubs = lines.get(i + 2).trim().split(",");

            try {
                // Load the agent class
                Class<?> agentClass = Class.forName(className);

                // Get the constructor with parameters (String, String[], String[])
                Constructor<?> constructor = agentClass.getConstructor(String.class, String[].class, String[].class);

                // Create an instance of the agent
                Agent agent = (Agent) constructor.newInstance(className, subs, pubs);

                // Wrap the agent with ParallelAgent
                ParallelAgent parallelAgent = new ParallelAgent(agent, 10);
                agents.add(parallelAgent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "Generic Config";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void close() {
        for (ParallelAgent agent : agents) {
            agent.close();
        }
    }
}
