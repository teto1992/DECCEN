package deccen.controls;

import peersim.config.Configuration;
import peersim.core.Control;

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
