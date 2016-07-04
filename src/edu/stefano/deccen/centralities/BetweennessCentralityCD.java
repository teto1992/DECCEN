/**
 * Stefano Forti - 481183
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;
import peersim.cdsim.CDState;

public class BetweennessCentralityCD extends AbstractDeccenCD {

    public BetweennessCentralityCD(String prefix) {
        super(prefix);
    }

    @Override
    void reportPhase(long v) {
        reportInbox.stream().forEach((ReportMessage m) -> {
            long s = m.getS();
            long t = m.getT();
            Couple sigma = new Couple(t, s);

            long distance = m.getDistance();
            double weight = m.getSigma();

            if (!reports.contains(sigma)) {
                reports.add(new Couple(m.getT(), m.getS()));

                if (s != v && t != v) {
                    double shortespathsVS = shortestPathsNumber.get(s);
                    double shortestpathsVT = shortestPathsNumber.get(t);
                    if ((distances.get(s) + distances.get(t)) == distance) {
                        lastUpdate = CDState.getCycle();
                        centrality = centrality
                                + ((shortespathsVS * shortestpathsVT) / weight);
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
