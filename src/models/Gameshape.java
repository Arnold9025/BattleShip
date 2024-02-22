package models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;

import javax.swing.*;

import views.GameState;

public class Gameshape extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	private GridCanvas player;
	private GridCanvas ai;
	private Location tempLoc;
	private int index;
	private Ship placingShip;
	private GameState gState;
	StatutUpdatePanel statusPanel;
	IntelligenceAI aiIntelligence;
	private boolean debug;
	JLabel panelLeft;
	JPanel panel2;
	JPanel panel3;
	JButton restart;
	JButton flip;
	JButton quit;

	public Gameshape(int difficulty) {
		this.setLayout(new GridLayout(1, 3));
		this.panel2 = new JPanel();
		this.panel3 = new JPanel();
		JPanel panel2in3 = new JPanel();
		String imageLabel = "Cover3.jpg";
		ImageIcon imageLeft = new ImageIcon(imageLabel);
		String imageLab = "presents1.jpg";
		ImageIcon imagehaut = new ImageIcon(imageLab);
		JLabel panel1in3 = new JLabel(imagehaut);
		this.panelLeft = new JLabel(imageLeft);
		this.flip = new JButton("FLIP");
		this.flip.setActionCommand("flip");
		this.flip.setBackground(Color.gray.brighter());
		this.flip.setForeground(Color.black);
		this.flip.addActionListener(this);
		this.quit = new JButton("QUITTER");
		this.quit.setActionCommand("quit");
		this.quit.setBackground(Color.red.darker());
		this.quit.setForeground(Color.white);
		this.quit.addActionListener(this);
		this.restart = new JButton("RESTART");
		this.restart.setActionCommand("restart");
		this.restart.setBounds(200, 100, 100, 50);
		this.restart.setBackground(Color.gray.brighter());
		this.restart.setForeground(Color.black);
		this.restart.addActionListener(this);
		
		panel2.setBackground(new Color(9, 129, 209));
		panel2in3.add(flip);
		panel2in3.add(restart);
		panel2in3.add(quit);
		panel2in3.setLayout(new GridLayout(3,1));
		this.panel3.add(panel1in3);
		this.panel3.add(panel2in3);
		panel3.setLayout(new GridLayout(2,1));
		panel3.setBackground(Color.green);
		ai = new GridCanvas(450, 60);
		player = new GridCanvas(450, ai.getHeight() + 85);
		statusPanel = new StatutUpdatePanel(new Location(400, 0), 400, 49);
		setPreferredSize(new Dimension(1200, 700));
		addMouseListener(this);
		addMouseMotionListener(this);
		this.add(this.panelLeft);
		this.add(panel2);
		this.add(panel3);
		if (gState == GameState.PLACING) {
			this.flip.setEnabled(false);
		} else {
			this.flip.setEnabled(true);
		}

		  if(difficulty == 0) aiIntelligence = new EasyAI(player);
	        else aiIntelligence = new SmartAI(player,difficulty == 2,difficulty == 2);
		restart();
	}

	private void placeShip(Location mouseLocation) {
		Location cible = player.getPositionInGrid(mouseLocation.x, mouseLocation.y);
		updatePlacement(cible);
		if (player.canPlaceAt(cible.x, cible.y, GridCanvas.BOAT_SIZES[index], placingShip.estHorizontal())) {
			place(cible);
		}
	}

	private void tryMovePlacingShip(Location mousePosition) {
		if (player.faitPartiede(mousePosition)) {
			Location targetPos = player.getPositionInGrid(mousePosition.x, mousePosition.y);
			updatePlacement(targetPos);
		}
	}

	private void place(Location cibleLocation) {
		placingShip.setColor(PlacementColour.DONE);
		player.placeShip(placingShip, tempLoc.x, tempLoc.y);
		index++;
		// Tant que toutes les bateaux ne sont pas places
		if (index < GridCanvas.BOAT_SIZES.length) {
			placingShip = new Ship(new Location(cibleLocation.x, cibleLocation.y),
					new Location(player.getLocation().x + cibleLocation.x * GridCanvas.UNIT,
							player.getLocation().y + cibleLocation.y * GridCanvas.UNIT),
					GridCanvas.BOAT_SIZES[index], Orientation.HORIZONTAL);
			updatePlacement(tempLoc);
		} else {
			gState = GameState.GAMING;
			statusPanel.setTopLine("Tirez sur l'ennemi");
			statusPanel.setBottomLine("Vous remportez la guerre lorsque la flotte ennemi sera anneanti");
		}
	}

	private void updatePlacement(Location targetPos) {
		// Constrain to fit inside the grid
		if (placingShip.estHorizontal()) {
			targetPos.x = Math.min(targetPos.x, GridCanvas.CANVAS_WIDTH - GridCanvas.BOAT_SIZES[index]);
		} else {
			targetPos.y = Math.min(targetPos.y, GridCanvas.CANVAS_HEIGHT - GridCanvas.BOAT_SIZES[index]);
		}
		// Update drawing position to use the new target position
		placingShip.setShipLocation(new Location(targetPos.x, targetPos.y));
		placingShip.setShipDrawLocation(new Location(player.getLocation().x + targetPos.x * GridCanvas.UNIT,
				player.getLocation().y + targetPos.y * GridCanvas.UNIT));
		// Store the grid position for other testing cases
		tempLoc = targetPos;
		// Change the colour of the ship based on whether it could be placed at the
		// current location.
		if (player.canPlaceAt(tempLoc.x, tempLoc.y, GridCanvas.BOAT_SIZES[index], placingShip.estHorizontal())) {
			placingShip.setColor(PlacementColour.FREE);
		} else {
			placingShip.setColor(PlacementColour.BUSY);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		ai.paint(g);
		player.paint(g);
		if (gState == GameState.PLACING) {
			placingShip.paint(g);
		}
		statusPanel.paint(g);
	}

	public void restart() {
		ai.reset();
		player.reset();
		// Player can see their own ships by default
		player.setShowShip(true);
		aiIntelligence.reset();
		tempLoc = new Location(0, 0);
		placingShip = new Ship(new Location(0, 0), new Location(player.getLocation().x, player.getLocation().y),
				GridCanvas.BOAT_SIZES[0], Orientation.HORIZONTAL);
		index = 0;
		updatePlacement(tempLoc);
		ai.randomplacing();
		debug = false;
		statusPanel.reset();
		gState = GameState.PLACING;
	}

	private void doAITurn() {
		Location aiMove = aiIntelligence.selectMove();
		boolean hit = player.markThisLocation(aiMove);
		String hitMiss = hit ? "L'ennemi nous a atteint aux coordonnees" : "a rate le tire aux coordonnees";
		String destroyed = "";
		if (hit && player.getSignAtLocation(aiMove).getShipHere().isDestroyed()) {
			destroyed = "(Destroyed)";
		}
		statusPanel.setBottomLine("Computer " + hitMiss + " " + aiMove + destroyed);
		if (player.areAllOver()) {
			// Computer wins!
			gState = GameState.GAMEOVER;
			statusPanel.showGameOver(false);
		}
	}
	

	private void doPlayerTurn(Location targetPosition) {
		boolean hit = ai.markThisLocation(targetPosition);
		String hitMiss = hit ? "Sacre tir aux coordonnees " : "a rate le tire aux coordonnees";
		String destroyed = "";
		if (hit && ai.getSignAtLocation(targetPosition).getShipHere().isDestroyed()) {
			destroyed = "(Destroyed)";
		}
		statusPanel.setTopLine("Player " + hitMiss + " " + targetPosition + destroyed);
		if (ai.areAllOver()) {
			// Le joueur a gagne
			gState = GameState.GAMEOVER;
			statusPanel.showGameOver(true);
		}
	}

	private void Game(Location mousePosition) {
		Location targetPosition = ai.getPositionInGrid(mousePosition.x, mousePosition.y);
		// Ignore if position was already clicked
		if (!ai.estmarque(targetPosition)) {
			doPlayerTurn(targetPosition);
			// Only do the AI turn if the game didn't end from the player's turn.
			if (!ai.areAllOver()) {
				doAITurn();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gState != GameState.PLACING)
			return;
		tryMovePlacingShip(new Location(e.getX(), e.getY()));
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Location mousePosition = new Location(e.getX(), e.getY());
		if (gState == GameState.PLACING && player.faitPartiede(mousePosition)) {
			placeShip(mousePosition);
		} else if (gState == GameState.GAMING && ai.faitPartiede(mousePosition)) {
			Game(mousePosition);
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String text = button.getActionCommand();

		switch (text) {
		case "flip":
			placingShip.changeOrientation();
			updatePlacement(tempLoc);
			;
			break;
		case "restart":
			 restart();
			break;
		case "quit":
			 System.exit(1);;
			break;
		}

		
	}
}
