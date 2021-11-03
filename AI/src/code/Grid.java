package code;
import java.util.ArrayList;

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
	
	
	//method to get the content of the given cell and useful information about content of cell
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
			result = "telephone;" + x + ";" + y;
			return result;
		}
		for (int i = 0 ; i < agents.length-1; i+=2) {
			if(x == Integer.parseInt(agents[i]) && y == Integer.parseInt(agents[i+1])) {
				result = "agent;" + x + ";" + y;
				return result;
			}
		}
		for (int i = 0 ; i < pills.length-1; i+=2) {
			if(x == Integer.parseInt(pills[i]) && y == Integer.parseInt(pills[i+1])) {
				result = "pill;" + x + ";" + y;
				return result;
			}
		}
		for (int i = 0 ; i < pads.length-1; i+=2) {
			if(x == Integer.parseInt(pads[i]) && y == Integer.parseInt(pads[i+1])) {
				result = "pad;" + x + ";" + y;
				if((i/2)%2==0) {
					result += ";" + pads[i+2] + ";" + pads[i+3];
				}else {
					result += ";" + pads[i-2] + ";" + pads[i-1];
				}
				//return pair of pads attached together
				return result;
			}
		}
		for (int i = 0 ; i < hostages.length-1; i+=3) {
			if(x == Integer.parseInt(hostages[i]) && y == Integer.parseInt(hostages[i+1])) {
				result = "hostage;" + x + ";" + y + ";" + hostages[i+2];
				return result;
			}
		}
		
		return result;
	}
	
	//method to fill the hostages and damage
	public static ArrayList<String> getHostages(String grid){
		ArrayList<String> result = new ArrayList<String>();
		
		String[] splitted = grid.split(";");
		//array that contains locations of all the hostages
		String[] hostages = splitted[7].split(",");

		for (int i = 0 ; i < hostages.length-1; i+=3) {
			//store the location and damage in a string
			String temp = "";
			//check the damage to know if mutant or alive
			//if mutant then skip this iteration
			if (Integer.parseInt(hostages[i+2]) < 100) {
				temp += hostages[i] + "," + hostages[i+1] + "," + hostages[i+2];
				result.add(temp);
			}
			
		}
		
		return result;
	}
	
		//method to fill the mutant hostages
		public static ArrayList<String> getMutantHostages(String grid){
			ArrayList<String> result = new ArrayList<String>();
			
			String[] splitted = grid.split(";");
			//array that contains locations of all the hostages
			String[] hostages = splitted[7].split(",");

			for (int i = 0 ; i < hostages.length-1; i+=3) {
				//store the location in a string
				String temp = "";
				//check the damage to know if mutant or alive
				//if alive then skip this iteration
				if (Integer.parseInt(hostages[i+2]) >= 100) {
					//add only the location since we do not need the damage
					temp += hostages[i] + "," + hostages[i+1];
					result.add(temp);
				}
				
			}
			
			return result;
		}
		
		
		//this method is responsible for only updating the grid itself
		//the action represents the different effects on the grid
		//this method returns an updated grid string
		public static String updateGrid(int x, int y, String oldGrid, String action) {
			String result = "";
			//split the grid at ; to extract different categories in the grid
			String[] splitted = oldGrid.split(";");
			//since the dimensions and neo's initial position and TB position are constant, append to result
			result += splitted[0] + ";" + splitted[1]+ ";" + splitted[2] + ";" + splitted[3] + ";";
			
			//array that contains locations of all the agents
			String[] agents = splitted[4].split(",");
			//array that contains the locations of all mutant hostages
			ArrayList<String> mutantHostages = getMutantHostages(oldGrid);
			//array that contains locations of all the pills
			String[] pills = splitted[5].split(",");
			//array that contains locations of all the pads
			//this array is always divisible by 4 since the pad comes in pairs
			//(startx, starty, finishx, finishy)
			String pads = splitted[6];
			//array that contains locations of all the hostages
			String[] hostages = splitted[7].split(",");

			//we need cases
			switch(action) {
			case("Kill"): //Neo killed the agent/mutantHostage at x and y
						  //remove this agent/mutantHostage from the grid

						for (int i = 0; i < agents.length-1; i+=2) {
							if(x == Integer.parseInt(agents[i]) && y == Integer.parseInt(agents[i+1])) {
								//replace the x and y with negatives so that when we combine the string again
								//we can know that this is not valid
								agents[i] = "-1";
								agents[i+1] = "-1";
								break;
							}		  
						}
						for (int i = 0; i < mutantHostages.size()-1; i+=2) {
							if(x == Integer.parseInt(mutantHostages.get(i)) && y == Integer.parseInt(mutantHostages.get(i+1))) {
								//remove the mutant hostages from the arraylist
								mutantHostages.remove(i+1);
								mutantHostages.remove(i);
								break;
							}		  
						}
						break;
			case("Pill"): //Neo took the pill at x and y
				  		 //remove this pill from the grid
						
						for (int i = 0; i < pills.length-1; i+=2) {
							if(x == Integer.parseInt(pills[i]) && y == Integer.parseInt(pills[i+1])) {
								//replace the x and y with negatives so that when we combine the string again
								//we can know that this is not valid
								pills[i] = "-1";
								pills[i+1] = "-1";
								break;
							}		  
						}
						break;
			case("Carry"): //Neo took the hostage at x and y
				  		 //remove this hostage from the grid
						
						for (int i = 0; i < hostages.length-1; i+=3) {
							if(x == Integer.parseInt(hostages[i]) && y == Integer.parseInt(hostages[i+1])) {
								//replace the x and y with negatives so that when we combine the string again
								//we can know that this is not valid
								hostages[i] = "-1";
								hostages[i+1] = "-1";
								break;
							}		  
						}
						break;
			default: break;
			}
			
			//then we need to form the new grid
			
			for (int i = 0; i < agents.length-1; i+=2) {
				if(agents[i] == "-1") {
					//killed 
					continue;
				}else{
					
					result += agents[i] + "," + agents[i+1];
					if (i < agents.length-3) {
						result += ",";
					}
				}
			}
			
			for (int i = 0; i < pills.length-1; i+=2) {
				if(pills[i] == "-1") {
					//taken 
					continue;
				}else{
					
					result += pills[i] + "," + pills[i+1];
					if (i < pills.length-3) {
						result += ",";
					}
				}
			}
			
			result += ";" + pads + ";";
			
			for (int i = 0; i < hostages.length-1; i+=3) {
				if(hostages[i] == "-1") {
					//carried 
					continue;
				}else{
					result += hostages[i] + "," + hostages[i+1] + "," + hostages[i+2];
					if (i < agents.length-4) {
						result += ",";
					}
				}
			}
			
			//add the mutant hostages to the hostages category in the grid 
			for (int i = 0; i < mutantHostages.size()-1; i+=2) {
				result += mutantHostages.get(i) + "," + mutantHostages.get(i+1) + "," + "100";
					if (i < mutantHostages.size()-3) {
						result += ",";
				}
			}
			
			return result;
		}
		
	//reminder: Safa's opinion: if Neo carried a hostage we should remove this hostage from its position
	//because we will increment the c and there is no need to monitor the damage of the hostage
	//because if he died then nothing will change 
	//However, if we kept it in the hostages array we will need to add a condition while monitoring the damage
	//that if the damage reached 100 while Neo is carrying him do not turn in mutant
	//and I believe this is complex for no good
		
	
	//updates everything needed according to the action taken
		//I think it should return the new grid string
	public static void update(String action, Location loc, String grid) {
		//we assume that Neo starts with 0 damage
		
		//update damage of all hostages
		ArrayList<String> hostages = getHostages(grid);
		ArrayList<String> mutantHostages = getMutantHostages(grid);
		
		for (int i = 0 ; i < hostages.size(); i++) {
			//each entry in the arraylist is a string with commas splitting the x and y and damage
			String[] splitted = hostages.get(i).split(",");
			//the third element is the damage
			splitted[2] += 2;
			//check if it reached 100
			if (Integer.parseInt(splitted[2]) >= 100) {
				//remove this hostage from the hostages array and add it to mutant
				mutantHostages.add(hostages.get(i));
				hostages.remove(i);
				//update the grid by adding the new hostage damage 
				//as we do not add a different category in the grid string to represent the mutant hostages
				//we rely on having the damage of 100 or greater to reflect this change
				
				//decrement the i by 1 in order not to miss any hostages
				i--;
			}
		}
		
		//check the action performed and accordingly update the grid or damage
		switch (action) {
		case("Move"): //nothing changes in the grid 
					  break;
		case("Kill"):
					//update grid by removing the killed agent or mutant hostage(method above)
					//increment Neo's damage by 20
					//increment the number of killings by 1 if it is an agent
					break;
		case("Pill"):
					//update grid method above
					//Decrement the damages of hostages and Neo by 20
					break;
		case("Carry"):
					//update grid method above
					//increment c by 1
					break;
		case("Drop"):
					//Decrement c by 1
					break;
		default: break;
		}	
		
		
		
	}
	
	

	public static void main(String[] args) {
		
		System.out.println(genGrid());
	}
}
