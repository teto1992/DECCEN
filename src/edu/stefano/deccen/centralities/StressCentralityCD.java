/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stefano.deccen.centralities;

import edu.stefano.deccen.utils.Couple;
import edu.stefano.deccen.messages.ReportMessage;
import peersim.cdsim.CDState;
import peersim.core.CommonState;

public class StressCentralityCD extends AbstractDeccenCD {

    public StressCentralityCD(String prefix) {
        super(prefix);
    }

    @Override
    void reportPhase(long v) {
        sigReports = 0;
        
        reportInbox.stream().forEach((ReportMessage m) -> {
            long s = m.getS();
            long t = m.getT();
            Couple sigma = new Couple(s, t);
            long distance = m.getDistance();
            
            if (!reports.contains(sigma)) { //has (s,t) been received?
                
                reports.add(new Couple(s, t)); // (s,t) received
                sigReports++;
                //update stress centrality
                if(distances.get(s)!= null && distances.get(t) != null)
                    if (s != v && t != v && (distances.get(s) + distances.get(t)) == distance) { // v != s != t
                            centrality = centrality + shortestPathsNumber.get(s) * shortestPathsNumber.get(t);
                    }
                toSendReport.add(m);

            }
 
        });
        
        System.out.println(CDState.getCycle() + "  Node " + v + " toSendReports " + toSendReport);
        System.out.println(CDState.getCycle() + "  Node " + v + " centrality " + centrality);
        System.out.println(reports.size());
        reportInbox.clear();

    }

}
