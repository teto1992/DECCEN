/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;
import peersim.cdsim.CDState;
import peersim.core.Network;

public class ClosenessCentralityCD extends AbstractDeccenCD {

    public ClosenessCentralityCD(String prefix) {
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
                    //closeness
                    lastUpdate = CDState.getCycle();
                    centrality += distance;
                } else {
                    toSendReport.add(m);
                }
            }
        });
        reportInbox.clear();
    }

    @Override
    public double getCentrality() {
        return centrality/(Network.size()-1);
    }

}
