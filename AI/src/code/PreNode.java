package code;
import code.Location;

//this object represents the possible nodes that can be created 
//this is to avoid creating all the nodes before needing to expand them
//this is to store the needed information to know when we change the path
//this is what is stored in the queue that represents the search strategy

public class PreNode implements Comparable<PreNode> {
	
	
	//the action that should be taken to create the node
	//example: "Up"/"Kill"/"Carry"
	public String action;
	//the location of the affected cell
	//this can be either the location of the next cell to move to
	//or the location of the cell containing the agent to kill
	public Location affectedCell;
	//the previous node directly before this action
	//this helps with storing the states needed to start traversing the branch
	public TreeNode prevNode;
	public double actualCost;
	public double hCost;
	
	public int depth;
	
	public PreNode(String action, Location affectedCell, TreeNode prevNode, double actualCost, double hCost ) {
		
		this.action = action;
		this.affectedCell = affectedCell;
		this.prevNode = prevNode;
		this.actualCost = actualCost;
		this.hCost = hCost;
		this.depth = prevNode.depth + 1;
	}
	
	@Override
	public String toString() {
		String result = this.action + " " + this.affectedCell.x + "  " + this.affectedCell.y+"\n--------";
		return result;
	}

	@Override
	public int compareTo(PreNode o) {
//		 zero             : If object is same as the specified object (o)
//		 positive integer : If object is greater than the specified object (o)
//		 negative integer : If object is less than the specified object (o)
		if(this.hCost>o.hCost)
			return 1;
		else if(this.hCost<o.hCost)
			return -1;
		return 0;
	}
	


}
