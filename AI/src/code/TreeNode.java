package code;
import java.util.ArrayList;

import code.Location;

public class TreeNode {
	
	//parent node?
	
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
	//array containing hostages and their locations
	//this array is created in each node using the given grid
	public ArrayList<String> hostages;
	//array containing mutant hostages and their locations
	//this array is created in each node using the given grid
	public ArrayList<String> mutantHostages;
	
	
	
	public TreeNode(ArrayList<TreeNode> prevNodes, Location myLoc, int neoD, String grid, int kills, int deaths, 
			ArrayList<String> hostages, ArrayList<String> mutantHostages) {
		
		this.prevNodes = prevNodes;
		this.myLoc = myLoc;
		this.neoDamage = neoD;
		this.grid = grid;
		this.kills = kills;
		this.deaths = deaths;
		this.hostages = hostages;
		this.mutantHostages = mutantHostages;
	}
	
	
	//method to print the content of any array
	public static void printArr (String[] arr)
	{
		for ( int i = 0 ; i < arr.length; i++) {
			System.out.print(arr[i]);
		}
		System.out.println(" ");
	}
	
	
	//goal test
	public boolean isItGoal() {
		
		boolean goal = false;
		if (this.mutantHostages.size() == 0 && this.hostages.size() == 0) {
			goal = true;
		}
		return goal;
		
	}
	

	
	public static void main(String[] args) {
		
		//System.out.println(whatInCell(2,1, "3,3;1;0,0;1,1;0,1;1,2;0,2,2,0;1,0,0,2,1,50,2,2,90"));
	}
	
	

}
