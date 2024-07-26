package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

public class IncAgent implements Agent {
    private String name;
    private String[] subs;
    private String[] pubs;

    public IncAgent(String name, String[] subs, String[] pubs) {
        this.name = name;
        this.subs = subs;
        this.pubs = pubs;
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        if (subs.length > 0) tm.getTopic(subs[0]).subscribe(this);
        if (pubs.length > 0) tm.getTopic(pubs[0]).addPublisher(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void reset() {
        // אין צורך לאתחול מיוחד
    }

    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            double result = msg.asDouble + 1;
            if (pubs.length > 0) {
                TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(result));
            }
        }
    }

    @Override
    public void close() {
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        if (subs.length > 0) tm.getTopic(subs[0]).unsubscribe(this);
        if (pubs.length > 0) tm.getTopic(pubs[0]).removePublisher(this);
    }
}
