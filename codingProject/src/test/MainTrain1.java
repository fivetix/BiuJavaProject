package test;

import java.util.Random;

import graph.Agent;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class MainTrain1 { // simple tests to get you going...


	public static void testMessage() {
	    // Test String constructor
	    String testString = "Hello";
	    Message msgFromString = new Message(testString);
	    if (!testString.equals(msgFromString.asText)) {
	        System.out.println("Error: String constructor - asText does not match input string (-5)");
	    }
	    if (!java.util.Arrays.equals(testString.getBytes(), msgFromString.data)) {
	        System.out.println("Error: String constructor - data does not match input string bytes (-5)");
	    }
	    if (!Double.isNaN(msgFromString.asDouble)) {
	        System.out.println("Error: String constructor - asDouble should be NaN for non-numeric string (-5)");
	    }
	    if (msgFromString.date == null) {
	        System.out.println("Error: String constructor - date should not be null (-5)");
	    }

	    // Test Double constructor
	    double testDouble = 3.14;
	    Message msgFromDouble = new Message(testDouble);
	    if (!Double.toString(testDouble).equals(msgFromDouble.asText)) {
	        System.out.println("Error: Double constructor - asText does not match input double (-5)");
	    }
	    if (Double.isNaN(msgFromDouble.asDouble)) {
	        System.out.println("Error: Double constructor - asDouble should not be NaN for numeric double (-5)");
	    }
	    if (msgFromDouble.date == null) {
	        System.out.println("Error: Double constructor - date should not be null (-5)");
	    }

	} 

    public static  abstract class AAgent implements Agent{
        public void reset() {}
        public void close() {}
        public String getName(){
            return getClass().getName();
        }
    }

    public static class TestAgent1 extends AAgent{

        double sum=0;
        int count=0;
        TopicManager tm=TopicManagerSingleton.get();

        public TestAgent1(){
            tm.getTopic("Numbers").subscribe(this);
        }

        @Override
        public void callback(String topic, Message msg) {
            count++;
            sum+=msg.asDouble;
            
            if(count%5==0){
                tm.getTopic("Sum").publish(new Message(sum));
                count=0;
            }

        }
        
    }

    public static class TestAgent2 extends AAgent{

        double sum=0;
        TopicManager tm=TopicManagerSingleton.get();

        public TestAgent2(){
            tm.getTopic("Sum").subscribe(this);
        }

        @Override
        public void callback(String topic, Message msg) {
            sum=msg.asDouble;
        }

        public double getSum(){
            return sum;
        }
        
    }

    public static void testAgents() {
        TopicManager tm = TopicManagerSingleton.get();
        TestAgent1 a = new TestAgent1();
        TestAgent2 a2 = new TestAgent2();
        double sum = 0;

        // Publish messages and validate
        for (int c = 0; c < 3; c++) {
            Topic num = tm.getTopic("Numbers");
            Random r = new Random();
            for (int i = 0; i < 5; i++) {
                int x = r.nextInt(1000);
                num.publish(new Message(x));
                sum += x;
            }
            double result = a2.getSum();
            if (result != sum) {
                System.out.println("Error: Published result does not match expected sum (-10)");
            }
        }

        // Clean up after testing
        a.close();
        a2.close();
    }
    
    public static void testTopicManager() {
        TopicManager tm = TopicManagerSingleton.get();

        // Test getting topics
        Topic topic1 = tm.getTopic("Topic1");
        Topic topic2 = tm.getTopic("Topic2");

        // Test publishing and subscribing
        topic1.subscribe(new TestAgent1());
        topic2.subscribe(new TestAgent2());

        // Publish messages to topics
        topic1.publish(new Message("Hello Topic1"));
        topic2.publish(new Message("Hello Topic2"));

    }
    
    

        
    public static void main(String[] args) {
        testMessage();
        testAgents();   
        testTopicManager();
        System.out.println("done");
    }
}
