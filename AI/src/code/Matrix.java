package code;

import java.util.Random;
import code.Location;

import java.util.ArrayList;

public class Matrix {

	// create a class for action methods?
	// use array list instead of queue

	public static String genGrid() {
		String grid = "";
		ArrayList<Location> busyLocations = new ArrayList<Location>();

		Random rand = new Random();
		int m = rand.nextInt((15 - 5) + 1) + 5;
		int n = rand.nextInt((15 - 5) + 1) + 5;
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
			teleY = rand.nextInt((m - 5) + 1) + 5;
			tele = new Location(teleX, teleY);
		}
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
				hos = new Location(hosX, hosY);
			}
			busyLocations.add(hos);
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
					agent = new Location(ageX, ageY);

				}
				busyLocations.add(agent);
				// append to string
				grid += ageX + "," + ageY;
				// to add the comma after each agent
				if (i < agentsNum - 1) {
					grid += ",";
				}
			}
			grid += ';';
		}

		// check if full
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
					pill = new Location(pillX, pillY);

				}
				busyLocations.add(pill);
				// append to string
				grid += pillX + "," + pillY;
				// to add the comma after each pill
				if (i < pillsNum - 1) {
					grid += ",";
				}
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
					pad = new Location(padX, padY);

				}
				busyLocations.add(pad);
				// append to string
				grid += padX + "," + padY;
				// to add the comma after each pad
				if (i < padsNum - 1) {
					grid += ",";
				}
			}

			grid += ";";
		}
		
		grid += hostages;

		System.out.println(grid);
		return grid;
	}

	// this method check whether or not the cell is empty in order to avoid overlap
	// in cells
	public static boolean isBusy(ArrayList<Location> busyLocations, Location loc) {
		boolean busy = false;
		if (busyLocations.contains(loc)) {
			busy = true;
		}
		//System.out.println(busy);
		/*
		 * for (int i = 0; i < busyLocations.size(); i++) { if (busyLocations.get(i).x
		 * == loc.x && busyLocations.get(i).y == loc.y) { busy = true; break; } }
		 */
		return busy;

	}

	public static void main(String[] args) {
		// System.out.println(test.contains(loc3));

		genGrid();
	}

	// gen grid
	// solve
	// update

}
