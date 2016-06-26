/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deccen.controls;

import deccen.messages.NOSPMessage;
import deccen.protocols.DeccenCD;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 *
 * @author Casa
 */
public class DeccenInitializer implements Control {

    private static final String PAR_PROT = "deccen";
    private final int pid;

    public DeccenInitializer(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    @Override
    public boolean execute() {

        int size = Network.size();

        for (long i = 0; i < size; i++) {
            Node node = Network.get((int) i);
            DeccenCD deccen = (DeccenCD) node.getProtocol(pid);

            deccen.shortestPathsNumber.put(i, (long) 1);
            deccen.distances.put(i, (long) 0);

            NOSPMessage nosp = new NOSPMessage(i, 1);
            deccen.toSendNOSP.add(nosp);

        }

        for (int i = 0; i < Network.size(); i++) {
            Node n = Network.get(i);
            DeccenCD prot = (DeccenCD) n.getProtocol(pid);
            prot.sendAll(n, pid);

        }
        return false;
    }
    

}
