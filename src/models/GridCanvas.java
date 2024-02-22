package models;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridCanvas extends Panel {

	//taille de chaque case de la grille
	public static final int UNIT = 30;
	//nombre de case en hauteur
	public static final int CANVAS_HEIGHT = 10;
	//nombre de case en largeur
	public static final int CANVAS_WIDTH = 10;
	//tableau des navires comportant la taille de chaque navire
	public static final int[] BOAT_SIZES = { 4, 3, 3, 2 };
	//un tableau de marqueur pour signifier les differents case non vides renfermant les indices de chaque case
	private Sign[][] markers = new Sign[CANVAS_WIDTH][CANVAS_HEIGHT];
	//boolean pour montrer la flotte du joueur
	private boolean showShip;
	//boolean permettant de savoir si les joueurs disposent encore de navire
	private boolean allOver;
	//La flotte de bateau
	private List<Ship> laFlotte;
	//permet de placer aleatoirement les bateaux
	private Random random;

	public GridCanvas(int x, int y) {
		super(new Location(x, y), UNIT * CANVAS_WIDTH, UNIT * CANVAS_HEIGHT);
		createMarkerGrid();
		random = new Random();
		laFlotte = new ArrayList<>();
		showShip = false;
	}
	   public void reset() {
	        for(int x = 0; x < CANVAS_WIDTH; x++) {
	            for(int y = 0; y < CANVAS_HEIGHT; y++) {
	                markers[x][y].reset();
	            }
	        }
	        laFlotte.clear();
	        showShip = false;
	        allOver = false;
	    }

	private void drawGrid(Graphics g) {
		g.setColor(Color.BLACK);
		// lignes verticales
		int y2 = location.y;
		int y1 = location.y + height;
		for (int x = 0; x <= CANVAS_WIDTH; x++)
			/*
			 * drawLine dessine une ligne entre 2 points de coordonnees x1,y1 et x2,y2 x1
			 * ici prends le coordonnes de ou se trouve la case et y ajoute l'indice ci
			 * dessus, ce qui fait une premiere ligne admettons que la grille commence a 0,
			 * location.x = 0, + (x =0* UNIT) ce qui donne toujours 0, le point de depart de
			 * la ligne est alors 0 y1 est le coordonnee en ordonnees du point de depart de
			 * la grille, admettons 0 aussi du cout le premier point de notre permiere ligne
			 * est A(0,0); le second point a pour abscisse, le meme point que le precedent
			 * c'est a dire 0 et po pour ordonnee, le point du nord Ouest de la grille + Sa
			 * hauteur soit B(0,10)
			 */
			g.drawLine(location.x + x * UNIT, y1, location.x + x * UNIT, y2);

		// lignes horizontales
		int x2 = location.x;
		int x1 = location.x + width;
		for (int y = 0; y <= CANVAS_HEIGHT; y++)
			// meme chose que la ligne verticale mais autrement
			g.drawLine(x1, location.y + y * UNIT, x2, location.y + y * UNIT);
	}

	// paint est la methode qui dessinne la grille
	public void paint(Graphics g) {
       for(Ship ship : laFlotte) {
    	   if(showShip || ship.isDestroyed()) {
    		   ship.paint(g);
    	   }
       }
		drawMarkers(g);
		drawGrid(g);
	}
    public boolean estmarque(Location posToTest) {
        return markers[posToTest.x][posToTest.y].Signed();
    }
    public Sign getSignAtLocation(Location locationToSelect) {
        return markers[locationToSelect.x][locationToSelect.y];
    }
//donne la position de lasouris sur la grille
	public Location getPositionInGrid(int mouseX, int mouseY) {
		if (!faitPartiede(new Location(mouseX, mouseY)))
			return new Location(-1, -1);

		return new Location((mouseX - location.x) / UNIT, (mouseY - location.y) / UNIT);
	}

	public boolean isShowShip() {
		return showShip;
	}

	public void setShowShip(boolean showShip) {
		this.showShip = showShip;
	}
//verifies si un navire peut etre place a un endroit specifique
	public boolean canPlaceAt(int gridX, int gridY, int segments, boolean sideways) {
		if (gridX < 0 || gridY < 0)
			return false;

		if (sideways) { // horizontal
			if (gridY > CANVAS_HEIGHT || gridX + segments > CANVAS_WIDTH)
				return false;
			for (int x = 0; x < segments; x++) {
				if (markers[gridX + x][gridY].shipHere())
					return false;
			}
		} else { // vertical
			if (gridY + segments > CANVAS_HEIGHT || gridX > CANVAS_WIDTH)
				return false;
			for (int y = 0; y < segments; y++) {
				if (markers[gridX][gridY + y].shipHere())
					return false;
			}
		}
		return true;
	}
//marque une position precise et verifie s'il y'a encore des navires non detruite
	public boolean markThisLocation(Location location) {
		markers[location.x][location.y].MakeSign();

		allOver = true;
		for (Ship ship : laFlotte) {
			if (!ship.isDestroyed()) {
				allOver = false;
				break;
			}
		}
		return markers[location.x][location.y].shipHere();
	}
//verifie si toutes les navires ont ete detruites
	public boolean areAllOver() {
		return allOver;
	}

	// methode permettant de colorier les cadres
	private void drawMarkers(Graphics markers) {
		for (int i = 0; i < CANVAS_WIDTH; i++) {
			for (int j = 0; j < CANVAS_HEIGHT; j++) {
				this.markers[i][j].paint(markers);
			}
		}

	}
	//Place un navire sur la grille avec les propriétés spécifiées.*/
	public void placeShip(Ship ship, int gridX, int gridY) {
		laFlotte.add(ship);
		if (ship.estHorizontal()) { // If the ship is horizontal
			for (int x = 0; x < ship.getCompartiments(); x++) {
				markers[gridX + x][gridY].CreateShipHere(laFlotte.get(laFlotte.size() - 1));
			}
		} else { // If the ship is vertical
			for (int y = 0; y < ship.getCompartiments(); y++) {
				markers[gridX][gridY + y].CreateShipHere(laFlotte.get(laFlotte.size() - 1));
			}
		}
	}
// Place un navire sur la grille avec les propriétés spécifiées.
	public void placeShip(int X, int Y, int compartiments, Orientation orientation) {
		placeShip(new Ship(new Location(X, Y), new Location(location.x + X * UNIT, location.y + Y * UNIT),
				compartiments, orientation.HORIZONTAL), X, Y);
	}

	// methode permettant de generer de facon totalement aleatoire la place des
	// navires
	public void randomplacing() {
		laFlotte.clear();
		for (int i = 0; i < BOAT_SIZES.length; i++) {
			// determine au hazard si le navire est vertical ou horizontal
			boolean sideways = random.nextBoolean();
			Orientation orientation;
			if (sideways) {
				orientation = Orientation.HORIZONTAL;
			} else {
				orientation = Orientation.VERTICAL;
			}
			int X, Y;
			do {
				X = random.nextInt(sideways ? CANVAS_WIDTH - BOAT_SIZES[i] : CANVAS_WIDTH);
				Y = random.nextInt(sideways ? CANVAS_HEIGHT : CANVAS_HEIGHT - BOAT_SIZES[i]);
			} while (!canPlaceAt(X, Y, BOAT_SIZES[i], sideways));
			placeShip(X, Y, BOAT_SIZES[i], orientation);
		}
	}
//Crees toutes les marqueurs de cases;
	private void createMarkerGrid() {
		for (int i = 0; i < CANVAS_WIDTH; i++) {
			for (int j = 0; j < CANVAS_HEIGHT; j++) {
				this.markers[i][j] = new Sign(location.x + i * UNIT, location.y + j * UNIT, UNIT, UNIT);
			}
		}
	}
	
}
