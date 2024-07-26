package configs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.Message;

public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;
    private int x, y; // מיקום הצומת

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getEdges() {
        return edges;
    }

    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    public Message getMessage() {
        return msg;
    }

    public void setMessage(Message msg) {
        this.msg = msg;
    }

    public void addEdge(Node node) {
        this.edges.add(node);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> stack = new HashSet<>();
        return hasCyclesHelper(this, visited, stack);
    }

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
}
