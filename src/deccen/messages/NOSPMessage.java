package deccen.messages;

import peersim.core.Node;

public class NOSPMessage {
	
	private Node identifier; // the source node
	private long weight; //the number of shortest paths from the source to the currently sending node
	
	public NOSPMessage() {
		weight = 0;
	}
	
	public NOSPMessage(long w) {
		this.setWeight(w);
	}
	
	public Node getIdentifier(){
		return identifier;
	}
	
	public void setIdentifier(Node id){
		identifier = id;
	}
	
	public long getWeight(){
		return weight;
	}
	
	public void setWeight(long w){
		weight = w;
	}
	
}
