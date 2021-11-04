package code;
import code.Location;

//this object represents the possible nodes that can be created 
//this is to avoid creating all the nodes before needing to expand them
//this is to store the needed information to know when we change the path
//this is what is stored in the queue that represents the search strategy

public class PreNode {
	
	//the action that should be taken to create the node
	public String action;
	//the location of the affected cell
	//this can be either the location of the next cell to move to
	//or the location of the cell containing the agent to kill
	public Location affectedCell;
	//the previous node directly before this action
	//this helps with storing the states needed to start traversing the branch
	public TreeNode prevNode;
	
	public PreNode(String action, Location affectedCell, TreeNode prevNode ) {
		
		this.action = action;
		this.affectedCell = affectedCell;
		this.prevNode = prevNode;
	}

}