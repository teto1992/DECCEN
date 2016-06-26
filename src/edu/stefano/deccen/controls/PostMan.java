package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.DeccenCD;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class PostMan implements Control {

    private static final String PAR_PROT = "deccen";
    private final int pid;
    private boolean print = false;

    public PostMan(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    @Override
    public boolean execute() {
        int emptyBoxes = 0;
        for (int i = 0; i < Network.size(); i++) {
            Node n = Network.get(i);
            DeccenCD prot = (DeccenCD) n.getProtocol(pid);
            if (!prot.sendAll(n, pid)) {
                emptyBoxes++;
            }
        }
        
        if (emptyBoxes == Network.size() && CommonState.getIntTime() != 1 && !print) {
            print = true;
            System.out.println(CommonState.getTime() + "*********************OVER***********************");
            for (int i = 0; i < Network.size(); i++) {
                Node n = Network.get(i);
                DeccenCD prot = (DeccenCD) n.getProtocol(pid);
                System.out.println(i + " stress: 1/" + ((int) (prot.getCentrality())));
            }
        }

        return false;
    }

}
