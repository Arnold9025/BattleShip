package views;

import javax.swing.*;

import models.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Game extends JFrame implements KeyListener {
 
    public static void main(String[] args) throws InterruptedException {
        @SuppressWarnings("unused")
    	Splash s = new Splash();
        Thread.sleep(4000);
        s.dispose();
		Game game = new Game();
    }

    private Gameshape gameShape;

	public Game() {
		 super("Battleship Arnold");

		ImageIcon ship = new ImageIcon("ship.png");
		JLabel icon = new JLabel(ship);
		JLabel text1 = new JLabel(
				"Nominations - est le niveau le plus facile, l'adversaire operera plus ou moins aleatoirement.");
		JLabel text2 = new JLabel(
				"Officier Superieur -  est le niveau intermediaire, l'adversiaire se concentrera sur les endroits  ");
		JLabel text3 = new JLabel("Amiral - est le niveau le plus complique et fera des choix plus strategiques.");
		JLabel text2_ = new JLabel("                                        ou il a touche un navire pour la premiere fois.");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 0));
		panel.add(icon);
		panel.add(text1);
		panel.add(text2);
		panel.add(text2_);
		panel.add(text3);
		String[] difficulty = new String[] { "Nomination", "Officier Superieur", "Amiral" };

		int choice = JOptionPane.showOptionDialog(null, panel, "Choisir une Difficulte", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, ship, difficulty, difficulty[0]);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(100,0);
		gameShape = new Gameshape(choice);
        Image iconShip = new ImageIcon(this.getClass().getResource("/icon.png")).getImage();
        this.setIconImage(iconShip);
		this.add(gameShape);
		this.getContentPane().setBackground(Color.red);
		this.addKeyListener(this);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
