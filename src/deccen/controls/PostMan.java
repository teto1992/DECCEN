package deccen.controls;

import deccen.protocols.CountCD;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class PostMan implements Control {
	
	private static final String PAR_PROT = "count";
	private final int pid;
	
	public PostMan(String prefix){
		pid = Configuration.getPid(prefix+"."+PAR_PROT);
	}

	public boolean execute() {
		for (int i = 0; i < Network.size(); i++){
			Node n = Network.get(i);
			CountCD prot = (CountCD) n.getProtocol(pid);
			prot.sendAll(n, pid);
		}
		
		return false;
	}

}
