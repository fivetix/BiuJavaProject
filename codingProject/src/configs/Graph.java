package configs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node> {

    // Method to check if the graph contains cycles
    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();

        for (Node node : this) {
            if (hasCyclesHelper(node, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    // Recursive helper method to detect cycles
    private boolean hasCyclesHelper(Node node, Set<Node> visited, Set<Node> stack) {
        if (stack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        stack.add(node);

        for (Node neighbor : node.getEdges()) {
            if (hasCyclesHelper(neighbor, visited, stack)) {
                return true;
            }
        }

        stack.remove(node);
        return false;
    }

    // Method to initialize the graph from topics and agents in TopicManager
    public void createFromTopics() {
        TopicManager tm = TopicManagerSingleton.get();
        Set<String> createdNodes = new HashSet<>();

        int x = 50, y = 50, step = 100;

        // Create nodes for all topics
        for (Topic topic : tm.getTopics()) {
            String topicName = "T" + topic.name;
            if (!createdNodes.contains(topicName)) {
                Node topicNode = new Node(topicName);
                topicNode.setX(x);
                topicNode.setY(y);
                this.add(topicNode);
                createdNodes.add(topicName);
                y += step;
                if (y > 500) {
                    y = 50;
                    x += step;
                }
            }

            // Add edges from topic to its subscribers
            for (Agent sub : topic.getSubs()) {
                String agentName = "A" + sub.getName();
                if (!createdNodes.contains(agentName)) {
                    Node agentNode = new Node(agentName);
                    agentNode.setX(x);
                    agentNode.setY(y);
                    this.add(agentNode);
                    createdNodes.add(agentName);
                    y += step;
                    if (y > 500) {
                        y = 50;
                        x += step;
                    }
                }
                getNodeByName(topicName).addEdge(getNodeByName(agentName));
            }
        }

        // Create nodes for all agents
        for (Topic topic : tm.getTopics()) {
            for (Agent pub : topic.getPubs()) {
                String agentName = "A" + pub.getName();
                if (!createdNodes.contains(agentName)) {
                    Node agentNode = new Node(agentName);
                    agentNode.setX(x);
                    agentNode.setY(y);
                    this.add(agentNode);
                    createdNodes.add(agentName);
                    y += step;
                    if (y > 500) {
                        y = 50;
                        x += step;
                    }
                }

                // Add edges from agent to its published topics
                String topicName = "T" + topic.name;
                getNodeByName(agentName).addEdge(getNodeByName(topicName));
            }
        }
    }

    // Helper method to get a node by its name
    private Node getNodeByName(String name) {
        for (Node node : this) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }
}
