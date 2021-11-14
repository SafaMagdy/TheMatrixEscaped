package code;

import java.util.ArrayList;

import code.Location;
import code.TreeNode;
import code.Queue.*;

public abstract class GeneralSearch {

	// habdyn el cost wl depth
	// check game over

	public static String generalSearch(String grid, String strategy) {
		String result = "";
		//initialize everything as this is the start
		ArrayList<TreeNode> prevNodes = new ArrayList<TreeNode>();
		ArrayList<PreNode> preNodes = new ArrayList<PreNode>();
		String[] splitted = grid.split(";");
		//get Neo's starting position
		String[] preNeo = splitted[2].split(",");
		Location neo = new Location(Integer.parseInt(preNeo[0]), Integer.parseInt(preNeo[1]));
		//array to store the damages of the carried hostages
		//also used to keep track of the number of carried hostages
		ArrayList<Integer> carried = new ArrayList<Integer>();
		
		// create initial starting node
		// reminder: cost
		TreeNode start = new TreeNode(null, prevNodes, neo, 0, grid, 0, 0, "Start", 0, 0, carried);
		PreNode startPre = new PreNode("Start", neo, start, 0);

		prevNodes.add(start);

		// create the queue for search
		Queue queue;
		switch (strategy) {
		case ("DF"):
			queue = new DFQueue();
			break;
		default:
			//temporary
			queue = new DFQueue();
			break;
		}

		// get the possible actions for the starting node
		ArrayList<String> possibleActions = getPossibleActions(neo, grid);
		printArr(possibleActions);
		for (int i = 0; i < possibleActions.size(); i++) {
			String[] pa = possibleActions.get(i).split(",");
			Location affected = new Location(Integer.parseInt(pa[1]), Integer.parseInt(pa[2]));
			PreNode pn = new PreNode(pa[0], affected, start, 0);
			queue.enqueue(pn);
		}

		queue.display();

		boolean failed = false;
		String finalGrid = "";

		while (!queue.queue.isEmpty()) {
			System.out.println("habd2 ml awl");
			PreNode frontPreNode = queue.dequeue();
			boolean repeated = false;
			/*
			for (int i = 0 ; i < prevNodes.size(); i++) {
				if(frontPreNode.prevNode.myLoc.x == prevNodes.get(i).myLoc.x && 
						frontPreNode.prevNode.myLoc.y == prevNodes.get(i).myLoc.y) {
					if (!(queue.queue.isEmpty())) {
					frontPreNode = queue.dequeue();
					}
				}
			}
			*/
			TreeNode frontTreeNode = update(
					frontPreNode.action + "," + frontPreNode.affectedCell.x + "," + frontPreNode.affectedCell.y,
					frontPreNode.prevNode, prevNodes);
			for (int i = 0 ; i < prevNodes.size(); i++) {
				if(frontTreeNode.myLoc.x == prevNodes.get(i).myLoc.x && 
						frontTreeNode.myLoc.y == prevNodes.get(i).myLoc.y &&
						compareGrids(frontTreeNode.grid, prevNodes.get(i).grid) == true) {
					//ignore this path
					repeated = true;
					break;
					//if (!(queue.queue.isEmpty())) {
					//frontPreNode = queue.dequeue();
					//}
				}
			}
			if (repeated == true) {
				System.out.println("msh hakml");
				continue;
			}
			System.out.println("tamam");
			prevNodes.add(frontTreeNode);
			possibleActions = getPossibleActions(frontTreeNode.myLoc, frontTreeNode.grid);
			
			for (int i = 0; i < possibleActions.size(); i++) {
				String[] pa = possibleActions.get(i).split(",");
				//System.out.println(possibleActions.get(i));
				Location affected = new Location(Integer.parseInt(pa[1]), Integer.parseInt(pa[2]));
				PreNode pn = new PreNode(pa[0], affected, frontTreeNode, 0);
				queue.enqueue(pn);
				//queue.display();
			}
			boolean goal = isItGoal(frontTreeNode);
			System.out.println(frontPreNode.action);
			queue.display();
			if (queue.queue.isEmpty()) {
				failed = true;
			}
			
			if (frontTreeNode.neoDamage >= 100) {
				return "fail";
			}
			
			finalGrid = frontTreeNode.grid;

		}
		return finalGrid;
	}
	
	//this might be tricky if we needed the damages as a difference
	public static boolean compareGrids(String grid1, String grid2) {
		boolean similar = false;
		//if same number of agents and hostages and pills then similar
		String[] splitted1 = grid1.split(";");
		String[] splitted2 = grid2.split(";");
		String[] agents1 = splitted1[4].split(",");
		String[] agents2 = splitted2[4].split(",");
		if (agents1.length == agents2.length) {
			//check pills
			String[] pills1 = splitted1[5].split(",");
			String[] pills2 = splitted2[5].split(",");
			if (pills1.length == pills2.length) {
				//check hostages
				String[] hos1;
				if (splitted1.length <= 7 ) {
					//no more hostages
					hos1 = new String[0];
				}else {
					hos1 = splitted1[7].split(",");
				}
				
				String[] hos2;
				if (splitted2.length <= 7 ) {
					//no more hostages
					hos2 = new String[0];
				}else {
					hos2 = splitted2[7].split(",");
				}
				if (hos1.length ==hos2.length) {
					similar = true;
				}else {
					similar = false;
				}
			}else {
				similar = false;
			}
		}else {
			similar = false;
		}
		return similar;
	}

	// goal test
	public static boolean isItGoal(TreeNode n) {

		boolean goal = false;
		String grid = n.grid;
		ArrayList<String> hostages = getHostages(grid);
		ArrayList<String> mutantHostages = getMutantHostages(grid);
		if (mutantHostages.size() == 0 && hostages.size() == 0) {
			goal = true;
		}
		return goal;

	}

	// method to print the content of any array
	public static void printArr(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			System.out.println(arr.get(i));
		}
		System.out.println(" ");
	}

	// method to fill the hostages and damage
	public static ArrayList<String> getHostages(String grid) {
		ArrayList<String> result = new ArrayList<String>();
		String[] splitted = grid.split(";");
		// System.out.println(splitted.length);
		// array that contains locations of all the hostages
		String[] hostages;
		if (splitted.length <= 7) {
			hostages = new String[0];
		}else {
			hostages = splitted[7].split(",");
		}

		for (int i = 0; i < hostages.length - 1; i += 3) {
			// store the location and damage in a string
			String temp = "";
			// check the damage to know if mutant or alive
			// if mutant then skip this iteration
			if (Integer.parseInt(hostages[i + 2]) < 100) {
				temp += hostages[i] + "," + hostages[i + 1] + "," + hostages[i + 2];
				result.add(temp);
			}

		}

		return result;
	}

	// method to fill the mutant hostages
	public static ArrayList<String> getMutantHostages(String grid) {
		ArrayList<String> result = new ArrayList<String>();

		String[] splitted = grid.split(";");
		// array that contains locations of all the hostages
		String[] hostages;
		if (splitted.length <= 7) {
			hostages = new String[0];
		}else {
			hostages = splitted[7].split(",");
		}

		for (int i = 0; i < hostages.length - 1; i += 3) {
			// store the location in a string
			String temp = "";
			// check the damage to know if mutant or alive
			// if alive then skip this iteration
			if (Integer.parseInt(hostages[i + 2]) >= 100) {
				// add only the location since we do not need the damage
				temp += hostages[i] + "," + hostages[i + 1];
				result.add(temp);
			}

		}

		return result;
	}

	// method to get the content of the given cell and useful information about
	// content of cell
	// can be used to determine the surroundings of the current cell
	public static String whatInCell(int x, int y, String grid) {
		String result = "";
		// split the grid at ; to extract different categories in the grid
		String[] splitted = grid.split(";");
		// array that consists of (x,y) of the telephone booth
		String[] telephone = splitted[3].split(",");
		// array that contains locations of all the agents
		String[] agents = splitted[4].split(",");
		// array that contains locations of all pills
		String[] pills = splitted[5].split(",");
		// array that contains locations of all the pads
		// this array is always divisible by 4 since the pad comes in pairs
		// (startx, starty, finishx, finishy)
		String[] pads = splitted[6].split(",");
		// array that contains locations of all the hostages
		String[] hostages;
		if (splitted.length <=7) {
			hostages = new String[0];
		}else {
			hostages = splitted[7].split(",");
		}

		if (x == Integer.parseInt(telephone[0]) && y == Integer.parseInt(telephone[1])) {
			result = "telephone;" + x + ";" + y;
			return result;
		}
		for (int i = 0; i < agents.length - 1; i += 2) {
			if (x == Integer.parseInt(agents[i]) && y == Integer.parseInt(agents[i + 1])) {
				result = "agent;" + x + ";" + y;
				return result;
			}
		}
		for (int i = 0; i < pills.length - 1; i += 2) {
			if (x == Integer.parseInt(pills[i]) && y == Integer.parseInt(pills[i + 1])) {
				result = "pill;" + x + ";" + y;
				return result;
			}
		}
		for (int i = 0; i < pads.length - 1; i += 2) {
			if (x == Integer.parseInt(pads[i]) && y == Integer.parseInt(pads[i + 1])) {
				result = "pad;" + x + ";" + y;
				if ((i / 2) % 2 == 0) {
					result += ";" + pads[i + 2] + ";" + pads[i + 3];
				} else {
					result += ";" + pads[i - 2] + ";" + pads[i - 1];
				}
				// return pair of pads attached together
				return result;
			}
		}
		for (int i = 0; i < hostages.length - 1; i += 3) {
			if (x == Integer.parseInt(hostages[i]) && y == Integer.parseInt(hostages[i + 1])) {
				result = "hostage;" + x + ";" + y + ";" + hostages[i + 2];
				return result;
			}
		}

		return result;
	}

	// method to detect where can I move
	public static ArrayList<String> whereCanIMove(Location current, String grid) {
		ArrayList<String> result = new ArrayList<String>();
		// check all possible directions
		String[] splitted = grid.split(";");
		String[] dimensions = splitted[0].split(",");
		int cols = Integer.parseInt(dimensions[1]);
		int rows = Integer.parseInt(dimensions[0]);
		boolean up = false;
		boolean right = false;
		boolean down = false;
		boolean left = false;
		// check if I can go left
		if (current.y > 0) {
			// check if there is an agent or a mutant agent
			String upCellComponent = whatInCell(current.x, current.y - 1, grid);
			if (!(upCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (upCellComponent.contains("hostage")) {
					// check the damage to know if it is mutant
					String[] hos = upCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 100) {
						left = true;
					} else {
						result.add("KillLeft");
					}
				}
				left = true;
			} else {
				result.add("KillLeft");
			}
		}
		// check if I can go down
		if (current.x < rows - 1) {
			// check if there is an agent or a mutant agent
			String rightCellComponent = whatInCell(current.x + 1, current.y, grid);
			if (!(rightCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (rightCellComponent.contains("hostage")) {
					// System.out.println(rightCellComponent);
					// check the damage to know if it is mutant
					String[] hos = rightCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 100) {
						down = true;
					} else {
						result.add("KillDown");
					}
				}
				down = true;
			} else {
				result.add("KillDown");
			}
		}
		// check if I can go right
		if (current.y < cols - 1) {
			// check if there is an agent or a mutant agent
			String downCellComponent = whatInCell(current.x, current.y + 1, grid);
			if (!(downCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (downCellComponent.contains("hostage")) {
					// check the damage to know if it is mutant
					String[] hos = downCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 100) {
						right = true;
					} else {
						result.add("KillRight");
					}
				}
				right = true;
			} else {
				result.add("KillRight");
			}
		}
		// check if I can go Up
		if (current.x > 0) {
			// check if there is an agent or a mutant agent
			String leftCellComponent = whatInCell(current.x - 1, current.y, grid);
			if (!(leftCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (leftCellComponent.contains("hostage")) {
					// check the damage to know if it is mutant
					String[] hos = leftCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 100) {
						up = true;
					} else {
						result.add("KillUp");
					}
				}
				up = true;
			} else {
				result.add("KillUp");
			}
		}
		if (up) {
			result.add("Up");
		}
		if (right) {
			result.add("Right");
		}
		if (down) {
			result.add("Down");
		}
		if (left) {
			result.add("Left");
		}

		return result;
	}

	// method to detect possible actions and insert in queue
	// this method returns array of strings of the following format
	// "ActionName", "affectedLocation"
	public static ArrayList<String> getPossibleActions(Location current, String grid) {
		ArrayList<String> result = new ArrayList<String>();
		// check the current cell components
		String currentCellComponent = whatInCell(current.x, current.y, grid);
		if (currentCellComponent.contains("pill")) {
			// either take it or leave it
			result.add("Pill," + current.x + "," + current.y);
		}
		if (currentCellComponent.contains("hostage")) {
			// cannot be a mutant hostage because we do not add this option to happen during
			// movement
			result.add("Carry," + current.x + "," + current.y);
		}
		if (currentCellComponent.contains("pad")) {
			// get the go-to pad from the string
			String[] pillData = currentCellComponent.split(";");
			// we know that the current cell is the first pad in the string so ignore it
			int fx = Integer.parseInt(pillData[3]);
			int fy = Integer.parseInt(pillData[4]);
			result.add("Fly," + fx + "," + fy);
		}
		if (currentCellComponent.contains("telephone")) {
			// you can either drop or leave
			// check before drop if there is a hostage to drop
			result.add("Drop," + current.x + "," + current.y);
		}

		// get the possible movements
		ArrayList<String> movements = whereCanIMove(current, grid);
		if (!(movements.isEmpty())) {
			for (int i = 0; i < movements.size(); i++) {
				if (movements.get(i) == "Up") {
					result.add("Up," + (current.x - 1) + "," + current.y);
				} else if (movements.get(i) == "Right") {
					result.add("Right," + current.x + "," + (current.y + 1));
				} else if (movements.get(i) == "Down") {
					result.add("Down," + (current.x + 1) + "," + current.y);
				} else if (movements.get(i) == "Left") {
					result.add("Left," + current.x + "," + (current.y - 1));
				} else if (movements.get(i) == "KillUp") {
					result.add("Kill," + (current.x - 1) + "," + current.y);
				} else if (movements.get(i) == "KillRight") {
					result.add("Kill," + current.x + "," + (current.y + 1));
				} else if (movements.get(i) == "KillDown") {
					result.add("Kill," + (current.x + 1) + "," + current.y);
				} else if (movements.get(i) == "KillLeft") {
					result.add("Kill," + current.x + "," + (current.y - 1));
				}

			}
		}
		return result;

	}

	// reminder: make the location of the carried hostage should be that of neo
	// reminder: adjust damage abl maymot law pill
	// reminder e3mly el ba2y
	// 5aly update ta5od el node

	// updates everything needed according to the action taken
	// I think it should return the new grid string
	public static TreeNode update(String action, TreeNode prevNode, ArrayList<TreeNode> prevNodes) {
		// we assume that Neo starts with 0 damage
		// update damage of all hostages

		// resultant grid
		String result = "";
		// split the grid at ; to extract different categories in the grid
		String[] splitted = prevNode.grid.split(";");
		// since the dimensions and neo's initial position and TB position are constant,
		// append to result
		result += splitted[0] + ";" + splitted[1] + ";" + splitted[2] + ";" + splitted[3] + ";";

		// array that contains locations of all the agents
		String[] agents = splitted[4].split(",");
		// array that contains the locations of all mutant hostages
		ArrayList<String> mutantHostages = getMutantHostages(prevNode.grid);
		// array that contains locations of all the pills
		String[] pills = splitted[5].split(",");
		// array that contains locations of all the pads
		// this array is always divisible by 4 since the pad comes in pairs
		// (startx, starty, finishx, finishy)
		String pads = splitted[6];
		// array that contains locations of all the hostages
		// get this array from the method getHostages to avoid getting mutant hostages
		// from the grid string
		ArrayList<String> hostages = getHostages(prevNode.grid);
		ArrayList<Integer> carried = prevNode.carried;
		int deaths = prevNode.deaths;

		for (int i = 0; i < hostages.size(); i++) {
			// each entry in the arraylist is a string with commas splitting the x and y and
			// damage
			String[] splittedHos = hostages.get(i).split(",");
			// the third element is the damage
			int oldDamage = Integer.parseInt(splittedHos[2]) + 2;
			if (action == "Pill") {
				oldDamage -= 2;
			}
			// set the new damage
			hostages.set(i, splittedHos[0] + "," + splittedHos[1] + "," + oldDamage);
			// check if it reached 100
			if (oldDamage >= 100) {
				// remove this hostage from the hostages array and add it to mutant
				mutantHostages.add(hostages.get(i));
				hostages.remove(i);
				// update the grid by adding the new hostage damage
				// as we do not add a different category in the grid string to represent the
				// mutant hostages
				// we rely on having the damage of 100 or greater to reflect this change
			}
		}
		for (int i = 0; i < carried.size(); i++) {
			int d = carried.get(i);
			if (action == "Pill") {
				if (d < 100) {
					if (d < 20) {
						carried.set(i, 0);
					} else {
						carried.set(i, d - 20);
					}

				}
			} else if (d < 100) {
				carried.set(i, d + 2);
				if (carried.get(i) >= 100) {
					deaths++;
				}
			}

		}
		int kills = prevNode.kills;
		int neoD = prevNode.neoDamage;
		Location newLocation = prevNode.myLoc;

		// get the affected location from action
		String[] actionDetails = action.split(",");
		Location moveTo = new Location(Integer.parseInt(actionDetails[1]), Integer.parseInt(actionDetails[2]));

		// check the action performed and accordingly update the grid or damage
		switch (actionDetails[0]) {
		case ("Kill"):
			System.out.println("hamoot" + (prevNode.myLoc.y == Integer.parseInt(agents[1])));
			// update grid by removing the killed agent or mutant hostage
			// increment Neo's damage by 20
			kills++;
			neoD += 20;
			// increment the number of killings by 1 if it is an agent
			for (int i = 0; i < agents.length - 1; i += 2) {
				if (Integer.parseInt(actionDetails[1]) == Integer.parseInt(agents[i])
						&& Integer.parseInt(actionDetails[2]) == Integer.parseInt(agents[i + 1])) {
					
					// replace the x and y with negatives so that when we combine the string again
					// we can know that this is not valid
					
					agents[i] = "-1";
					agents[i + 1] = "-1";
					break;
				}
			}
			for (int i = 0; i < mutantHostages.size() - 1; i += 2) {
				if (prevNode.myLoc.x == Integer.parseInt(mutantHostages.get(i))
						&& prevNode.myLoc.y == Integer.parseInt(mutantHostages.get(i + 1))) {
					// remove the mutant hostages from the arraylist
					mutantHostages.remove(i + 1);
					mutantHostages.remove(i);
					// also remove the hostage from the grid string
					break;
				}
			}

			break;
		case ("Pill"):
			// update grid method above
			// Decrement the damages of hostages and Neo by 20
			for (int i = 0; i < pills.length - 1; i += 2) {
				if (prevNode.myLoc.x == Integer.parseInt(pills[i])
						&& prevNode.myLoc.y == Integer.parseInt(pills[i + 1])) {
					// replace the x and y with negatives so that when we combine the string again
					// we can know that this is not valid
					pills[i] = "-1";
					pills[i + 1] = "-1";
					break;
				}
			}

			if (neoD < 20) {
				neoD = 0;
			} else {
				neoD -= 20;
			}

			for (int i = 0; i < hostages.size(); i++) {
				// each entry in the arraylist is a string with commas splitting the x and y and
				// damage
				String[] splitted2 = hostages.get(i).split(",");
				// the third element is the damage
				// decrement by 22 because we already incremented by 2 at the beginning of the
				// method
				int oldDamage2 = Integer.parseInt(splitted2[2]);
				if (oldDamage2 < 20) {
					oldDamage2 = 0;
				} else {
					oldDamage2 -= 20;
				}
				// set the new damage
				if (oldDamage2 < 0) {
					hostages.set(i, splitted2[0] + "," + splitted2[1] + "," + oldDamage2);
					// check that is cannot reach below 0
				}
			}
			// Decrement Neo damage by 20
			break;
		case ("Carry"):
			// update grid method above
			int cAllowed = Integer.parseInt(splitted[1]);
			if (carried.size() + 1 < cAllowed) {
				// increment c by 1
				for (int i = 0; i < hostages.size() - 1; i += 3) {
					if (prevNode.myLoc.x == Integer.parseInt(hostages.get(i))
							&& prevNode.myLoc.y == Integer.parseInt(hostages.get(i + 1))) {
						// replace the x and y with negatives so that when we combine the string again
						// we can know that this is not valid

						// set the location to that of neo
						hostages.remove(i + 1);
						hostages.remove(i);
						carried.add(Integer.parseInt(hostages.remove(i + 2)));
						break;
					}
				}
			}

			break;
		case ("Drop"):
			// Reset carried
			prevNode.carried = new ArrayList<Integer>();
			carried = new ArrayList<Integer>();
			break;
		case ("Down"):
			System.out.println("here2");
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;

			break;
		case ("Up"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;

			break;
		case ("Right"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;

			break;
		case ("Left"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;

			break;
		case ("Fly"):
			// change hostages carried location
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;
			break;
		default:
			break;
		}

		// then we need to form the new grid

		for (int i = 0; i < agents.length - 1; i += 2) {
			if (agents[i] == "-1") {
				// killed
				continue;
			} else {
				result += agents[i] + "," + agents[i + 1];
				if (i < agents.length - 3) {
					result += ",";
				}
			}
		}
		result += ";";

		for (int i = 0; i < pills.length - 1; i += 2) {
			if (pills[i] == "-1") {
				// taken
				continue;
			} else {

				result += pills[i] + "," + pills[i + 1];
				if (i < pills.length - 3) {
					result += ",";
				}
			}
		}

		result += ";" + pads + ";";

		for (int i = 0; i < hostages.size() - 2; i += 3) {
			result += hostages.get(i) + "," + hostages.get(i + 1) + "," + hostages.get(i + 2);
			if (i < hostages.size() - 4) {
				result += ",";
			}
		}

		// add the mutant hostages to the hostages category in the grid
		for (int i = 0; i < mutantHostages.size() - 1; i += 2) {
			result += mutantHostages.get(i) + "," + mutantHostages.get(i + 1) + "," + "100";
			if (i < mutantHostages.size() - 3) {
				result += ",";
			}
		}

		TreeNode resNode = new TreeNode(prevNode, prevNodes, newLocation, neoD, result, kills, deaths, actionDetails[0],
				prevNode.depth + 1, 0, carried);
		System.out.println(result + "griiiid");
		return resNode;

	}

	// raz3yn el depth wl costs
}
