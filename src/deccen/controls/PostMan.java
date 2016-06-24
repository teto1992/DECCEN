package deccen.controls;

import deccen.protocols.ClosenessCD;
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
			ClosenessCD prot = (ClosenessCD) n.getProtocol(pid);
			prot.sendAll(n, pid);
		}
		
		for (int i = 0; i < Network.size(); i++){
			Node n = Network.get(i);
			ClosenessCD prot = (ClosenessCD) n.getProtocol(pid);
			System.out.println(i + " closeness " + prot.closeness+ " max dist " + prot.maxDistance + " stress " + prot.stress + " betweeness " + prot.betweeness);

		}
		
		return false;
	}

}
