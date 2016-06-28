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

    double[] centralities;

    public PostManControl(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        centralities = new double[Network.size()];
    }

    @Override
    public boolean execute() {
        int size = Network.size();
        
        int emptyBoxes = 0;
        
        for (int i = 0; i < size; i++) {
            Node n = Network.get(i);
  
            AbstractDeccenCD prot = (AbstractDeccenCD) n.getProtocol(pid);
  
            if (!prot.sendAll(n, pid)){
                emptyBoxes++;
            }
   
        }
        
        if (emptyBoxes==size)
            System.out.println(CommonState.getTime()+" There are no more messages to send!");


        return false;
    }

}
