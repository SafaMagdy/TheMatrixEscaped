package code.Queue;

import code.PreNode;

//Depth First Search
public class DFQueue extends Queue{
	
	public DFQueue() {
		super();
	}

	@Override
	public void enqueue(PreNode pn) {
		super.queue.add(0, pn);
		
	}
	

}
