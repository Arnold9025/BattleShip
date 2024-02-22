package models;

import java.awt.*;
import java.awt.Font;

public class StatutUpdatePanel extends Panel{
	   private final Font font = new Font("Arial", Font.CENTER_BASELINE, 12);
	   //Ligne d'en haut lorsque le jeu est en phase de placementdes bateaux
	    private final String whenPlacingShip1 = "Creer Votre plan strategique placez votre flotte";
	   //Ligne d'en bas lorsque le jeu est en phase de placementdes bateaux
	    private final String whenPlacingShip2 = "Appuyez sur Flip pour retourner.";
	   //Mesage s'affichant sur la premiere ligne quand le joueur perds
	    private final String gameOver = "Vous avez perdu";
		//Mesage s'affichant sur la premiere ligne quand le joueur gagne
	    private final String win= "Vous avez gagne";
       //message s'affichant sur la seconde ligne
	    private final String gameOverBottomLine = "Appuiyez sur restart pour recommencer.";
	    private String topLine;

	    private String bottomLine;
	    
	    public StatutUpdatePanel(Location ocation, int width, int height) {
	        super(ocation, width, height);
	        reset();
	    }
        //Message initial
	    public void reset() {
	        topLine = whenPlacingShip1;
	        bottomLine = whenPlacingShip2;
	    }
     
	    public void showGameOver(boolean playerWon) {
	        topLine = (playerWon) ? win : gameOver;
	        bottomLine = gameOverBottomLine;
	    }

	    public void setTopLine(String message) {
	        topLine = message;
	    }

	    public void setBottomLine(String message) {
	        bottomLine = message;
	    }


	    public void paint(Graphics g) {
	        g.setColor(Color.white);
	        g.fillRect(location.x, location.y, width, height);
	        g.setColor(Color.black);
	        g.setFont(font);
	        int strWidth = g.getFontMetrics().stringWidth(topLine);
	        g.drawString(topLine, location.x+width/2-strWidth/2, location.y+20);
	        strWidth = g.getFontMetrics().stringWidth(bottomLine);
	        g.drawString(bottomLine, location.x+width/2-strWidth/2, location.y+40);
	    }
}
