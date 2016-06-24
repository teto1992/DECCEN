package deccen.protocols;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import deccen.messages.NOSPMessage;
import deccen.messages.ReportMessage;
import peersim.cdsim.CDProtocol;
import peersim.cdsim.CDState;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import utils.Couple;

public class ClosenessCD implements CDProtocol
{

	protected Hashtable<Long,Long> distances = new Hashtable<Long,Long>();
	protected Hashtable<Long,Long> shortestPathsNumber = new Hashtable<Long,Long>();
	protected LinkedList<NOSPMessage> toSendNOSP = new LinkedList<NOSPMessage>(); // mailbox for outgoing messages
	protected LinkedList<ReportMessage> toSendReport = new LinkedList<ReportMessage>(); // mailbox for outgoing messages
	public LinkedList<NOSPMessage> NOSPinbox = new LinkedList<NOSPMessage>(); // mailbox for incoming messages
	public LinkedList<ReportMessage> reportInbox = new LinkedList<ReportMessage>(); // mailbox for incoming messages
	private HashSet<Couple> reports = new HashSet<Couple>();
	private boolean first = true;

	public long closeness;
	public long maxDistance = 0;
	public long stress = 0;
	public double betweeness = 0.0;


	public ClosenessCD(String prefix){	
	}

	public Hashtable<Long,Long> getDistances(){
		return distances;
	}

	public void init(){
		first = true;
		distances = new Hashtable<Long,Long>();
		shortestPathsNumber = new Hashtable<Long,Long>();
		toSendNOSP = new LinkedList<NOSPMessage>();
		NOSPinbox = new LinkedList<NOSPMessage>();
		toSendReport = new LinkedList<ReportMessage>();
		reportInbox = new LinkedList<ReportMessage>();
		reports = new  HashSet<Couple>();
		maxDistance = 0;
		closeness = 0;
		stress = 0;
		betweeness = 0;
	}

	public Object clone() { 
		ClosenessCD closeness = null;
		try {
			closeness = (ClosenessCD) super.clone();
			closeness.init();

		} catch ( CloneNotSupportedException ex){}

		return closeness;

	}

	private void countPhase(long nodeId){

		for (NOSPMessage m : NOSPinbox){

			long id = m.getIdentifier();
			long weight = 0; //summing all

			if (!shortestPathsNumber.containsKey(id)){ // check that is not a back-firing message

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

		}
		NOSPinbox.clear();
		System.out.println(nodeId + " " + shortestPathsNumber);

	}


	private void reportPhase(long nodeId){

		for (ReportMessage m : reportInbox){

			Couple sigma = new Couple(m.getT(), m.getS());
			long s = m.getS();
			long t = m.getT();
			long distance = m.getDistance();
			long weight = m.getSigma();
			long sp = m.getSigma();
			boolean sent = false;

			if (!reports.contains(sigma)){

				reports.add(new Couple(m.getT(),m.getS()));

				//stress
				Long vs, vt;
				if (s!=nodeId && t != nodeId)
					if ((distances.get(s) + distances.get(t)) == distance ){ // d(v,s) + d (v,t) = d(s,t)
						stress = stress + shortestPathsNumber.get(m.getS())*shortestPathsNumber.get(m.getT());
						betweeness = betweeness + ((double)(shortestPathsNumber.get(m.getS())*shortestPathsNumber.get(m.getT()))/weight);
						toSendReport.add(m);
						sent = true;
					}

				if (sigma.contains(nodeId)){
					//closeness
					closeness += m.getDistance();
					//graph
					if (m.getDistance()>maxDistance){
						maxDistance = m.getDistance();
					}

				} else {
					if (!sent)
						toSendReport.add(m);	
				}

			} 


		}
		reportInbox.clear();

	}

	public void nextCycle(Node n, int pid) {
		long nodeId = n.getID();

		if (first){

			shortestPathsNumber.put(nodeId, (long) 1);
			distances.put(nodeId, (long) 0);

			NOSPMessage nosp = new NOSPMessage(nodeId, 1);
			toSendNOSP.add(nosp);
			first = false;

		} else {


			countPhase(nodeId);
			if (!first)
				reportPhase(nodeId);

		}
	}

	/**
	 * A function to synchronously perform all the send once every cycle; it 
	 * is called by the PostMan controller.
	 * @param n current node
	 * @param pid current protocol id
	 */

	public void sendAll(Node n, int pid){
		Linkable linkable = (Linkable) n.getProtocol(FastConfig.getLinkable(pid));

		for (NOSPMessage m : toSendNOSP){
			for (int i = 0; i < linkable.degree(); i++){
				Node peer = linkable.getNeighbor(i);
				ClosenessCD neighbour = (ClosenessCD) peer.getProtocol(pid);
				neighbour.NOSPinbox.add(m);
			}

		}

		for (ReportMessage m : toSendReport){
			for (int i = 0; i < linkable.degree(); i++){
				Node peer = linkable.getNeighbor(i);
				ClosenessCD neighbour = (ClosenessCD) peer.getProtocol(pid);
				neighbour.reportInbox.add(m);
			}

		}

		toSendNOSP.clear();
		toSendReport.clear();
	}



}
