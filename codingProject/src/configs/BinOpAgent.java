package configs;

import java.util.function.BinaryOperator;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;

public class BinOpAgent implements Agent {
    private String name;
    private String inputTopic1;
    private String inputTopic2;
    private String outputTopic;
    private BinaryOperator<Double> operation;
    private Double input1;
    private Double input2;
    
    public BinOpAgent(String name, String inputTopic1, String inputTopic2, String outputTopic, BinaryOperator<Double> operation) {
        this.name = name;
        this.inputTopic1 = inputTopic1;
        this.inputTopic2 = inputTopic2;
        this.outputTopic = outputTopic;
        this.operation = operation;
        this.input1 = null;
        this.input2 = null;
        
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(inputTopic1).subscribe(this);
        tm.getTopic(inputTopic2).subscribe(this);
        tm.getTopic(outputTopic).addPublisher(this);
    }
    
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void reset() {
		this.input1 = null;
        this.input2 = null;
	}
	
	@Override
	public void callback(String topic, Message msg) {
		if (msg.asDouble != Double.NaN) {
            if (topic.equals(this.inputTopic1)) {
                this.input1 = msg.asDouble;
            } else if (topic.equals(this.inputTopic2)) {
                this.input2 = msg.asDouble;
            }

            if (this.input1 != null && this.input2 != null) {
                Double result = this.operation.apply(this.input1, this.input2);
                TopicManagerSingleton.get().getTopic(this.outputTopic).publish(new Message(result));
                this.reset();
            }
        }
	}
	
	@Override
	public void close() {
		TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(inputTopic1).unsubscribe(this);
        tm.getTopic(inputTopic2).unsubscribe(this);
        tm.getTopic(outputTopic).removePublisher(this);
    }

}
