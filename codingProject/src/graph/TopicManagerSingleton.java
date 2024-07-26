package graph;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

public class TopicManagerSingleton {

    public static class TopicManager{
    	private static final TopicManager instance = new TopicManager();
    	private final ConcurrentHashMap<String, Topic> topics = new ConcurrentHashMap<>();
    	
    	// Private constructor 
    	private TopicManager() {
            //
        }
    	
    	// Gets topic if exists, else creates it in the map and return it. 
    	public Topic getTopic(String name) {
            return topics.computeIfAbsent(name, Topic::new);
        }
    	
    	// Return collection of all topics 
    	public Collection<Topic> getTopics() {
            return topics.values();
        }
    	
    	// Clear all topics
    	public void clear() {
            topics.clear();
        }
    }
    
    public static TopicManager get() {
        return TopicManager.instance;
    }

}
