/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;

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
                reports.add(new Couple(t, s));

                if (sigma.contains(v)) {
                    if (distance > centrality) {
                        centrality = distance;
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
        return 1/centrality;
    }

}
