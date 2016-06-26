package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.NOSPMessage;
import edu.stefano.deccen.messages.ReportMessage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import peersim.cdsim.CDProtocol;
import peersim.cdsim.CDState;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;


/**
 * 
 * @author Stefano Forti
 */

public abstract class DeccenCD implements CDProtocol
{
	public HashMap<Long,Long> distances = new HashMap<>(); //for each nodeId, stores the length of the shortest path
	public HashMap<Long,Long> shortestPathsNumber = new HashMap<>(); //for each nodeId, stores the number of shortest paths directed to it 
	public LinkedList<NOSPMessage> toSendNOSP = new LinkedList<>(); // mailbox for outgoing NOSP messages
	protected LinkedList<ReportMessage> toSendReport = new LinkedList<>(); // mailbox for outgoing Report messages
	protected LinkedList<NOSPMessage> NOSPinbox = new LinkedList<>(); // mailbox for incoming NOSP messages
	protected LinkedList<ReportMessage> reportInbox = new LinkedList<>(); // mailbox for incoming Report messages
        
	protected HashSet<Couple> reports = new HashSet<>(); //stores the couples of the already received Reports
	protected boolean first = true; // indicates if it is the first cycle
        
        // These variables store the centralities whilst they are computed.
	protected double centrality = 0;


	public DeccenCD(String prefix){	
	}

	public HashMap<Long,Long> getDistances(){
		return distances;
	}

	public void reset(){
		first = true;
		distances = new HashMap<>();
		shortestPathsNumber = new HashMap<>();
		toSendNOSP = new LinkedList<>();
		NOSPinbox = new LinkedList<>();
		toSendReport = new LinkedList<>();
		reportInbox = new LinkedList<>();
		reports = new  HashSet<>();
                centrality = 0;
	}

        @Override
	public Object clone() { 
		DeccenCD deccen = null;
		try {
			deccen = (DeccenCD) super.clone();
			deccen.reset();

		} catch ( CloneNotSupportedException ex){}

		return deccen;

	}

	private void countPhase(long nodeId){

            NOSPinbox.stream().map((m) -> m.getIdentifier()).forEach((id) -> {
                
                long weight = 0; //summing all
                if (!shortestPathsNumber.containsKey(id)) {
                    // check that is not a back-firing message
                    
                    // sum all the weights for the newly discovered node
                    for (int k = 0; k < NOSPinbox.size(); k++){
                        
                        NOSPMessage tmp = NOSPinbox.get(k);
                        
                        if (tmp.getIdentifier()==id){
                            weight+=tmp.getWeight();
                        }
                    }       
                    long dist = (long) CDState.getCycle();
                    // store the new entry
                    shortestPathsNumber.put(id, weight);
                    // store the current cycle that equals the distance
                    distances.put(id, dist );
                    
                    // post the new weight
                    NOSPMessage msg = new NOSPMessage(id, weight);
                    toSendNOSP.add(msg);            
                    ReportMessage rep = new ReportMessage(nodeId, id, weight, dist);
                    toSendReport.add(rep);
                }
            });
		NOSPinbox.clear();
	}


	abstract void reportPhase(long v);



        @Override
	public void nextCycle(Node n, int pid) {
		long nodeId = n.getID();
                if (!first){
                    countPhase(nodeId);
                    reportPhase(nodeId);
                } else {
                    first = !first;
                }
	}

	/**
	 * A function to synchronously perform all the send once every cycle; it 
	 * is called by the PostMan Control instance.
	 * @param n current node
	 * @param pid current protocol id
         * @return false if the post box is empty for both NOSP and Reports, true otherwise.
	 */

	public boolean sendAll(Node n, int pid){
		Linkable linkable = (Linkable) n.getProtocol(FastConfig.getLinkable(pid));
                
                if (toSendNOSP.isEmpty() && toSendReport.isEmpty())
                    return false;

                toSendNOSP.stream().forEach((m) -> {
                    for (int i = 0; i < linkable.degree(); i++){
                        Node peer = linkable.getNeighbor(i);
                        DeccenCD neighbour = (DeccenCD) peer.getProtocol(pid);
                        neighbour.NOSPinbox.add(m);
                    }
            });

                toSendReport.stream().forEach((m) -> {
                    for (int i = 0; i < linkable.degree(); i++){
                        Node peer = linkable.getNeighbor(i);
                        DeccenCD neighbour = (DeccenCD) peer.getProtocol(pid);
                        neighbour.reportInbox.add(m);
                    }
            });

		toSendNOSP.clear();
		toSendReport.clear();
                
                return true;
	}



}
