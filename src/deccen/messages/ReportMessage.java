package deccen.messages;


public class ReportMessage {
	private long t; // the current node
	private long s; // the source node
	private long sigma; // the number of shortest paths between s and t
	private long distance; // the distance between s and t
	
	public ReportMessage(long t , long s, long sigma, long distance){
		this.distance = distance;
		this.s = s;
		this.t = t;
		this.sigma = sigma;
				
	}
	
	public long getT(){
		return t;
	}
	
	public void setT(long n){
		t = n;
	}
	
	public long getS(){
		return s;
	}
	
	public void setS(long n){
		t = n;
	}
	
	public long getSigma(){
		return sigma;
	}
	
	public void setSigma(long d){
		sigma = d;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}
	
	public String toString(){
		return ("("+t+", "+s+") o = " + sigma + " dist = " + distance);
	}
	
}
