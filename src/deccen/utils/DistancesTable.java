package deccen.utils;

import java.util.Hashtable;

import peersim.core.Node;

public class DistancesTable {
	public Hashtable<Node,Long> table;
	
	public DistancesTable(){
		table = new Hashtable<Node,Long>();
	}
}
