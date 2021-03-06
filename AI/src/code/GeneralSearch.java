package code;

import java.util.ArrayList;

import code.Location;
import code.TreeNode;
import code.Queue.*;

public abstract class GeneralSearch {

	// habdyn el cost

	// general search method==> returns the string required in the pdf
	public static String generalSearch(String grid, String strategy, boolean visualize) {
		if (!(strategy.equals("ID"))) {
			String result = "";
			// initialize everything as this is the start
			ArrayList<TreeNode> prevNodes = new ArrayList<TreeNode>();
			ArrayList<PreNode> preNodes = new ArrayList<PreNode>();
			String[] splitted = grid.split(";");
			// get Neo's starting position from the given grid
			String[] preNeo = splitted[2].split(",");
			Location neo = new Location(Integer.parseInt(preNeo[0]), Integer.parseInt(preNeo[1]));
			// array to store the damages of the carried hostages and keep track of their
			// number
			ArrayList<Integer> carried = new ArrayList<Integer>();

			// TO-DO: cost
			// create initial starting node
			TreeNode start = new TreeNode(null, prevNodes, neo, 0, grid, 0, 0, "Start", 0, 0, carried);
			PreNode startPre = new PreNode("Start", neo, start, 0, 0);

			// add to the array of previous nodes to check for repeated states
			prevNodes.add(start);

			if (visualize) {
				System.out.println("Starting the game with search strategy: " + strategy);
				System.out.println("Neo is at cell: " + neo.x + "  " + neo.y);
			}

			// get the possible actions for the starting node
			ArrayList<String> possibleActions = getPossibleActions(start);

			// try to favor carry and kill mutant
			// create the queue for search
			Queue queue;
			switch (strategy) {
			case ("DF"):
				queue = new DFQueue();
				ArrayList<String> temp = new ArrayList<String>();
				// organize possible actions accordingly
				for (int i = 0; i < possibleActions.size(); i++) {
					String pa = possibleActions.get(i);
					if (pa.contains("kill")) {
						temp.add(possibleActions.remove(i));
						i--;
					} else if (pa.contains("carry")) {
						temp.add(possibleActions.remove(i));
						i--;
					} else if (pa.contains("drop")) {
						temp.add(possibleActions.remove(i));
						i--;
					}
				}
				for (int i = 0; i < temp.size(); i++) {
					possibleActions.add(temp.get(i));
				}
				temp = new ArrayList<String>();
				break;
			case ("BF"):
				queue = new BFQueue();
				break;
			default:
				// temporary
				queue = new DFQueue();
				break;
			}

			for (int i = 0; i < possibleActions.size(); i++) {
				if (!(possibleActions.get(i).contains("kill"))) {
					String[] pa = possibleActions.get(i).split(",");
					Location affected = new Location(Integer.parseInt(pa[1]), Integer.parseInt(pa[2]));
					PreNode pn = new PreNode(pa[0], affected, start, 0, 0);
					queue.enqueue(pn);
				} else {
					// enqueue once and handle number of kills inside update
					String[] pk = possibleActions.get(i).split(";");
					String[] pa = pk[0].split(",");
					PreNode pn = new PreNode(pa[0], neo, start, 0, 0);
					queue.enqueue(pn);
				}
			}

			if (visualize) {
				System.out.println("The possible action(s) available at this cell is/are (as ordered in the queue): ");
				queue.display();
			}

			boolean failed = false;
			String finalGrid = "";

			System.out.println(prevNodes.get(0).carried.size());
			while (!queue.queue.isEmpty()) {
				if (visualize) {
					System.out.println("Removing a PreNode from the queue ");
				}

				PreNode frontPreNode = queue.dequeue();

				if (visualize) {
					System.out.println("The prenode: " + frontPreNode.action);
				}
				boolean repeated = false;
				// System.out.println(frontPreNode.action);
				TreeNode frontTreeNode = update(
						frontPreNode.action + "," + frontPreNode.affectedCell.x + "," + frontPreNode.affectedCell.y,
						frontPreNode.prevNode, prevNodes);
				// check if gameOver
				if (gameOver(frontTreeNode.neoDamage)) {
					System.out.println("Game Over at this path");
					continue;
				}

				// check if goal
				// return the requirements of solve
				if (isItGoal(frontTreeNode)) {

					result = "";
					System.out.println("Daret ya syaaaa3");
					ArrayList<String> goalPath = new ArrayList<String>();
					TreeNode p = frontTreeNode;
					while (p != null) {
						System.out.println(p.operator + " ," + p.deaths + " , " + p.carried.size() + "c, " + p.grid);
						goalPath.add(p.operator);
						p = p.parent;
					}
					for (int z = goalPath.size() - 2; z >= 0; z--) {
						if (z == 0) {
							result += goalPath.get(z);
						} else {
							result += goalPath.get(z) + ",";
						}
					}
					return result += ";" + frontTreeNode.deaths + ";" + frontTreeNode.kills + ";" + prevNodes.size();
				}

				// TO-DO: check if this is a valid check for repeated states
				for (int i = 0; i < prevNodes.size(); i++) {
					if (frontTreeNode.myLoc.x == prevNodes.get(i).myLoc.x
							&& frontTreeNode.myLoc.y == prevNodes.get(i).myLoc.y
							&& compareGrids(frontTreeNode.grid, prevNodes.get(i).grid) == true
							&& !(frontPreNode.action.contains("drop"))
							&& frontTreeNode.carried.size() == prevNodes.get(i).carried.size()) {
						// ignore this path
						repeated = true;
						break;
					}
				}
				if (repeated == true) {
					if (visualize) {
						System.out.println("Following this action will lead to a repeated state so I ignored it ");
					}
					continue;
				}
				if (visualize) {
					System.out.println(
							"Neo was at cell: " + frontPreNode.prevNode.myLoc.x + "  " + frontPreNode.prevNode.myLoc.y);
					System.out.println(" After applying the action: " + frontPreNode.action + " Neo is now at cell: "
							+ frontTreeNode.myLoc.x + "  " + frontTreeNode.myLoc.y);
					System.out.println(" Neo's damage is now: " + frontTreeNode.neoDamage);
					System.out.println(" Number of Kills: " + frontTreeNode.kills);
					System.out.println(" Number of Deaths: " + frontTreeNode.deaths);
					System.out.println(" Neo is carrying: " + frontTreeNode.carried.size() + " hostages");
					System.out.println(" The grid is now: " + frontTreeNode.grid);
				}
				prevNodes.add(frontTreeNode);
				possibleActions = getPossibleActions(frontTreeNode);
				if (strategy.equals("DF")) {
					ArrayList<String> temp = new ArrayList<String>();
					// organize possible actions accordingly
					for (int i = 0; i < possibleActions.size(); i++) {
						String pa = possibleActions.get(i);
						if (pa.contains("kill")) {
							temp.add(possibleActions.remove(i));
							i--;
						} else if (pa.contains("carry")) {
							temp.add(possibleActions.remove(i));
							i--;
						} else if (pa.contains("drop")) {
							temp.add(possibleActions.remove(i));
							i--;
						}
					}
					for (int i = 0; i < temp.size(); i++) {
						possibleActions.add(temp.get(i));
					}
					temp = new ArrayList<String>();
				}
				for (int i = 0; i < possibleActions.size(); i++) {
					if (!(possibleActions.get(i).contains("kill"))) {
						String[] pa = possibleActions.get(i).split(",");
						Location affected = new Location(Integer.parseInt(pa[1]), Integer.parseInt(pa[2]));
						PreNode pn = new PreNode(pa[0], affected, frontTreeNode, 0, 0);
						queue.enqueue(pn);
					} else {
						// enqueue once and handle number of kills inside update
						String[] pk = possibleActions.get(i).split(";");
						String[] pa = pk[0].split(",");
						PreNode pn = new PreNode(pa[0], frontTreeNode.myLoc, frontTreeNode, 0, 0);
						queue.enqueue(pn);
					}
				}
				if (queue.queue.isEmpty()) {
					return "No Solution";
				}
				finalGrid = frontTreeNode.grid;
			}
			return "No Solution";
		} else {
			String result = "";

			int i = 0;
			while (true) {
				// initialize everything as this is the start
				System.out.println("Start again");
				ArrayList<TreeNode> prevNodes = new ArrayList<TreeNode>();
				ArrayList<PreNode> preNodes = new ArrayList<PreNode>();
				String[] splitted = grid.split(";");
				// get Neo's starting position from the given grid
				String[] preNeo = splitted[2].split(",");
				Location neo = new Location(Integer.parseInt(preNeo[0]), Integer.parseInt(preNeo[1]));
				// array to store the damages of the carried hostages and keep track of their
				// number
				ArrayList<Integer> carried = new ArrayList<Integer>();

				// TO-DO: cost
				// create initial starting node
				TreeNode start = new TreeNode(null, prevNodes, neo, 0, grid, 0, 0, "Start", 0, 0, carried);
				PreNode startPre = new PreNode("Start", neo, start, 0, 0);
				// add to the array of previous nodes to check for repeated states
				prevNodes.add(start);

				if (visualize) {
					System.out.println("Starting the game with search strategy: " + strategy);
					System.out.println("Neo is at cell: " + neo.x + "  " + neo.y);
				}

				// get the possible actions for the starting node
				ArrayList<String> possibleActions = getPossibleActions(start);

				// try to favor carry and kill mutant
				// create the queue for search
				Queue queue = new DFQueue();
				boolean stop = false;
				for (int j = 0; j < possibleActions.size(); j++) {
					if (!(possibleActions.get(j).contains("kill"))) {
						String[] pa = possibleActions.get(j).split(",");
						Location affected = new Location(Integer.parseInt(pa[1]), Integer.parseInt(pa[2]));
						PreNode pn = new PreNode(pa[0], affected, start, 0, 0);
						if (pn.depth > i) {
							// System.out.println(pn.depth);
							stop = true;
							break;
						}
						queue.enqueue(pn);
					} else {
						// enqueue once and handle number of kills inside update
						String[] pk = possibleActions.get(j).split(";");
						String[] pa = pk[0].split(",");
						PreNode pn = new PreNode(pa[0], neo, start, 0, 0);
						if (pn.depth > i) {
							// System.out.println(pn.depth);
							stop = true;
							break;
						}
						queue.enqueue(pn);
					}
				}
				if (stop) {
					i++;
					continue;
				}
				if (visualize) {
					System.out.println(
							"The possible action(s) available at this cell is/are (as ordered in the queue): ");
					queue.display();
				}

				boolean failed = false;
				String finalGrid = "";
				while (!queue.queue.isEmpty()) {
					if (visualize) {
						System.out.println("Removing a PreNode from the queue ");
					}

					PreNode frontPreNode = queue.dequeue();

					if (visualize) {
						System.out.println("The prenode: " + frontPreNode.action + frontPreNode.depth);
					}
					boolean repeated = false;
					TreeNode frontTreeNode = update(
							frontPreNode.action + "," + frontPreNode.affectedCell.x + "," + frontPreNode.affectedCell.y,
							frontPreNode.prevNode, prevNodes);

					// check if gameOver
					if (gameOver(frontTreeNode.neoDamage)) {
						System.out.println("Game Over at this path");
						continue;
					}

					// check if goal
					// return the requirements of solve
					if (isItGoal(frontTreeNode)) {
						result = "";
						System.out.println("Daret ya syaaaa3");
						ArrayList<String> goalPath = new ArrayList<String>();
						TreeNode p = frontTreeNode;
						while (p != null) {
							// System.out.println(p.operator);
							goalPath.add(p.operator);
							p = p.parent;
						}
						for (int z = goalPath.size() - 2; z >= 0; z--) {
							if (z == 0) {
								result += goalPath.get(z);
							} else {
								result += goalPath.get(z) + ",";
							}
						}
						return result += ";" + frontTreeNode.deaths + ";" + frontTreeNode.kills + ";"
								+ prevNodes.size();
					}

					// TO-DO: check if this is a valid check for repeated states
					for (int j = 0; j < prevNodes.size(); j++) {
						if (frontTreeNode.myLoc.x == prevNodes.get(j).myLoc.x
								&& frontTreeNode.myLoc.y == prevNodes.get(j).myLoc.y
								&& compareGrids(frontTreeNode.grid, prevNodes.get(j).grid) == true
								&& !(frontPreNode.action.equals("drop"))
								&& frontTreeNode.carried.size() == prevNodes.get(j).carried.size()) {
							// ignore this path
							repeated = true;
							break;
						}
					}
					if (repeated == true) {
						if (visualize) {
							System.out.println("Following this action will lead to a repeated state so I ignored it ");
						}
						continue;
					}
					if (visualize) {
						System.out.println("Neo was at cell: " + frontPreNode.prevNode.myLoc.x + "  "
								+ frontPreNode.prevNode.myLoc.y);
						System.out.println(" After applying the action: " + frontPreNode.action
								+ " Neo is now at cell: " + frontTreeNode.myLoc.x + "  " + frontTreeNode.myLoc.y);
						System.out.println(" Neo's damage is now: " + frontTreeNode.neoDamage);
						System.out.println(" Number of Kills: " + frontTreeNode.kills);
						System.out.println(" Number of Deaths: " + frontTreeNode.deaths);
						System.out.println(" Neo is carrying: " + frontTreeNode.carried.size() + " hostages");
						System.out.println(" The grid is now: " + frontTreeNode.grid);
					}
					result += frontPreNode.action;
					prevNodes.add(frontTreeNode);
					possibleActions = getPossibleActions(frontTreeNode);

					for (int j = 0; j < possibleActions.size(); j++) {
						if (!(possibleActions.get(j).contains("kill"))) {
							String[] pa = possibleActions.get(j).split(",");
							Location affected = new Location(Integer.parseInt(pa[1]), Integer.parseInt(pa[2]));
							PreNode pn = new PreNode(pa[0], affected, frontTreeNode, 0, 0);
							if (pn.depth > i) {
								stop = true;
								break;
							}
							queue.enqueue(pn);
						} else {
							// enqueue once and handle number of kills inside update
							String[] pk = possibleActions.get(j).split(";");
							String[] pa = pk[0].split(",");
							PreNode pn = new PreNode(pa[0], frontTreeNode.myLoc, frontTreeNode, 0, 0);
							if (pn.depth > i) {
								stop = true;
								break;
							}
							queue.enqueue(pn);
						}
					}
					if (stop) {
						break;
					}

					if (queue.queue.isEmpty()) {
						System.out.println("failed");
						result += "No Solution";
						return "No Solution";
					}
					finalGrid = frontTreeNode.grid;
				}
				i++;
			}
		}

	}

	// returns true if neo is dead, false otherwise
	public static boolean gameOver(int neoD) {
		boolean gameOver = false;
		if (neoD >= 100) {
			gameOver = true;
		}
		return gameOver;
	}

	// compares the grids of the two nodes in comparison to help check for repeated
	// states
	// TO-DO: this might be tricky if we needed the damages as a difference
	public static boolean compareGrids(String grid1, String grid2) {
		boolean similar = false;
		// if same number of agents and hostages and pills then similar
		String[] splitted1 = grid1.split(";");
		String[] splitted2 = grid2.split(";");
		String[] agents1 = splitted1[4].split(",");
		String[] agents2 = splitted2[4].split(",");
		if (agents1.length == agents2.length) {
			// check pills
			String[] pills1 = splitted1[5].split(",");
			String[] pills2 = splitted2[5].split(",");
			if (pills1.length == pills2.length) {
				// check hostages
				String[] hos1;
				// this check is needed because if there are no more hostages in the grid
				// the last part of the grid will not appear after split
				if (splitted1.length <= 7) {
					// no more hostages
					hos1 = new String[0];
				} else {
					hos1 = splitted1[7].split(",");
				}

				String[] hos2;
				if (splitted2.length <= 7) {
					// no more hostages
					hos2 = new String[0];
				} else {
					hos2 = splitted2[7].split(",");
				}
				if (hos1.length == hos2.length) {
					similar = true;
				} else {
					similar = false;
				}
			} else {
				similar = false;
			}
		} else {
			similar = false;
		}
		return similar;
	}

	// goal test
	public static boolean isItGoal(TreeNode n) {
		boolean goal = false;
		String grid = n.grid;
		String[] splitted = grid.split(";");
		// array that consists of (x,y) of the telephone booth
		String[] telephone = splitted[3].split(",");
		Location tb = new Location(Integer.parseInt(telephone[0]), Integer.parseInt(telephone[1]));
		ArrayList<String> hostages = getHostages(grid);
		ArrayList<String> mutantHostages = getMutantHostages(grid);
		if (mutantHostages.size() == 0 && hostages.size() == 0 && n.carried.size() == 0 && n.myLoc.equals(tb)) {
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

	// returns arraylist of hostages with their damages
	// each element is in the form of "hosX,hosY,damage"
	public static ArrayList<String> getHostages(String grid) {
		ArrayList<String> result = new ArrayList<String>();
		String[] splitted = grid.split(";");
		// array that contains locations of all the hostages
		String[] hostages;
		if (splitted.length <= 7) {
			hostages = new String[0];
		} else {
			hostages = splitted[7].split(",");
		}

		for (int i = 0; i < hostages.length - 2; i += 3) {
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

	// returns arraylist of mutant hostages
	// each element is in the form of "hosX,hosY"
	public static ArrayList<String> getMutantHostages(String grid) {
		ArrayList<String> result = new ArrayList<String>();
		String[] splitted = grid.split(";");
		// array that contains locations of all the hostages
		String[] hostages;
		if (splitted.length <= 7) {
			hostages = new String[0];
		} else {
			hostages = splitted[7].split(",");
		}

		for (int i = 0; i < hostages.length - 2; i += 3) {
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
		if (splitted.length <= 7) {
			hostages = new String[0];
		} else {
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
		if (result == "") {
			result = "Empty";
		}
		return result;
	}

	// method to detect where can I move
	public static ArrayList<String> whereCanIMove(Location current, String grid) {
		ArrayList<String> result = new ArrayList<String>();
		// check all possible directions
		String[] splitted = grid.split(";");
		String[] dimensions = splitted[0].split(",");
		int rows = Integer.parseInt(dimensions[0]);
		int cols = Integer.parseInt(dimensions[1]);
		boolean up = false;
		boolean right = false;
		boolean down = false;
		boolean left = false;
		boolean kill = true;
		// check if I am in a cell with a hostage of damage 98 or 99 and if yes then do
		// not perform kill action
		String currentCellComponent = whatInCell(current.x, current.y, grid);
		if (currentCellComponent.contains("hostage")) {
			String[] h = currentCellComponent.split(";");
			if (Integer.parseInt(h[3]) < 98) {
				kill = true;
			} else {
				kill = false;
			}
		}
		// check if I can go left
		if (current.y > 0) {
			// check if there is an agent or a mutant agent or if there is a hostage check
			// the damage
			String upCellComponent = whatInCell(current.x, current.y - 1, grid);
			if (!(upCellComponent.contains("agent"))) {
				// check if there is a mutant hostage or the hostage is about to become mutant
				if (upCellComponent.contains("hostage")) {
					String[] hos = upCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 98) {
						left = true;
					}
				} else {
					left = true;
				}
			}
		}
		// check if I can go down
		if (current.x < rows - 1) {
			// check if there is an agent or a mutant agent
			String rightCellComponent = whatInCell(current.x + 1, current.y, grid);
			if (!(rightCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (rightCellComponent.contains("hostage")) {
					String[] hos = rightCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 98) {
						down = true;
					}
				} else {
					down = true;
				}
			}
		}
		// check if I can go right
		if (current.y < cols - 1) {
			// check if there is an agent or a mutant agent
			String downCellComponent = whatInCell(current.x, current.y + 1, grid);
			if (!(downCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (downCellComponent.contains("hostage")) {
					String[] hos = downCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 98) {
						right = true;
					}
				} else {
					right = true;
				}
			}
		}
		// check if I can go Up
		if (current.x > 0) {
			// check if there is an agent or a mutant agent
			String leftCellComponent = whatInCell(current.x - 1, current.y, grid);
			if (!(leftCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (leftCellComponent.contains("hostage")) {
					String[] hos = leftCellComponent.split(";");
					if (Integer.parseInt(hos[3]) < 98) {
						up = true;
					}
				} else {
					up = true;
				}
			}
		}
		if (up) {
			result.add("up");
		}
		if (right) {
			result.add("right");
		}
		if (down) {
			result.add("down");
		}
		if (left) {
			result.add("left");
		}

		return result;
	}

	public static ArrayList<String> getPossibleKills(TreeNode node) {
		ArrayList<String> result = new ArrayList<String>();
		// check all possible directions
		String[] splitted = node.grid.split(";");
		String[] dimensions = splitted[0].split(",");
		int rows = Integer.parseInt(dimensions[0]);
		int cols = Integer.parseInt(dimensions[1]);
		boolean kill = true;
		// check if I am in a cell with a hostage of damage 98 or 99 and if yes then do
		// not perform kill action
		String currentCellComponent = whatInCell(node.myLoc.x, node.myLoc.y, node.grid);
		if (currentCellComponent.contains("hostage")) {
			String[] h = currentCellComponent.split(";");
			if (Integer.parseInt(h[3]) < 98) {
				kill = true;
			} else {
				kill = false;
			}
		}
		// check if I can kill left
		if (node.myLoc.y > 0) {
			// check if there is an agent or a mutant agent or if there is a hostage check
			// the damage
			String upCellComponent = whatInCell(node.myLoc.x, node.myLoc.y - 1, node.grid);
			if (!(upCellComponent.contains("agent"))) {
				// check if there is a mutant hostage or the hostage is about to become mutant
				if (upCellComponent.contains("hostage")) {
					String[] hos = upCellComponent.split(";");
					if ((Integer.parseInt(hos[3]) >= 100) && kill) {
						result.add("KillLeft");
					}
				}
			} else if (kill) {
				result.add("KillLeft");
			}
		}
		// check if I can go down
		if (node.myLoc.x < rows - 1) {
			// check if there is an agent or a mutant agent
			String rightCellComponent = whatInCell(node.myLoc.x + 1, node.myLoc.y, node.grid);
			if (!(rightCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (rightCellComponent.contains("hostage")) {
					String[] hos = rightCellComponent.split(";");
					if ((Integer.parseInt(hos[3]) >= 100) && kill) {
						result.add("KillDown");
					}
				}
			} else if (kill) {
				result.add("KillDown");
			}
		}
		// check if I can go right
		if (node.myLoc.y < cols - 1) {
			// check if there is an agent or a mutant agent
			String downCellComponent = whatInCell(node.myLoc.x, node.myLoc.y + 1, node.grid);
			if (!(downCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (downCellComponent.contains("hostage")) {
					String[] hos = downCellComponent.split(";");
					if ((Integer.parseInt(hos[3]) >= 100) && kill) {
						result.add("KillRight");
					}
				}
			} else if (kill) {
				result.add("KillRight");
			}
		}
		// check if I can go Up
		if (node.myLoc.x > 0) {
			// check if there is an agent or a mutant agent
			String leftCellComponent = whatInCell(node.myLoc.x - 1, node.myLoc.y, node.grid);
			if (!(leftCellComponent.contains("agent"))) {
				// check if there is a mutant hostage
				if (leftCellComponent.contains("hostage")) {
					String[] hos = leftCellComponent.split(";");
					if ((Integer.parseInt(hos[3]) >= 100) && kill) {
						result.add("KillUp");
					}
				}
			} else if (kill) {
				result.add("KillUp");
			}
		}
		return result;
	}

	// method to detect possible actions and insert in queue
	// this method returns array of strings of the following format
	// "ActionName", "affectedLocation"
	public static ArrayList<String> getPossibleActions(TreeNode node) {
		ArrayList<String> result = new ArrayList<String>();
		String[] splitted = node.grid.split(";");
		int cAllowed = Integer.parseInt(splitted[1]);
		// check the current cell components
		String currentCellComponent = whatInCell(node.myLoc.x, node.myLoc.y, node.grid);
		if (currentCellComponent.contains("telephone")) {
			// check before drop if there is a hostage to drop
			if (node.carried.size() > 0) {
				// System.out.println(node.depth);
				result.add("drop," + node.myLoc.x + "," + node.myLoc.y);
			}
		}
		if (currentCellComponent.contains("hostage")) {
			String[] hos = currentCellComponent.split(";");
			// cannot be a mutant hostage because we do not add this option to happen during
			// movement
			// in the update method we check if we can actually perform the carry or not
			if (node.carried.size() + 1 <= cAllowed && Integer.parseInt(hos[3]) < 100) {
				result.add("carry," + node.myLoc.x + "," + node.myLoc.y);
			}

		}
		if (currentCellComponent.contains("pill")) {
			// either take it or leave it
			result.add("takePill," + node.myLoc.x + "," + node.myLoc.y);
		}

		if (currentCellComponent.contains("pad")) {
			// get the go-to pad from the string
			String[] padData = currentCellComponent.split(";");
			// we know that the current cell is the first pad in the string so ignore it
			int fx = Integer.parseInt(padData[3]);
			int fy = Integer.parseInt(padData[4]);
			result.add("fly," + fx + "," + fy);
		}

		// get the possible movements
		Location current = new Location(node.myLoc.x, node.myLoc.y);
		ArrayList<String> movements = whereCanIMove(current, node.grid);
		if (!(movements.isEmpty())) {
			for (int i = 0; i < movements.size(); i++) {
				if (movements.get(i) == "up") {
					result.add("up," + (current.x - 1) + "," + current.y);
				} else if (movements.get(i) == "right") {
					result.add("right," + current.x + "," + (current.y + 1));
				} else if (movements.get(i) == "down") {
					result.add("down," + (current.x + 1) + "," + current.y);
				} else if (movements.get(i) == "left") {
					result.add("left," + current.x + "," + (current.y - 1));
				}
			}
		}

		ArrayList<String> kills = getPossibleKills(node);
		String toKill = "";
		if (!(kills.isEmpty())) {
			for (int i = 0; i < kills.size(); i++) {
				if (kills.get(i) == "KillUp") {
					toKill += "kill," + (current.x - 1) + "," + current.y;
				} else if (kills.get(i) == "KillRight") {
					toKill += "kill," + current.x + "," + (current.y + 1);
				} else if (kills.get(i) == "KillDown") {
					toKill += "kill," + (current.x + 1) + "," + current.y;
				} else if (kills.get(i) == "KillLeft") {
					toKill += "kill," + current.x + "," + (current.y - 1);
				}
				if (i < kills.size() - 1) {
					toKill += ";";
				}
			}
			result.add(toKill);
		}
		return result;
	}

	// updates everything needed according to the action taken
	// returns the new node resulting from the update
	public static TreeNode update(String action, TreeNode prevNode, ArrayList<TreeNode> prevNodes) {
		// we assume that Neo starts with 0 damage
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
		ArrayList<String> hostages = getHostages(prevNode.grid);
		ArrayList<Integer> carried = new ArrayList<Integer>();

		for (int i = 0; i < prevNode.carried.size(); i++) {
			carried.add(prevNode.carried.get(i));
		}

		int deaths = prevNode.deaths;
		int kills = prevNode.kills;
		int neoD = prevNode.neoDamage;
		Location newLocation = prevNode.myLoc;
		// get the affected location from action
		String[] actionDetails = action.split(",");
		Location moveTo = new Location(Integer.parseInt(actionDetails[1]), Integer.parseInt(actionDetails[2]));

		// check the action performed and accordingly update the grid or damage
		switch (actionDetails[0]) {
		case ("kill"):
			// check if I am in a cell with a hostage of damage 98 or 99 and if yes then do
			// not perform this action
			String currentCellComponent = whatInCell(prevNode.myLoc.x, prevNode.myLoc.y, prevNode.grid);
			if (currentCellComponent.contains("hostage")) {
				// check the damage
				String[] h = currentCellComponent.split(";");
				if (Integer.parseInt(h[3]) < 98) {
					// check how many kills should be performed
					ArrayList<String> toKill = getPossibleKills(prevNode);
					neoD += 20;
					for (int w = 0; w < toKill.size(); w++) {
						// perform the kill
						// increment the number of killings by 1
						kills++;
						// increment Neo's damage by 20
						
						int kx = -1;
						int ky = -1;
						if (toKill.get(w).contains("KillUp")) {
							kx = prevNode.myLoc.x - 1;
							ky = prevNode.myLoc.y;
						} else if (toKill.get(w).contains("KillRight")) {
							kx = prevNode.myLoc.x;
							ky = prevNode.myLoc.y + 1;
						} else if (toKill.get(w).contains("KillDown")) {
							kx = prevNode.myLoc.x + 1;
							ky = prevNode.myLoc.y;
						} else if (toKill.get(w).contains("KillLeft")) {
							kx = prevNode.myLoc.x;
							ky = prevNode.myLoc.y - 1;
						}
						// update grid by removing the killed agent or mutant hostage
						for (int i = 0; i < agents.length - 1; i += 2) {
							if (kx == Integer.parseInt(agents[i]) && ky == Integer.parseInt(agents[i + 1])) {
								// replace the x and y with negatives so that when we combine the string again
								// we can know that this is not valid
								agents[i] = "-1";
								agents[i + 1] = "-1";
								break;
							}
						}
						for (int i = 0; i < mutantHostages.size(); i++) {
							String[] mHos = mutantHostages.get(i).split(",");
							if (kx == Integer.parseInt(mHos[0]) && ky == Integer.parseInt(mHos[1])) {
								// remove the mutant hostages from the arraylist
								mutantHostages.remove(i);
								break;
							}
						}
					}
				}
			} else {
				ArrayList<String> toKill = getPossibleKills(prevNode);
				neoD += 20;
				for (int w = 0; w < toKill.size(); w++) {
					// perform the kill
					// increment the number of killings by 1
					kills++;
					// increment Neo's damage by 20
					
					int kx = -1;
					int ky = -1;
					if (toKill.get(w).contains("KillUp")) {
						kx = prevNode.myLoc.x - 1;
						ky = prevNode.myLoc.y;
					} else if (toKill.get(w).contains("KillRight")) {
						kx = prevNode.myLoc.x;
						ky = prevNode.myLoc.y + 1;
					} else if (toKill.get(w).contains("KillDown")) {
						kx = prevNode.myLoc.x + 1;
						ky = prevNode.myLoc.y;
					} else if (toKill.get(w).contains("KillLeft")) {
						kx = prevNode.myLoc.x;
						ky = prevNode.myLoc.y - 1;
					}
					// update grid by removing the killed agent or mutant hostage
					for (int i = 0; i < agents.length - 1; i += 2) {
						if (kx == Integer.parseInt(agents[i]) && ky == Integer.parseInt(agents[i + 1])) {
							// replace the x and y with negatives so that when we combine the string again
							// we can know that this is not valid
							agents[i] = "-1";
							agents[i + 1] = "-1";
							break;
						}
					}
					for (int i = 0; i < mutantHostages.size(); i++) {
						String[] mHos = mutantHostages.get(i).split(",");
						if (kx == Integer.parseInt(mHos[0]) && ky == Integer.parseInt(mHos[1])) {
							// remove the mutant hostages from the arraylist
							mutantHostages.remove(i);
							break;
						}
					}
				}
			}
			break;
		case ("takePill"):
			// remove the pill
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
			// Decrement the damages of hostages and Neo by 20
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
				int oldDamage2 = Integer.parseInt(splitted2[2]);
				// check that is cannot reach below 0
				if (oldDamage2 < 20) {
					oldDamage2 = 0;
				} else {
					oldDamage2 -= 20;
				}
				// set the new damage
				hostages.set(i, splitted2[0] + "," + splitted2[1] + "," + oldDamage2);
			}
			break;
		case ("carry"):
			int cAllowed = Integer.parseInt(splitted[1]);
			// check that there is capacity
			if (carried.size() + 1 <= cAllowed) {
				for (int i = 0; i < hostages.size(); i++) {
					String[] hos = hostages.get(i).split(",");
					if (prevNode.myLoc.x == Integer.parseInt(hos[0]) && prevNode.myLoc.y == Integer.parseInt(hos[1])) {
						hostages.remove(i);
						carried.add(Integer.parseInt(hos[2]));
						break;
					}
				}
			}
			break;
		case ("drop"):
			// Reset carried
			if (carried.size() > 0) {
				carried = new ArrayList<Integer>();
			}
			break;
		case ("down"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;
			break;
		case ("up"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;
			break;
		case ("right"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;
			break;
		case ("left"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;
			break;
		case ("fly"):
			// nothing changes in the grid only the overall hostage damage increases by 2
			// and this is already handled above
			newLocation = moveTo;
			break;
		default:
			break;
		}

		// update damage of all hostages
		for (int i = 0; i < hostages.size(); i++) {
			// each entry in the arraylist is a string with commas splitting the x and y and
			// damage
			String[] splittedHos = hostages.get(i).split(",");
			// the third element is the damage
			int oldDamage = Integer.parseInt(splittedHos[2]) + 2;
			if (action == "takePill") {
				// reverse the action
				oldDamage -= 2;
			}
			// set the new damage
			hostages.set(i, splittedHos[0] + "," + splittedHos[1] + "," + oldDamage);
			// check if it reached 100
			if (oldDamage >= 100) {
				// remove this hostage from the hostages array and add it to mutant
				mutantHostages.add(hostages.get(i));
				// System.out.println(hostages.get(i));
				hostages.remove(i);
				i--;
				deaths++;
				// as we do not add a different category in the grid string to represent the
				// mutant hostages
				// we rely on having the damage of 100 or greater to reflect this change
			}
		}
		for (int i = 0; i < carried.size(); i++) {
			int d = carried.get(i);
			// System.out.println(d);
			if (action.contains("takePill")) {
				if (d < 100) {
					if (d < 20) {
						carried.set(i, 0);
					} else {
						carried.set(i, d - 20);
					}
				}
			} else if (d < 100 && !(action.contains("drop"))) {
				carried.set(i, d + 2);
				if (carried.get(i) >= 100) {
					// System.out.println(carried.get(i));
					deaths++;
				}
			}
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
		for (int i = 0; i < hostages.size(); i++) {
			String[] curHos = hostages.get(i).split(",");
			result += curHos[0] + "," + curHos[1] + "," + curHos[2];
			if (i < hostages.size()) {
				result += ",";
			}
		}
		// add the mutant hostages to the hostages category in the grid
		for (int i = 0; i < mutantHostages.size(); i++) {
			String[] curHos = mutantHostages.get(i).split(",");
			result += curHos[0] + "," + curHos[1] + "," + "100";
			if (i < mutantHostages.size() - 1) {
				result += ",";
			}
		}
		// creating the resultant node
		TreeNode resNode = new TreeNode(prevNode, prevNodes, newLocation, neoD, result, kills, deaths, actionDetails[0],
				prevNode.depth + 1, 0, carried);
		return resNode;
	}
}
