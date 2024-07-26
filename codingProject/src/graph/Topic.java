package graph;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    public final String name;
    private final List<Agent> subs; // רשימת המאזינים
    private final List<Agent> pubs; // רשימת המפרסמים

    // Contractor (package-private)
    Topic(String name) {
        this.name = name;
        this.subs = new ArrayList<>();
        this.pubs = new ArrayList<>();
    }
    
    // Subscribe an Agent 
    public void subscribe(Agent a){
    	if (!subs.contains(a)) {
            subs.add(a);
            }
    }
    // Unsubscribe an Agent
    public void unsubscribe(Agent a){
    	subs.remove(a);
    }
    // Public a message to all subscribers
    public void publish(Message m){
    	for (Agent a : subs) {
            a.callback(name, m);
        }
    }
    // Add a publisher 
    public void addPublisher(Agent a){
    	if (!pubs.contains(a)) {
            pubs.add(a);
        }
    }
    // Remove a publisher
    public void removePublisher(Agent a){
    	pubs.remove(a);
    }
    
 // Get subscribers
    public List<Agent> getSubs() {
        return subs;
    }

    // Get publishers
    public List<Agent> getPubs() {
        return pubs;
    }


}
