package views;


import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
//Classe du SplashScreen
public class Splash extends JWindow {

	
	Splash(){
		this.setLocation(300, 100);
		this.setSize(800,600);
		 JPanel panel = new JPanel();
		 JLabel label = new JLabel();
		 JLabel text = new JLabel("Bienvenue dans mon jeu");
		 label.setIcon(new ImageIcon(getClass().getResource("/shipSlash.jpg")));
		    

        this.setContentPane(label);
        this.add(panel);
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws InterruptedException {
		Splash s = new Splash();
        Thread.sleep(4000);
        s.dispose();
        Game.main(args);
        }
}


