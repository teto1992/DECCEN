package deccen.utils;

import java.util.Hashtable;

import peersim.core.Node;

public class ShortestPathsTable {
	public Hashtable<Node,Long> table;
	
	public ShortestPathsTable(){
		table = new Hashtable<Node,Long>();
	}
}
