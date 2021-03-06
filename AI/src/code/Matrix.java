package code;

import java.util.Random;
import code.Location;
import code.TreeNode;
import code.Queue.*;

import java.util.ArrayList;

public class Matrix extends GeneralSearch{
	
	public Matrix () {
		super();
	}
	
	/*
	public static String genGrid() {
		String grid = "";
		ArrayList<Location> busyLocations = new ArrayList<Location>();

		Random rand = new Random();
		int m = rand.nextInt((15 - 5) + 1) + 5;
		int n = rand.nextInt((15 - 5) + 1) + 5;
		System.out.println(m);
		System.out.println(n);
		// calculate the total number of cells as a check for later
		int totalCells = m * n;
		int c = rand.nextInt((4 - 1) + 1) + 1;
		int neoX = rand.nextInt((n - 5) + 1) + 5;
		int neoY = rand.nextInt((m - 5) + 1) + 5;
		Location neo = new Location(neoX, neoY);
		busyLocations.add(neo);
		grid = m + "," + n + ";" + c + ";" + neoX + "," + neoY + ";";
		int teleX = -1;
		int teleY = -1;
		// random variables till false
		teleX = rand.nextInt((n - 5) + 1) + 5;
		teleY = rand.nextInt((m - 5) + 1) + 5;
		Location tele = new Location(teleX, teleY);
		while (isBusy(busyLocations, tele)) {
			teleX = rand.nextInt((n - 5) + 1) + 5;
			//System.out.println(teleX);
			teleY = rand.nextInt((m - 5) + 1) + 5;
			tele.x = teleX;
			tele.y = teleY;
			//tele = new Location(teleX, teleY);
		}
		System.out.println("out of loop 1");
		// if it comes out of the loop then it is not busy
		busyLocations.add(tele);
		grid += teleX + "," + teleY + ";";

		int hostagesNum = 0;
		// check to avoid overflow of hostages
		// generate number of hostages first since there is a restriction on it
		hostagesNum = rand.nextInt((10 - 3) + 1) + 3;
		
		while (hostagesNum > (totalCells - busyLocations.size())) {
			// try again
			hostagesNum = rand.nextInt((10 - 3) + 1) + 3;
		}
		System.out.println("hostages: " + hostagesNum);
		// the number of hostages is suitable
		// assign them to cells and add them to the grid string
		// since agents come first create a new string and append it to the grid string
		String hostages = "";
		for (int i = 0; i < hostagesNum; i++) {
			int hosX = rand.nextInt((n - 5) + 1) + 5;
			int hosY = rand.nextInt((m - 5) + 1) + 5;
			int damage = rand.nextInt((99 - 1) + 1) + 1;
			Location hos = new Location(hosX, hosY);
			// check if this location is valid
			while (isBusy(busyLocations, hos)) {
				// try again
				hosX = rand.nextInt((n - 5) + 1) + 5;
				hosY = rand.nextInt((m - 5) + 1) + 5;
				damage = rand.nextInt((99 - 1) + 1) + 1;
				//hos = new Location(hosX, hosY);
				hos.x = hosX;
				hos.y = hosY;
				System.out.println("maznoo2: " + hos.x + "   " + hos.y);
			}
			busyLocations.add(hos);
			System.out.println("out of loop 2: " + i);
			// append to string
			hostages += hosX + "," + hosY + "," + damage;
			// to add the comma after each hostage

			if (i < hostagesNum - 1) {
				hostages += ",";
			}

		}
		hostages += ";";

		// check if full
		if (busyLocations.size() < totalCells) {
			// generate agents
			int agentsNum = 0;
			// to avoid overflow of agents put the max as the number of available cells
			agentsNum = rand.nextInt((totalCells - busyLocations.size()) + 1);
			System.out.println("agents: " + agentsNum);
			// assign them to cells and add them to the grid string
			for (int i = 0; i < agentsNum; i++) {
				int ageX = rand.nextInt((n - 5) + 1) + 5;
				int ageY = rand.nextInt((m - 5) + 1) + 5;
				Location agent = new Location(ageX, ageY);
				// check if this location is valid
				// System.out.println(busyLocations.size());
				// System.out.println(totalCells);
				while (isBusy(busyLocations, agent)) {
					// try again
					ageX = rand.nextInt((n - 5) + 1) + 5;
					ageY = rand.nextInt((m - 5) + 1) + 5;
					//agent = new Location(ageX, ageY);
					agent.x = ageX;
					agent.y = ageY;
					System.out.println("e7tmal at7shr " + ageX + "   " + ageY);

				}
				busyLocations.add(agent);
				System.out.println("out of loop 3: " + i);
				// append to string
				grid += ageX + "," + ageY;
				// to add the comma after each agent
				if (i < agentsNum - 1) {
					grid += ",";
				}
			}
			System.out.println("out of loop 4");
			
			grid += ';';
		}
		// check if full
		//System.out.println(isBusy(busyLocations, tele));
		if (busyLocations.size() < totalCells) {
			// generate the pills
			int pillsNum = 0;
			// check to avoid overflow of pills
			// the max number of pills is the number of hostages
			pillsNum = rand.nextInt(hostagesNum + 1);
			while (pillsNum > (totalCells - busyLocations.size())) {
				// try again
				pillsNum = rand.nextInt(hostagesNum + 1);
			}
			System.out.println("pills: " + pillsNum);
			// assign them to cells and add them to the grid string
			for (int i = 0; i < pillsNum; i++) {
				int pillX = rand.nextInt((n - 5) + 1) + 5;
				int pillY = rand.nextInt((m - 5) + 1) + 5;
				Location pill = new Location(pillX, pillY);
				// check if this location is valid
				while (isBusy(busyLocations, pill)) {
					// try again
					pillX = rand.nextInt((n - 5) + 1) + 5;
					pillY = rand.nextInt((m - 5) + 1) + 5;
					//pill = new Location(pillX, pillY);
					pill.x = pillX;
					pill.y = pillY;
					System.out.println("e7tmal at7shr2");

				}
				busyLocations.add(pill);
				System.out.println("out of loop 5");
				// append to string
				grid += pillX + "," + pillY;
				// to add the comma after each pill
				if (i < pillsNum - 1) {
					grid += ",";
				}
				System.out.println("out of loop 6: " + i);
			}
			

			grid += ";";
		}

		// check if full
		// subtract 1 because the pads come in pairs
		if (busyLocations.size() < totalCells - 1) {
			// generate the pads
			int padsNum = 0;
			// check to avoid overflow of pills
			padsNum = rand.nextInt((totalCells - busyLocations.size()) + 1);
			// check if the number is divisible by 2
			while (padsNum > (totalCells - busyLocations.size()) || padsNum % 2 != 0) {
				// try again
				rand.nextInt((totalCells - busyLocations.size()) + 1);
				
			}
			System.out.println("pads: " + padsNum);
			// assign them to cells and add them to the grid string
			for (int i = 0; i < padsNum; i++) {
				int padX = rand.nextInt((n - 5) + 1) + 5;
				int padY = rand.nextInt((m - 5) + 1) + 5;
				Location pad = new Location(padX, padY);
				// check if this location is valid
				while (isBusy(busyLocations, pad)) {
					// try again
					padX = rand.nextInt((n - 5) + 1) + 5;
					padY = rand.nextInt((m - 5) + 1) + 5;
					pad.x = padX;
					pad.y = padY;
					//pad = new Location(padX, padY);
					System.out.println("e7tmal at7shr3");

				}
				busyLocations.add(pad);
				System.out.println("out of loop 7: " + i);
				// append to string
				grid += padX + "," + padY;
				// to add the comma after each pad
				if (i < padsNum - 1) {
					grid += ",";
				}
			}

			grid += ";";
			System.out.println("out of loop 8");
		}
		
		System.out.println("e7tmal at7shr4");
		grid += hostages;

		System.out.println(grid);
		return grid;
	}
	*/
	
	
	//temporary method just for testing
	public static String genGrid() {
		String grid = "";
		int m = 3;
		int n = 3;
		int c = 1;
		int neoX = 0;
		int neoY = 0;
		grid = m + "," + n + ";" + c + ";" + neoX + "," + neoY + ";";
		int teleX = 1;
		int teleY = 1;
		grid += teleX + "," + teleY;
		int agentNumber = 1;
		for (int i = 0; i < agentNumber; i++) {
			int agentX = 0;
			int agentY = 1;
			grid += ";" + agentX + "," + agentY;
		}
		int pillNumber = 1;
		for (int i = 0; i < pillNumber; i++) {
			int pillX = 1;
			int pillY = 2;
			grid += ";" + pillX + "," + pillY;
		}
		int padNumber = 1;
		for (int i = 0; i < padNumber; i++) {
			int spadX = 0;
			int spadY = 2;
			int fpadX = 2;
			int fpadY = 0;
			grid += ";" + spadX + "," + spadY + "," + fpadX + "," + fpadY;
		}
		int hosNumber = 3;
		for (int i = 0; i < 1; i++) {
			int hosX = 1;
			int hosY = 0;
			int hosDamage = 0;
			grid += ";" + hosX + "," + hosY + "," + hosDamage;
		}
		for (int i = 0; i < 1; i++) {
			int hosX = 2;
			int hosY = 1;
			int hosDamage = 50;
			grid += "," + hosX + "," + hosY + "," + hosDamage;
			;
		}
		for (int i = 0; i < 1; i++) {
			int hosX = 2;
			int hosY = 2;
			int hosDamage = 90;
			grid += "," + hosX + "," + hosY + "," + hosDamage;
			;
		}
		return grid;
	}


	// this method check whether or not the cell is empty in order to avoid overlap
	// in cells
	public static boolean isBusy(ArrayList<Location> busyLocations, Location loc) {
		boolean busy = false;
		if (busyLocations.contains(loc)) {
			busy = true;
		}
		return busy;

	}
	
	
	public static String solve(String grid, String strategy, boolean visualize) {
		String result = "";
		result = generalSearch(grid, strategy, visualize);
		return result;
	}
	
	//method to print the content of any array
	public static void printArr(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			System.out.println(arr.get(i));
		}
		System.out.println(" ");
	}

	public static void main(String[] args) {

		//String grid = genGrid();
		String grid0 = "5,5;2;4,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,3,3,3,3,4,4;4,0,17,1,2,54,0,0,46,4,1,22";
		//String grid3 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,1";
		//String grid1 = "5,5;1;1,4;1,0;0,4;0,0,2,2;3,4,4,2,4,2,3,4;0,2,32,0,1,38";
		String grid = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
		System.out.println(grid);
		//System.out.println(solve(grid0, "ID", true));
		System.out.println(solve(grid, "BF", false));
	}

}
