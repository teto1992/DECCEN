package deccen.controls;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class DeccenInitializer implements Control
{
	private static final String PAR_PROTCOUNT = "count";
	private static int pid_count;
	
	private final String PAR_PROTREP = "report";
	private final int pid_report;
	
	public DeccenInitializer (String name){
		pid_count = Configuration.getPid(name+"."+PAR_PROTCOUNT);
		pid_report = Configuration.getPid(name+"."+PAR_PROTREP);
	}
	
	public boolean execute() {
		int size = Network.size();
		
		for (int i = 0; i < size; i++){
			Node node = Network.get(i);
			
		}
		
		return false;
	}
	
}
