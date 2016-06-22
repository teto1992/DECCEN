package deccen.protocols;

import java.util.ArrayList;
import java.util.LinkedList;

import deccen.messages.NOSPMessage;
import deccen.utils.DistancesTable;
import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;

public class ClosenessCD implements CDProtocol, Linkable
{
	
	protected DistancesTable distancesTable = new DistancesTable();
	protected ArrayList<Node> neighbours;
	protected LinkedList<NOSPMessage> inbox;
	
	
	public ClosenessCD(String prefix){	
	}
	
	public DistancesTable getDistances(){
		return distancesTable;
	}
	
	public void onKill() {
		neighbours = null;
		distancesTable = null;
	}
	
	public Object clone() { 
		ClosenessCD closeness = null;
		try {
			closeness = (ClosenessCD) super.clone();
			closeness.neighbours = new ArrayList<Node>();
			
		} catch ( CloneNotSupportedException ex){}
		
		return closeness;
		
	}

	public boolean addNeighbor(Node n) {
		// has a Set behaviour
		if (neighbours.contains(n))
			return false;
		else {
			neighbours.add(n);
			return true;
		}
	}

	public boolean contains(Node n) {
		return neighbours.contains(n);
	}

	public int degree() {
		return neighbours.size();
	}

	public Node getNeighbor(int arg0) {
		return neighbours.get(arg0);
	}

	public void pack() {
		System.out.println("ClosenessCD.pack() -- not implemented");
	}

	public void nextCycle(Node n, int pid) {
		Linkable linkable = (Linkable) n.getProtocol(pid);
		
		if (linkable.degree()>0){
			for (int i = 0; i <  linkable.degree(); i++){
				Node peer = linkable.getNeighbor(CommonState.r.nextInt(linkable.degree()));
				ClosenessCD neighbour = (ClosenessCD) peer.getProtocol(pid);
				NOSPMessage nosp = new NOSPMessage();
				neighbour.inbox.addLast()
			}
			
		}
			

		
	}

}
