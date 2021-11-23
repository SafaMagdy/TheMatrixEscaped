package code.Queue;

import code.PreNode;

public class BFQueue extends Queue{
	
	public BFQueue() {
		super();
	}
	
	
	@Override
	public void enqueue(PreNode pn) {
		super.queue.add(super.queue.size(), pn);
		
		
	}

}
