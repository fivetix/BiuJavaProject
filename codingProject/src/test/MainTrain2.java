package test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import graph.Agent;
import graph.Message;
import graph.ParallelAgent;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class MainTrain2 { // just a simple tests about the parallel agent to get you going...

    static String tn=null;
    static final AtomicInteger messageCount = new AtomicInteger(0);
    static final CountDownLatch latch = new CountDownLatch(1);

    public static class TestAgent1 implements Agent{
        
        public void reset() {
        }
        public void close() {
        }
        public String getName(){
            return getClass().getName();
        }

        @Override
        public void callback(String topic, Message msg) {
            tn = Thread.currentThread().getName();
            messageCount.incrementAndGet();
            latch.countDown();  // Signal that a message was processed
        }
        
    }
    
    public static void main(String[] args) {
        TopicManager tm = TopicManagerSingleton.get();
        int tc = Thread.activeCount();
        ParallelAgent pa = new ParallelAgent(new TestAgent1(), 10);
        tm.getTopic("A").subscribe(pa);

        // Test if a new thread is created
        if (Thread.activeCount() != tc + 1) {
            System.out.println("your ParallelAgent does not open a thread (-10)");
        }

        // Publish a message and check if the callback runs
        tm.getTopic("A").publish(new Message("a"));
        try {
            latch.await();  // Wait for the callback to be executed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tn == null) {
            System.out.println("your ParallelAgent didn't run the wrapped agent callback (-20)");
        } else {
            if (tn.equals(Thread.currentThread().getName())) {
                System.out.println("the ParallelAgent does not run the wrapped agent in a different thread (-10)");
            }
            String last = tn;
            tm.getTopic("A").publish(new Message("a"));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (!last.equals(tn))
                System.out.println("all messages should be processed in the same thread of ParallelAgent (-10)");
        }

        // Concurrency test with multiple agents
        final int numAgents = 10;
        ParallelAgent[] agents = new ParallelAgent[numAgents];
        for (int i = 0; i < numAgents; i++) {
            agents[i] = new ParallelAgent(new TestAgent1(), 10);
            tm.getTopic("B").subscribe(agents[i]);
        }

        // Publish multiple messages to the topic "B"
        for (int i = 0; i < 100; i++) {
            tm.getTopic("B").publish(new Message("message " + i));
        }

        try {
            Thread.sleep(1000);  // Wait for all messages to be processed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close all agents
        for (ParallelAgent agent : agents) {
            agent.close();
        }
        pa.close();

        // Edge case: closing while processing
        ParallelAgent pa2 = new ParallelAgent(new TestAgent1(), 10);
        tm.getTopic("C").subscribe(pa2);
        tm.getTopic("C").publish(new Message("test closing"));

        try {
            Thread.sleep(50);  // Give some time to start processing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pa2.close();  // Close the agent while processing

        System.out.println("done");
    }
}
