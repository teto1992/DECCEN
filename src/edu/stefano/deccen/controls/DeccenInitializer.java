/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stefano.deccen.controls;

import edu.stefano.deccen.messages.NOSPMessage;
import edu.stefano.deccen.centralities.AbstractDeccenCD;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 * This class initialises the system at time t0.
 * @author stefano
 */
public class DeccenInitializer implements Control {

    private static final String PAR_PROT = "deccen";
    private final int pid;

    public DeccenInitializer(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }
    
    /**
     * It sets up the NOSP table and the distances table for each node v
     * with (v, 1) and (v, 0) respectively. It posts the message (v,1) to
     * be sent later by the PostMan Control.
     * @return false
     */
    @Override
    public boolean execute() {
        
        int size = Network.size();
        
        for (long i = 0; i < size; i++) {
            Node node = Network.get((int) i);
            AbstractDeccenCD deccen = (AbstractDeccenCD) node.getProtocol(pid);

            deccen.shortestPathsNumber.put(i, (long) 1);
            deccen.distances.put(i, (long) 0);

            NOSPMessage nosp = new NOSPMessage(i, 1);
            deccen.toSendNOSP.add(nosp);

        }
        
        return false;
    }
    

}
