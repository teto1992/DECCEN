package edu.stefano.deccen.controls;

import edu.stefano.deccen.centralities.AbstractDeccenCD;
import java.util.ArrayList;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class PostManControl implements Control {

    private static final String PAR_PROT = "deccen";
    private final int pid;
    private final ArrayList<Integer> convergedNodes;

    public PostManControl(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        convergedNodes = new ArrayList<>();
    }

    @Override
    public boolean execute() {
        int emptyBoxes = 0;
        boolean converged = true;
        
        for (int i = 0; i < Network.size(); i++) {
            Node n = Network.get(i);
            AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
            if (converged = !prot.sendAll(n, pid)) {
                emptyBoxes++;
                if (CommonState.getTime() != 1 && !convergedNodes.contains(i)){
                    System.out.println("Node " + i + " converged at cycle " + CommonState.getIntTime() + "!");
                    convergedNodes.add(i);
                }
            }
            
        }
        


        if (emptyBoxes == Network.size() && CommonState.getIntTime() != 1) {
            System.out.println("No more messages to deliver!");
        }

        return false;
    }

}
