package code;
import java.util.Random;
import code.Location;
import java.util.ArrayList;


public class Matrix {
	
	//create a class for action methods?
	//use array list instead of queue
	
	public static String genGrid() {
		String grid = "";
		ArrayList<Location> busyLocations = new ArrayList<Location>();
		
		Random rand = new Random();
		int m = rand.nextInt((15 - 5) + 1) + 5;
		int n = rand.nextInt((15 - 5) + 1) + 5;
		int c = rand.nextInt((4 - 1) + 1) + 1;
		int neoX = rand.nextInt((15 - 5) + 1) + 5;
		int neoY = rand.nextInt((15 - 5) + 1) + 5;
		Location neo = new Location(neoX, neoY);
		busyLocations.add(neo);
		grid = m + "," + n + ";" + c + ";" + neoX + "," + neoY + ";";
		boolean busy = true;
		int teleX = -1;
		int teleY = -1;
		while(busy == true) {
			//call the is busy method to know if the cell is busy and keep generating random variables till false
			teleX = rand.nextInt((15 - 5) + 1) + 5;
			teleY = rand.nextInt((15 - 5) + 1) + 5;
			Location tele = new Location(teleX, teleY);
			if (isBusy(busyLocations, tele)) {
				continue;
			}else {
				busyLocations.add(tele);
				busy = false;
				break;
			}
		}
		//reset busy to be used later
		busy = true;
		grid += teleX + "," + teleY;
		System.out.println(m + " " + n + " " + c);
		System.out.println(neoX + " " + neoY);
		System.out.println(teleX + " " + teleY);
		// should be randomized
		
		/*
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
		*/
		return grid;
	}
	
	
	//this method check whether or not the cell is empty in order to avoid overlap in cells
	public static boolean isBusy(ArrayList<Location> busyLocations, Location loc) {
		boolean busy = false;
		for (int i = 0; i < busyLocations.size(); i++) {
			if (busyLocations.get(i).x == loc.x && busyLocations.get(i).y == loc.y) {
				busy = true;
				break;
			}
		}
		return busy;
		
	}
	
	public static void main(String[] args) {
		genGrid();
	}

	
	
	//gen grid
	//solve
	//update
	

}
