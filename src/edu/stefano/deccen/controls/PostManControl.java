package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.AbstractDeccenCD;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class PostManControl implements Control {

    private static final String PAR_PROT = "deccen";
    private final int pid;

    public PostManControl(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    @Override
    public boolean execute() {
        int emptyBoxes = 0;
        for (int i = 0; i < Network.size(); i++) {
            Node n = Network.get(i);
            AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
            if (!prot.sendAll(n, pid)) {
                emptyBoxes++;
            }
        }

        if (emptyBoxes == Network.size() && CommonState.getIntTime() != 1) {
            System.out.println("No more messages to deliver!");
        }

        return false;
    }

}
