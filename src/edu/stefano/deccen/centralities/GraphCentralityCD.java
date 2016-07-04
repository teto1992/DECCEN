/**
 * Stefano Forti - 481183
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;
import peersim.cdsim.CDState;

public class GraphCentralityCD extends AbstractDeccenCD {

    public GraphCentralityCD(String prefix) {
        super(prefix);
    }

    @Override
    void reportPhase(long v) {
        reportInbox.stream().forEach((ReportMessage m) -> {
            long s = m.getS();
            long t = m.getT();
            Couple sigma = new Couple(t, s);
            long distance = m.getDistance();

            if (!reports.contains(sigma)) {
                reports.add(sigma);

                if (m.contains(v)) {
                    if (distance > centrality) {
                        centrality = distance;
                        lastUpdate = CDState.getCycle();
                    }
                } else {
                    toSendReport.add(m);
                }
            }
        });
        reportInbox.clear();
    }

    @Override
    public double getCentrality() {
        return (1 / centrality);
    }

}
