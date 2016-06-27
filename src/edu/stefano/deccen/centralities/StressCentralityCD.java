/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;

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

            if (!reports.contains(sigma)) { //has (s,t) been registered?
                reports.add(new Couple(t, s));
                if (s != v && t != v) {
                    if ((distances.get(s) + distances.get(t)) == distance) { // d(v,s) + d (v,t) = d(s,t)
                        //stress
                        centrality = centrality + shortestPathsNumber.get(s) * shortestPathsNumber.get(t);
                    }
                    toSendReport.add(m);
                }

            }
        });

        reportInbox.clear();

    }

}
