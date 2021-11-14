package code.Queue;

import code.TreeNode;
import code.PreNode;
import java.util.ArrayList;

public abstract class Queue {

	public ArrayList<PreNode> queue;

	public Queue() {
		queue = new ArrayList<PreNode>();
	}

	public abstract void enqueue(PreNode pn);

	public PreNode dequeue() {
		return this.queue.remove(0);
	}

	// method to print the content of any array
	public void display() {
		for (int i = 0; i < this.queue.size(); i++) {
			System.out.println(this.queue.get(i).toString());
		}
		System.out.println(" ");
	}

}
