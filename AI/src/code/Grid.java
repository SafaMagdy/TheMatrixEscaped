package code;
import code.Location;

public class Grid {
	
	//create an object to contain the x and y of busy cells
	//in order to give a set of random numbers sufficient to the cells create a method 
	//with loop that randomizes the numbers l7ad ama nwsl l set tnf3
	
	public static String genGrid() {
		String grid = "";
		//should be randomized
		int m = 3;
		int n = 3;
		int c = 1;
		int neoX = 0;
		int neoY = 0;
		grid = m + "," + n +";" + c + ";" + neoX + "," + neoY + ";";
		int teleX = 1;
		int teleY = 1;
		grid += teleX + "," + teleY;
		int agentNumber = 1;
		for(int i = 0; i < agentNumber; i++) {
			int agentX = 0;
			int agentY = 1;
			grid += ";" + agentX + "," + agentY;
		}
		int pillNumber = 1;
		for(int i = 0; i < pillNumber; i++) {
			int pillX = 1;
			int pillY = 2;
			grid += ";" + pillX + "," + pillY;
		}
		int padNumber = 1;
		for(int i = 0; i < padNumber; i++) {
			int spadX = 0;
			int spadY = 2;
			int fpadX = 2;
			int fpadY = 0;
			grid += ";" + spadX + "," + spadY + "," + fpadX + "," + fpadY;
		}
		int hosNumber = 3;
		for(int i = 0; i < 1; i++) {
			int hosX = 1;
			int hosY = 0;
			int hosDamage = 0;
			grid += ";" + hosX + "," + hosY + "," + hosDamage;
		}
		for(int i = 0; i < 1; i++) {
			int hosX = 2;
			int hosY = 1;
			int hosDamage = 50;
			grid += "," + hosX + "," + hosY + "," + hosDamage;;
		}
		for(int i = 0; i < 1; i++) {
			int hosX = 2;
			int hosY = 2;
			int hosDamage = 90;
			grid += "," + hosX + "," + hosY + "," + hosDamage;;
		}
		return grid;
	}
	
	

	public static void main(String[] args) {
		
		System.out.println(genGrid());
	}
}
