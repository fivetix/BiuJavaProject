package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {
    private final Agent agent;
    private final BlockingQueue<MessageTask> queue;
    private volatile boolean running = true;
    private final Thread workerThread;

    public ParallelAgent(Agent agent, int capacity) {
        this.agent = agent;
        this.queue = new ArrayBlockingQueue<>(capacity);

        workerThread = new Thread(() -> {
            while (running) {
                try {
                    MessageTask task = queue.take();
                    if (task == MessageTask.helpClose) {
                        break; // Exit the loop if close massage is appeared 
                    }
                    agent.callback(task.topic, task.message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        workerThread.start();
    }

    @Override
    public String getName() {
        return agent.getName();
    }

    @Override
    public void reset() {
        agent.reset();
    }

    @Override
    public void callback(String topic, Message msg) {
        try {
            queue.put(new MessageTask(topic, msg));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        running = false;
        try {
            queue.put(MessageTask.helpClose); // Add message to stop the worker thread
            workerThread.join(); // Wait for the worker thread to terminate
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class MessageTask {
        final String topic;
        final Message message;
        static final MessageTask helpClose = new MessageTask(null, null);

        MessageTask(String topic, Message message) {
            this.topic = topic;
            this.message = message;
        }
    }
}

