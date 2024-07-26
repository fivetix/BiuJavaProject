package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

public class PlusAgent implements Agent {
    private String name;
    private String[] subs;
    private String[] pubs;
    private double x = Double.NaN;
    private double y = Double.NaN;

    public PlusAgent(String name, String[] subs, String[] pubs) {
        this.name = name;
        this.subs = subs;
        this.pubs = pubs;
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        if (subs.length > 0) tm.getTopic(subs[0]).subscribe(this);
        if (subs.length > 1) tm.getTopic(subs[1]).subscribe(this);
        if (pubs.length > 0) tm.getTopic(pubs[0]).addPublisher(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void reset() {
        this.x = Double.NaN;
        this.y = Double.NaN;
    }

    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            if (topic.equals(this.subs[0])) {
                this.x = msg.asDouble;
            } else if (topic.equals(this.subs[1])) {
                this.y = msg.asDouble;
            }

            if (!Double.isNaN(this.x) && !Double.isNaN(this.y)) {
                double result = this.x + this.y;
                if (pubs.length > 0) {
                    TopicManagerSingleton.get().getTopic(this.pubs[0]).publish(new Message(result));
                }
                this.reset();
            }
        }
    }

    @Override
    public void close() {
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        if (subs.length > 0) tm.getTopic(subs[0]).unsubscribe(this);
        if (subs.length > 1) tm.getTopic(subs[1]).unsubscribe(this);
        if (pubs.length > 0) tm.getTopic(pubs[0]).removePublisher(this);
    }
}
