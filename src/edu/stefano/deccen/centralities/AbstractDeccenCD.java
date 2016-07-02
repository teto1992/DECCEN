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
public abstract class AbstractDeccenCD implements CDProtocol {

    public HashMap<Long, Long> distances = new HashMap<>(); //for each source node, stores the length of the shortest path
    public HashMap<Long, Long> shortestPathsNumber = new HashMap<>(); //for each each source node, stores the number of shortest paths directed to it 

    public LinkedList<NOSPMessage> toSendNOSP = new LinkedList<>(); // mailbox for outgoing NOSP messages
    public LinkedList<ReportMessage> toSendReport = new LinkedList<>(); // mailbox for outgoing Report messages
    public LinkedList<NOSPMessage> NOSPinbox = new LinkedList<>(); // mailbox for incoming NOSP messages
    public LinkedList<ReportMessage> reportInbox = new LinkedList<>(); // mailbox for incoming Report messages

    public HashSet<Couple> reports = new HashSet<>(); //stores the couples of the already received Reports
    protected boolean first = true; // indicates if it is the first cycle
    protected boolean converged = false;

    long exchangedNOSP = 0;
    long exchangedReports = 0;
    // These variable stores the centrality whilst it is computed.
    protected double centrality = 0;

    public AbstractDeccenCD(String prefix) {
    }

    public HashMap<Long, Long> getDistances() {
        return distances;
    }

    public double getCentrality() {
        return centrality;
    }

    public long getNOSPNumber() {
        return exchangedNOSP;
    }

    public long getReportsNumber() {
        return exchangedReports;
    }

    public void reset() {
        first = true;
        distances = new HashMap<>();
        shortestPathsNumber = new HashMap<>();
        toSendNOSP = new LinkedList<>();
        NOSPinbox = new LinkedList<>();
        toSendReport = new LinkedList<>();
        reportInbox = new LinkedList<>();
        reports = new HashSet<>();
        centrality = 0;
        exchangedNOSP = 0;
        exchangedReports = 0;
        converged = false;
    }

    @Override
    public Object clone() {
        AbstractDeccenCD deccen = null;
        try {
            deccen = (AbstractDeccenCD) super.clone();
            deccen.reset();

        } catch (CloneNotSupportedException ex) {
        }

        return deccen;

    }

    private void countPhase(long v) {

        if (first) {
            NOSPMessage msg = new NOSPMessage(v, 1);
            toSendNOSP.add(msg);
            shortestPathsNumber.put(v, (long) 1);
            distances.put(v, (long) 0);
        } else {
            
            NOSPinbox.stream().map((m) -> m.getIdentifier()).forEach((s) -> {
                // check that is not a back-firing message
                if (!shortestPathsNumber.containsKey(s)) { // is it a backfiring msg?
                    long weight = 0; //summing all
                    // sum all the weights for the newly discovered node
                    for (int k = 0; k < NOSPinbox.size(); k++) {
                        NOSPMessage tmp = NOSPinbox.get(k);
                        if (tmp.getIdentifier() == s) {
                            weight += tmp.getWeight();
                        }
                    }

                    long dist = (long) CDState.getCycle();
                    // store the new entry
                    shortestPathsNumber.put(s, weight);
                    // store the current cycle that equals the distance of the shortest path
                    distances.put(s, dist);

                    // post the new weight
                    NOSPMessage msg = new NOSPMessage(s, weight);
                    toSendNOSP.add(msg);

                    //generate report message for the newly discovered node
                    ReportMessage rep = new ReportMessage(v, s, weight, dist);
                    toSendReport.add(rep);
                    reports.add(new Couple(v,s));
                }
            });
            NOSPinbox.clear();
        }
    }

    abstract void reportPhase(long v);

    @Override
    public void nextCycle(Node n, int pid) {
        long nodeId = n.getID();
            if (first){
                countPhase(nodeId);
                first = false;
            } else{
                countPhase(nodeId);
                reportPhase(nodeId);
            }
    }

    /**
     * A function to synchronously perform all the send once every cycle; it is
     * called by the PostMan Control instance.
     *
     * @param n current node
     * @param pid current protocol id
     * @return false if the post box is empty for both NOSP and Reports, true
     * otherwise.
     */
    public boolean sendAll(Node n, int pid) {
        Linkable linkable = (Linkable) n.getProtocol(FastConfig.getLinkable(pid));
        int degree = linkable.degree();

        if (toSendNOSP.isEmpty() && toSendReport.isEmpty()) {
            return false;
        }

        toSendNOSP.stream().forEach((m) -> {
            for (int i = 0; i < degree; i++) {
                Node peer = linkable.getNeighbor(i);
                AbstractDeccenCD neighbour = (AbstractDeccenCD) peer.getProtocol(pid);
                neighbour.NOSPinbox.add(m);
            }
        });

        toSendReport.stream().forEach((m) -> {
            for (int i = 0; i < degree; i++) {
                Node peer = linkable.getNeighbor(i);
                AbstractDeccenCD neighbour = (AbstractDeccenCD) peer.getProtocol(pid);
                neighbour.reportInbox.add(m);
            }
        });

        exchangedNOSP = (degree * toSendNOSP.size());
        exchangedReports = (degree * toSendReport.size());
        toSendNOSP.clear();
        toSendReport.clear();
        return true;
    }

}
