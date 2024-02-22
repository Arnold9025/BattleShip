package models;

//classe definissant la positions des objects graphiques du jeu
public class Location {

	public int x;
	public int y;

	public static final Location BAS = new Location(0, 1);
	public static final Location HAUT = new Location(0, -1);
	public static final Location GAUCHE = new Location(-1, 0);
	public static final Location DROITE = new Location(1, 0);
	public static final Location NUL = new Location(0, 0);

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Location(Location locationToCopy) {
		this.x = locationToCopy.x;
		this.y = locationToCopy.y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(Location otherPosition) {
		this.x += otherPosition.x;
		this.y += otherPosition.y;
	}

	public double distanceTo(Location otherPosition) {
		return Math.sqrt(Math.pow(x - otherPosition.x, 2) + Math.pow(y - otherPosition.y, 2));
	}

	public void multiply(int amount) {
		x *= amount;
		y *= amount;
	}

	public void subtract(Location otherPosition) {
		this.x -= otherPosition.x;
		this.y -= otherPosition.y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Location position = (Location) o;
		return x == position.x && y == position.y;
	}
	  @Override
	    public String toString() {
	        return "(" + (x+1) + ", " + (y+1) + ")";
	    }
}
