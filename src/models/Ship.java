package models;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Ship {
    //Coordonnee d'un bateau
	private Location shipLocation;
	//coordonnes de temporaire d'un bateau avant qu'il ne soit place
	private Location shipDrawLocation;
	// Compartiments d'un bateau, definissant sa  taille 
	private int compartiments;
	//Orientation que peut prendre un bateau
	private Orientation orientation;
	//Compartiments detruits 
	private int DestroyedCompartiments;
	//Couleur
	public PlacementColour color;
	
	public Ship(Location sL, Location sDL, int c, Orientation o) {
		this.shipLocation = sL;
		this.shipDrawLocation = sDL;
		this.compartiments = c;
		this.orientation = o;
		this.DestroyedCompartiments = 0;
		this.color = PlacementColour.DONE; 
	}
	//Defini la couleur du bateau en fonction de la disponibilite des cases
	public void paint(Graphics shipgr) {
		if(this.color == PlacementColour.DONE) {
			shipgr.setColor(DestroyedCompartiments >= compartiments ? new Color(9, 129, 209).darker().darker() : Color.white);
		} else {
			shipgr.setColor(this.color == PlacementColour.FREE ? Color.cyan : Color.red);
		}
		if(orientation == Orientation.HORIZONTAL) {
			paintHorizontal(shipgr);
		}else {
			paintVerticalShip(shipgr);
		}
	}

	public Location getShipLocation() {
		return shipLocation;
	}

	public void setShipLocation(Location shipLocation) {
		this.shipLocation = shipLocation;
	}

	public Location getShipDrawLocation() {
		return shipDrawLocation;
	}

	public void setShipDrawLocation(Location shipDrawLocation) {
		this.shipDrawLocation = shipDrawLocation;
	}

	public int getCompartiments() {
		return compartiments;
	}

	public void setCompartiments(int compartiments) {
		this.compartiments = compartiments;
	}

	public int getDestroyedCompartiments() {
		return DestroyedCompartiments;
	}

	public void DestroyCompartiments() {
		this.DestroyedCompartiments++;
	}

	public PlacementColour getColor() {
		return color;
	}

	public void setColor(PlacementColour color) {
		this.color = color;
	}
	public boolean estHorizontal() {
		return this.orientation == Orientation.HORIZONTAL;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void changeOrientation() {
		if(this.orientation == Orientation.HORIZONTAL) {
			this.orientation = Orientation.VERTICAL;
		}else {
			this.orientation = Orientation.HORIZONTAL;
		}
	}
	
	//definis l'etat du bateau
	public boolean isDestroyed() {
		return DestroyedCompartiments>=compartiments;
	}
	//definis les cases occupes par le bateau
	public List<Location> busyLocations(){
		List<Location>locations = new ArrayList<>();
		if(orientation == Orientation.HORIZONTAL) {
			for(int i = 0;i<compartiments;i++) {
				locations.add(new Location(shipLocation.x+i, shipLocation.y));
			}
		}else {
			for(int j =0; j < compartiments; j++) {
				locations.add(new Location(shipLocation.x, shipLocation.y+j));
			}
			
		}
		return locations;
	}
	//dessines le bateau en vertical
	public void paintVerticalShip(Graphics ship) {
		int shipWidth = (int)(GridCanvas.UNIT*0.8);
		int shipLeftX = shipDrawLocation.x + GridCanvas.UNIT / 2 - shipWidth / 2;
		ship.fillPolygon(new int[] {shipDrawLocation.x+GridCanvas.UNIT/2,shipLeftX, shipLeftX+shipWidth},
				new int[] {shipDrawLocation.y+GridCanvas.UNIT/4, shipDrawLocation.y+GridCanvas.UNIT, shipDrawLocation.y+GridCanvas.UNIT},3);
		ship.fillRect(shipLeftX, shipDrawLocation.y+GridCanvas.UNIT, shipWidth, (int)(GridCanvas.UNIT *(compartiments-1.2)));
	}
	//dessines le bateau en horizontal
	public void paintHorizontal(Graphics ship) {
		int shipWidth = (int)(GridCanvas.UNIT*0.8);
		int shiptoTop = shipDrawLocation.y + GridCanvas.UNIT / 2 - shipWidth / 2;
		ship.fillPolygon(new int [] {shipDrawLocation.x+GridCanvas.UNIT/4,shipDrawLocation.x+GridCanvas.UNIT, shipDrawLocation.x+GridCanvas.UNIT},
				         new int [] {shipDrawLocation.y+GridCanvas.UNIT/2, shiptoTop,shiptoTop+shipWidth},3);
		ship.fillRect(shipDrawLocation.x+GridCanvas.UNIT, shiptoTop,(int)(GridCanvas.UNIT*(compartiments - 1.2)), shipWidth);

	}
}

