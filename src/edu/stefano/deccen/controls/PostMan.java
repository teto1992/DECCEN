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
	
	public PostMan(String prefix){
		pid = Configuration.getPid(prefix+"."+PAR_PROT);
	}

        @Override
	public boolean execute() {
            int emptyBoxes = 0;
		for (int i = 0; i < Network.size(); i++){
			Node n = Network.get(i);
			DeccenCD prot = (DeccenCD) n.getProtocol(pid);
			if (!prot.sendAll(n, pid)){
                            emptyBoxes++;
                        }  
		}
		
		for (int i = 0; i < Network.size(); i++){
			Node n = Network.get(i);
			DeccenCD prot = (DeccenCD) n.getProtocol(pid);
                        System.out.println(i + " stress: 1/" + ((int)(prot.getCentrality())));
		}
                
                if (emptyBoxes==Network.size() && CommonState.getIntTime() != 0){
                    System.out.println(CommonState.getTime()+"*********************OVER***********************");
                }
		
		return false;
	}
        

}
