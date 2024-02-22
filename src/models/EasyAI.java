package models;

import java.util.Collections;

//EasyAI est la classe qui coordonne le comportement de L'AI de niveau facile et il herite de la classe de l'intelligence artificiel
public class EasyAI extends IntelligenceAI {

	public EasyAI(GridCanvas playerGrid) {
		super(playerGrid);
		Collections.shuffle(validMoves);
	}

	@Override
	public void reset() {
		super.reset();
		Collections.shuffle(validMoves);
	}

	@Override
	public Location selectMove() {
		Location nextMove = validMoves.get(0);
		validMoves.remove(0);
		return nextMove;
	}
}
