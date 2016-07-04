/**
 * Stefano Forti - 481183
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;
import peersim.cdsim.CDState;

public class StressCentralityCD extends AbstractDeccenCD {

    public StressCentralityCD(String prefix) {
        super(prefix);
    }

    @Override
    void reportPhase(long v) {
        reportInbox.stream().forEach((ReportMessage m) -> {
            long s = m.getS();
            long t = m.getT();
            Couple sigma = new Couple(t, s);
            long distance = m.getDistance();

            if (!reports.contains(sigma)) { //has (s,t) been received?
                reports.add(new Couple(t, s)); // (s,t) received
                //update stress centrality
                if (distances.get(s) != null && distances.get(t) != null) {
                    if (s != v && t != v
                            && (distances.get(s) + distances.get(t)) == distance) {
                        centrality = centrality
                                + shortestPathsNumber.get(s)
                                * shortestPathsNumber.get(t);
                        lastUpdate = CDState.getCycle();
                    }
                }
                toSendReport.add(m);
            }
        });
        reportInbox.clear();

    }

    @Override
    public double getCentrality() {
        return centrality;
    }

}
