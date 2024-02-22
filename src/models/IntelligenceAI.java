package models;

import java.util.ArrayList;
import java.util.List;

public class IntelligenceAI {
   
    protected GridCanvas playerGrid;
   
    protected List<Location> validMoves;

    public IntelligenceAI(GridCanvas playerGrid) {
        this.playerGrid = playerGrid;
        possibleMove();
    }

 // Logique de tir de l'IA
    public Location selectMove() {
        return Location.NUL;
    }

//Recreates the valid move list.
  
    public void reset() {
        possibleMove();
    }
//crees une liste de positions base sur toutes les cases de la grille qu'il stocke dans un tableau 
    
 
    private void possibleMove() {
        validMoves = new ArrayList<>();
        for(int x = 0; x < GridCanvas.CANVAS_WIDTH; x++) {
            for(int y = 0; y < GridCanvas.CANVAS_HEIGHT; y++) {
                validMoves.add(new Location(x,y));
            }
        }
    }
}

