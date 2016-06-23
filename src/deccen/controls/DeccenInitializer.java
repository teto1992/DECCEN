package deccen.controls;

import deccen.messages.NOSPMessage;
import deccen.protocols.CountCD;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class DeccenInitializer implements Control
{
	private static final String PAR_PROTCOUNT = "count";
	private static int pid;
	
	public DeccenInitializer (String name){
		pid = Configuration.getPid(name+"."+PAR_PROTCOUNT);
	}
	
	public boolean execute() {

		
		return false;
	}
	
}
