package code;
import java.util.ArrayList;

import code.Location;

public class TreeNode {
	
	//parent node
	public TreeNode parent;
	//list for tracking the previous nodes visited to avoid repeated states
	public ArrayList<TreeNode> prevNodes;
	//current node location
	public Location myLoc;
	//track of neo's damage
	public int neoDamage;
	//the updated string according to the path that led to the current node
	public String grid;
	//keeps track of kills and deaths till now
	public int kills;
	public int deaths;
	//operator applied to get this node
	public String operator;
	//depth
	public int depth;
	//cost
	public double cost;
	//carried hostages damages
	public ArrayList<Integer> carried;
	
	public TreeNode(TreeNode parent,ArrayList<TreeNode> prevNodes, Location myLoc, int neoD, String grid, int kills, int deaths, 
			 String op, int d, double cost, ArrayList<Integer> carried) {
		
		this.parent = parent;
		this.prevNodes = prevNodes;
		this.myLoc = myLoc;
		this.neoDamage = neoD;
		this.grid = grid;
		this.kills = kills;
		this.deaths = deaths;
		this.operator = op;
		this.depth= d;
		this.cost = cost;
		this.carried = carried;
		
	}
	

}
