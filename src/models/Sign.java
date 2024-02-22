package models;

import java.awt.Color;
import java.awt.Graphics;

public class Sign extends Panel{

	private final Color NO_SHIP = new Color(9, 129, 209).brighter();
	private final Color LOCATED = new Color(9, 129, 209).darker();
	
	private final int PADDING = 3;
	
	private boolean showSign;
	
	private Ship ShipAtSign;
	
	
	public Sign(int x, int y, int width, int height) {
		super(new Location(x, y), width, height);
	}
	
	
	public boolean shipHere() {
		return ShipAtSign != null;
	}
	
	public void MakeSign() {
		if(!showSign && shipHere() && ShipAtSign.isDestroyed() == false) {
			ShipAtSign.DestroyCompartiments();
		}
		showSign = true;
	}
	
	
	
	public boolean Signed() {
		return showSign;
	}
	
	
	
	public void reset() {
		ShipAtSign = null;
		showSign = false;
	}
	
	public void CreateShipHere(Ship ship) {
		this.ShipAtSign = ship;
	}
	
	public Ship getShipHere() {
		return ShipAtSign;
		
	}
	
	public void paint(Graphics g) {
		if(!showSign) return;
		
		g.setColor(shipHere() ? LOCATED : NO_SHIP);
		g.fillRect(location.x+PADDING+1, location.y+PADDING+1, width-PADDING*2, height-PADDING*2);
	}
}
