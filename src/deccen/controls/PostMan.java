package deccen.controls;

import deccen.protocols.DeccenCD;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class PostMan implements Control {
	
	private static final String PAR_PROT = "closeness";
	private final int pid;
	
	public PostMan(String prefix){
		pid = Configuration.getPid(prefix+"."+PAR_PROT);
	}

	public boolean execute() {
		for (int i = 0; i < Network.size(); i++){
			Node n = Network.get(i);
			DeccenCD prot = (DeccenCD) n.getProtocol(pid);
			prot.sendAll(n, pid);
		}
		
		for (int i = 0; i < Network.size(); i++){
			Node n = Network.get(i);
			DeccenCD prot = (DeccenCD) n.getProtocol(pid);
			System.out.println(i + " closeness " + prot.closeness + " graph " + prot.maxDistance + " stress " + prot.stress + " betweeness " + prot.betweeness);

		}
		
		return false;
	}

}
