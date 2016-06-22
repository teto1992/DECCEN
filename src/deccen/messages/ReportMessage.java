package deccen.messages;

import peersim.core.Node;

public class ReportMessage {
	private Node t; // the current node
	private Node s; // the source node
	private long sigma; // the distance between s and t
	
	public Node getT(){
		return t;
	}
	
	public void setT(Node n){
		t = n;
	}
	
	public Node getS(){
		return s;
	}
	
	public void setS(Node n){
		t = n;
	}
	
	public long getSigma(){
		return sigma;
	}
	
	public void setSigma(long d){
		sigma = d;
	}
	
}
