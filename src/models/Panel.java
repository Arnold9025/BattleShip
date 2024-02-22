package models;

//classe definissant les caracteristique des celulles et des elements graphiques du jeu
public class Panel {

	protected Location location;
	protected int width;
	protected int height;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Panel(Location location, int width, int height) {
		super();
		this.location = location;
		this.width = width;
		this.height = height;
	}
	 public boolean faitPartiede(Location objectLocation) {
	        return objectLocation.x >= this.location.x && objectLocation.y >=  this.location.y
	                && objectLocation.x <  this.location.x + width && objectLocation.y <  this.location.y + height;
	    }
}
