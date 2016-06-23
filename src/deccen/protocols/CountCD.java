package deccen.protocols;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;

import deccen.messages.NOSPMessage;

import peersim.cdsim.CDProtocol;
import peersim.cdsim.CDState;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;

public class CountCD implements CDProtocol
{

	protected Hashtable<Long,Long> distances = new Hashtable<Long,Long>();
	protected Hashtable<Long,Long> shortestPathsNumber = new Hashtable<Long,Long>();
	protected LinkedList<NOSPMessage> toSend = new LinkedList<NOSPMessage>(); // mailbox for incoming messages
	public LinkedList<NOSPMessage> inbox = new LinkedList<NOSPMessage>(); // mailbox for incoming messages
	private boolean first = true;


	public CountCD(String prefix){	
		first = true;
		distances = new Hashtable<Long,Long>();
		shortestPathsNumber = new Hashtable<Long,Long>();
		toSend = new LinkedList<NOSPMessage>();
		inbox = new LinkedList<NOSPMessage>();
	}

	public Hashtable<Long,Long> getDistances(){
		return distances;
	}

	public void init(){
		first = true;
		distances = new Hashtable<Long,Long>();
		shortestPathsNumber = new Hashtable<Long,Long>();
		toSend = new LinkedList<NOSPMessage>();
		inbox = new LinkedList<NOSPMessage>();
	}

	public Object clone() { 
		CountCD closeness = null;
		try {
			closeness = (CountCD) super.clone();
			closeness.init();

		} catch ( CloneNotSupportedException ex){}

		return closeness;

	}


	public void nextCycle(Node n, int pid) {
		long nodeId = n.getID();

		if (first){
			shortestPathsNumber.put(nodeId, (long) 1);
			distances.put(nodeId, (long) 0);
			
			NOSPMessage nosp = new NOSPMessage(nodeId, 1);
			toSend.add(nosp);
			first = false;

		} else {

			for (NOSPMessage m : inbox){

				long id = m.getIdentifier();
				long weight = 0;

				if (!shortestPathsNumber.containsKey(id)){ // check that is not a back-firing message

					// sum all the weights for the newly discovered node
					for (int k = 0; k < inbox.size(); k++){

						NOSPMessage tmp = inbox.get(k);

						if (tmp.getIdentifier()==id){
							weight+=tmp.getWeight();
						}

					}

					// store the new entry
					shortestPathsNumber.put(id, weight);
					// store the current cycle that equals the distance
					distances.put(id, (long) CDState.getCycle());

				    NOSPMessage msg = new NOSPMessage(id, weight);
				    toSend.add(msg);

				}

			}
			inbox.clear();
			
			//System.out.println("dist "+nodeId + " " +distances);
			System.out.println("sp "+nodeId + " " +shortestPathsNumber);

		}

	}

	public void sendAll(Node n, int pid){
		Linkable linkable = (Linkable) n.getProtocol(FastConfig.getLinkable(pid));

		for (NOSPMessage m : toSend){
			for (int i = 0; i < linkable.degree(); i++){
				Node peer = linkable.getNeighbor(i);
				CountCD neighbour = (CountCD) peer.getProtocol(pid);
				neighbour.inbox.add(m);
			}

		}

		toSend.clear();
	}



}
