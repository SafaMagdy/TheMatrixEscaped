package code;
import java.util.ArrayList;

import code.Location;

public class TreeNode {
	
	//list for tracking the previous cells visited to avoid repeated states
	public ArrayList<Location> prevCells;
	//current node location
	public Location myLoc;
	//number of current alive hostages
	public int hostagesNum;
	//number of mutant hostages that must be killed
	public int mutantHostagesNum;
	//track of neo's health/damage
	public int neoHealth;
	//the updated string according to the path that led to the current node
	public String grid;
	
	public TreeNode(ArrayList<Location> prevCells, Location myLoc, int hNum, int mHNum, int neoH, String grid) {
		
		this.prevCells = prevCells;
		this.myLoc = myLoc;
		this.hostagesNum = hNum;
		this.mutantHostagesNum = mHNum;
		this.neoHealth = neoH;
		this.grid = grid;		
		
	}
	
	//method to print the content of any array
	public static void printArr (String[] arr)
	{
		for ( int i = 0 ; i < arr.length; i++) {
			System.out.print(arr[i]);
		}
		System.out.println(" ");
	}
	
	// we need to add the locations of mutant hostages in the grid
	// or implement a method that checks the damage of all hostages
	
	
	//method to get the content of the given cell
	//can be used to determine the surroundings of the current cell 
	public static String whatInCell(int x, int y, String grid) {
		String result = "";
		//split the grid at ; to extract different categories in the grid
		String[] splitted = grid.split(";");
		//array that consists of (x,y) of the telephone booth
		String[] telephone = splitted[3].split(",");
		//array that contains locations of all the agents
		String[] agents = splitted[4].split(",");
		//array that contains locations of all pills
		String[] pills = splitted[5].split(",");
		//array that contains locations of all the pads
		//this array is always divisible by 4 since the pad comes in pairs
		//(startx, starty, finishx, finishy)
		String[] pads = splitted[6].split(",");
		//array that contains locations of all the hostages
		String[] hostages = splitted[7].split(",");
		
		if(x == Integer.parseInt(telephone[0]) && y == Integer.parseInt(telephone[1])) {
			result = "telephone";
			return result;
		}
		for (int i = 0 ; i < agents.length-1; i+=2) {
			if(x == Integer.parseInt(agents[i]) && y == Integer.parseInt(agents[i+1])) {
				result = "agent";
				return result;
			}
		}
		for (int i = 0 ; i < pills.length-1; i+=2) {
			if(x == Integer.parseInt(pills[i]) && y == Integer.parseInt(pills[i+1])) {
				result = "pill";
				return result;
			}
		}
		for (int i = 0 ; i < pads.length-1; i+=2) {
			if(x == Integer.parseInt(pads[i]) && y == Integer.parseInt(pads[i+1])) {
				result = "pad";
				return result;
			}
		}
		for (int i = 0 ; i < hostages.length-1; i+=3) {
			System.out.println(hostages[i]);
			if(x == Integer.parseInt(hostages[i]) && y == Integer.parseInt(hostages[i+1])) {
				result = "hostage";
				return result;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		
		System.out.println(whatInCell(1,2, "3,3;1;0,0;1,1;0,1;1,2;0,2,2,0;1,0,0,2,1,50,2,2,90"));
	}
	
	

}
