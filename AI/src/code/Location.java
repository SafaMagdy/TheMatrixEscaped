package code;

public class Location{
	
	public int x;
	public int y;
	
	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object locOb) {
		boolean eq = false;
		Location loc = (Location) locOb;
		if (loc.x == this.x && loc.y == this.y) {
			eq = true;
			//System.out.println(loc.x + " " + this.x);
			
			//System.out.println("here");
		}else {
			eq = false;
		}
		
		return eq;
	}

}
