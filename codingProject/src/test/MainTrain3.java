package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import configs.Graph;
import configs.Node;
import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;
import configs.Config;

public class MainTrain3 {

    public static boolean hasCycles(List<Node> graph) {
        for (Node node : graph) {
            if (node.hasCycles()) {
                return true;
            }
        }
        return false;
    }

    public static void testCycles(){
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
    
        a.addEdge(b);
        b.addEdge(c);
        c.addEdge(d);
    
        // Create a graph
        List<Node> graph = new ArrayList<>();
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
    
        // Check if the graph has cycles
        boolean hasCycles = hasCycles(graph);
        if(hasCycles)
            System.out.println("wrong answer for hasCycles when there are no cycles (-20)");

        d.addEdge(a);
        hasCycles = hasCycles(graph);
        if(!hasCycles)
            System.out.println("wrong answer for hasCycles when there is a cycle (-10)");
    }

    public static void testSelfLoop() {
        Node a = new Node("A");
        a.addEdge(a); // Self-loop

        List<Node> graph = new ArrayList<>();
        graph.add(a);

        // Check if the graph has cycles
        boolean hasCycles = hasCycles(graph);
        if(!hasCycles)
            System.out.println("wrong answer for hasCycles when there is a self-loop (-10)");
    }

    public static void testDisconnectedGraph() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
    
        a.addEdge(b);
        b.addEdge(c);
        d.addEdge(e);
    
        // Create a graph
        List<Node> graph = new ArrayList<>();
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
    
        // Check if the graph has cycles
        boolean hasCycles = hasCycles(graph);
        if(hasCycles)
            System.out.println("wrong answer for hasCycles when there are no cycles in a disconnected graph (-20)");
    }

    public static void testMultipleComponents() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
        Node f = new Node("F");
    
        a.addEdge(b);
        b.addEdge(c);
        d.addEdge(e);
        e.addEdge(f);
        f.addEdge(d); // Create a cycle in second component
    
        // Create a graph
        List<Node> graph = new ArrayList<>();
        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);
        graph.add(e);
        graph.add(f);
    
        // Check if the graph has cycles
        boolean hasCycles = hasCycles(graph);
        if(!hasCycles)
            System.out.println("wrong answer for hasCycles when there is a cycle in one of the components (-10)");
    }

    public static class GetAgent implements Agent {
        public Message msg;
        public GetAgent(String topic) {
            TopicManagerSingleton.get().getTopic(topic).subscribe(this);
        }

        @Override
        public String getName() { return "Get Agent"; }

        @Override
        public void reset() {}

        @Override
        public void callback(String topic, Message msg) {
            this.msg = msg;
        }

        @Override
        public void close() {}
    }

    public static void testBinGraph() {
        TopicManager tm = TopicManagerSingleton.get();
        tm.clear();
        Config c = new MathExampleConfig();
        c.create();

        GetAgent ga = new GetAgent("R3");

        Random r = new Random();
        int x = 1 + r.nextInt(100);
        int y = 1 + r.nextInt(100);
        tm.getTopic("A").publish(new Message(x));
        tm.getTopic("B").publish(new Message(y));
        double rslt = (x + y) * (x - y);

        if (Math.abs(rslt - ga.msg.asDouble) > 0.05) {
            System.out.println("your BinOpAgents did not produce the desired result (-20)");
        }
    }

    public static void testBinGraphZeroInput() {
        TopicManager tm = TopicManagerSingleton.get();
        tm.clear();
        Config c = new MathExampleConfig();
        c.create();

        GetAgent ga = new GetAgent("R3");

        tm.getTopic("A").publish(new Message(0.0));
        tm.getTopic("B").publish(new Message(0.0));
        double rslt = (0.0 + 0.0) * (0.0 - 0.0);

        if (Math.abs(rslt - ga.msg.asDouble) > 0.05) {
            System.out.println("your BinOpAgents did not produce the desired result with zero inputs (-20)");
        }
    }

    public static void testBinGraphNegativeInput() {
        TopicManager tm = TopicManagerSingleton.get();
        tm.clear();
        Config c = new MathExampleConfig();
        c.create();

        GetAgent ga = new GetAgent("R3");

        int x = -5;
        int y = -3;
        tm.getTopic("A").publish(new Message(x));
        tm.getTopic("B").publish(new Message(y));
        double rslt = (x + y) * (x - y);

        if (Math.abs(rslt - ga.msg.asDouble) > 0.05) {
            System.out.println("your BinOpAgents did not produce the desired result with negative inputs (-20)");
        }
    }

    public static void testBinGraphMixedInput() {
        TopicManager tm = TopicManagerSingleton.get();
        tm.clear();
        Config c = new MathExampleConfig();
        c.create();

        GetAgent ga = new GetAgent("R3");

        int x = 7;
        int y = -4;
        tm.getTopic("A").publish(new Message(x));
        tm.getTopic("B").publish(new Message(y));
        double rslt = (x + y) * (x - y);

        if (Math.abs(rslt - ga.msg.asDouble) > 0.05) {
            System.out.println("your BinOpAgents did not produce the desired result with mixed inputs (-20)");
        }
    }
    


    public static void testTopicsGraph(){
        TopicManager tm=TopicManagerSingleton.get();
        tm.clear();
        Config c=new MathExampleConfig();
        c.create();
        Graph g=new Graph();
        g.createFromTopics();

        if(g.size()!=8)
            System.out.println("the graph you created from topics is not in the right size (-10)");
        
        List<String> l=Arrays.asList("TA","TB","Aplus","Aminus","TR1","TR2","Amul","TR3");
        boolean b=true;
        for(Node n  : g){
            b&=l.contains(n.getName());
        }
        if(!b)
            System.out.println("the graph you created from topics has wrong names to Nodes (-10)");

        if (g.hasCycles())
            System.out.println("Wrong result in hasCycles for topics graph without cycles (-10)");

        GetAgent ga=new GetAgent("R3");
        tm.getTopic("A").addPublisher(ga); // cycle
        g.createFromTopics();

        if (!g.hasCycles())
            System.out.println("Wrong result in hasCycles for topics graph with a cycle (-10)");
    }
    
    
    public static void main(String[] args) {
    	testCycles();
        testSelfLoop();
        testDisconnectedGraph();
        testMultipleComponents();
        testBinGraph();
        testBinGraphZeroInput();
        testBinGraphNegativeInput();
        testBinGraphMixedInput();
        testTopicsGraph();
        System.out.println("done");
    }

}
