package test;

import java.util.Random;

import configs.GenericConfig;
import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

public class MainTrain4 {

    public static void main(String[] args) {
        int c = Thread.activeCount();
        GenericConfig gc = new GenericConfig();
        gc.setConfFile("C:\\Users\\User\\eclipse-workspace\\codingProject\\config_files\\simple.conf.txt"); // change to the exact location where you put the file.
        gc.create();

        if (Thread.activeCount() != c + 2) {
            System.out.println("the configuration did not create the right number of threads (-10)");
        }

        double result[] = {0.0};

        TopicManagerSingleton.get().getTopic("D").subscribe(new Agent() {

            @Override
            public String getName() {
                return "";
            }

            @Override
            public void reset() {
            }

            @Override
            public void callback(String topic, Message msg) {
                result[0] = msg.asDouble;
            }

            @Override
            public void close() {
            }

        });

        Random r = new Random();

        // בדיקה עם ערכים רגילים
        for (int i = 0; i < 9; i++) {
            int x, y;
            x = r.nextInt(1000);
            y = r.nextInt(1000);
            TopicManagerSingleton.get().getTopic("A").publish(new Message(x));
            TopicManagerSingleton.get().getTopic("B").publish(new Message(y));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if (result[0] != x + y + 1) {
                System.out.println("your agents did not produce the desired result (-10)");
            }
        }

        // בדיקה עם ערכים שליליים
        for (int i = 0; i < 9; i++) {
            int x, y;
            x = -r.nextInt(1000);
            y = -r.nextInt(1000);
            TopicManagerSingleton.get().getTopic("A").publish(new Message(x));
            TopicManagerSingleton.get().getTopic("B").publish(new Message(y));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if (result[0] != x + y + 1) {
                System.out.println("your agents did not produce the desired result with negative values (-10)");
            }
        }

        // בדיקה עם אפסים
        for (int i = 0; i < 9; i++) {
            int x = 0, y = 0;
            TopicManagerSingleton.get().getTopic("A").publish(new Message(x));
            TopicManagerSingleton.get().getTopic("B").publish(new Message(y));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if (result[0] != x + y + 1) {
                System.out.println("your agents did not produce the desired result with zero values (-10)");
            }
        }

        // בדיקה עם מספרים גדולים אך לא קרובים ל-Integer.MAX_VALUE
        for (int i = 0; i < 9; i++) {
            int x, y;
            x = 100000 + r.nextInt(100000);
            y = 100000 + r.nextInt(100000);
            TopicManagerSingleton.get().getTopic("A").publish(new Message(x));
            TopicManagerSingleton.get().getTopic("B").publish(new Message(y));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if (result[0] != x + y + 1) {
                System.out.println("your agents did not produce the desired result with large values (-10)");
            }
        }

        // בדיקה עם הערכים המינימליים והמקסימליים
        TopicManagerSingleton.get().getTopic("A").publish(new Message(Integer.MIN_VALUE));
        TopicManagerSingleton.get().getTopic("B").publish(new Message(Integer.MAX_VALUE));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (result[0] != (double) Integer.MIN_VALUE + Integer.MAX_VALUE + 1) {
            System.out.println("your agents did not produce the desired result with min and max values (-10)");
        }

        // בדיקה עם ערכים כפולים
        int x = r.nextInt(1000);
        int y = r.nextInt(1000);
        for (int i = 0; i < 3; i++) {
            TopicManagerSingleton.get().getTopic("A").publish(new Message(x));
            TopicManagerSingleton.get().getTopic("B").publish(new Message(y));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (result[0] != x + y + 1) {
                System.out.println("your agents did not produce the desired result with duplicate values (-10)");
            }
        }

        // בדיקה עם ערכים משתנים במהירות
        for (int i = 0; i < 9; i++) {
            x = r.nextInt(1000);
            y = r.nextInt(1000);
            TopicManagerSingleton.get().getTopic("A").publish(new Message(x));
            TopicManagerSingleton.get().getTopic("B").publish(new Message(y));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            if (result[0] != x + y + 1) {
                System.out.println("your agents did not produce the desired result with rapid changing values (-10)");
            }
        }

        gc.close();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        if (Thread.activeCount() != c) {
            System.out.println("your code did not close all threads (-10)");
        }

        System.out.println("done");
    }
}
