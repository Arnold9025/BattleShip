package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmartAI extends IntelligenceAI {

	private List<Location> shipHits;

	private final boolean debugAI = false;

	private boolean preferMovesFormingLine;

	private boolean maximiseAdjacentRandomisation;

	public SmartAI(GridCanvas playerGrid, boolean preferMovesFormingLine, boolean maximiseAdjacentRandomisation) {
		super(playerGrid);
		shipHits = new ArrayList<>();
		this.preferMovesFormingLine = preferMovesFormingLine;
		this.maximiseAdjacentRandomisation = maximiseAdjacentRandomisation;
		Collections.shuffle(validMoves);
	}

	@Override
	public void reset() {
		super.reset();
		shipHits.clear();
		Collections.shuffle(validMoves);
	}

	@Override
	public Location selectMove() {
		if (debugAI)
			System.out.println("\nBEGIN TURN===========");
		Location selectedMove;
		// If a ship has been hit, but not destroyed
		if (shipHits.size() > 0) {
			if (preferMovesFormingLine) {
				selectedMove = getSmarterAttack();
			} else {
				selectedMove = getSmartAttack();
			}
		} else {
			if (maximiseAdjacentRandomisation) {
				selectedMove = findMostOpenPosition();
			} else {
				// Use a random move
				selectedMove = validMoves.get(0);
			}
		}
		updateShipHits(selectedMove);
		validMoves.remove(selectedMove);
		if (debugAI) {
			System.out.println("Selected Move: " + selectedMove);
			System.out.println("END TURN===========");
		}
		return selectedMove;
	}

	private Location getSmartAttack() {
		List<Location> suggestedMoves = getAdjacentSmartMoves();
		Collections.shuffle(suggestedMoves);
		return suggestedMoves.get(0);
	}

	private Location getSmarterAttack() {
		List<Location> suggestedMoves = getAdjacentSmartMoves();
		for (Location possibleOptimalMove : suggestedMoves) {
			if (atLeastTwoHitsInDirection(possibleOptimalMove, Location.GAUCHE))
				return possibleOptimalMove;
			if (atLeastTwoHitsInDirection(possibleOptimalMove, Location.DROITE))
				return possibleOptimalMove;
			if (atLeastTwoHitsInDirection(possibleOptimalMove, Location.BAS))
				return possibleOptimalMove;
			if (atLeastTwoHitsInDirection(possibleOptimalMove, Location.HAUT))
				return possibleOptimalMove;
		}
		// No optimal choice found, just randomise the move.
		Collections.shuffle(suggestedMoves);
		return suggestedMoves.get(0);
	}

	private Location findMostOpenPosition() {
		Location position = validMoves.get(0);
		;
		int highestNotAttacked = -1;
		for (int i = 0; i < validMoves.size(); i++) {
			int testCount = getAdjacentNotAttackedCount(validMoves.get(i));
			if (testCount == 4) { // Maximum found, just return immediately
				return validMoves.get(i);
			} else if (testCount > highestNotAttacked) {
				highestNotAttacked = testCount;
				position = validMoves.get(i);
			}
		}
		return position;
	}

	
	private int getAdjacentNotAttackedCount(Location position) {
		List<Location> adjacentCells = getAdjacentCells(position);
		int notAttackedCount = 0;
		for (Location adjacentCell : adjacentCells) {
			if (!playerGrid.getSignAtLocation(adjacentCell).Signed()) {
				notAttackedCount++;
			}
		}
		return notAttackedCount;
	}

	private boolean atLeastTwoHitsInDirection(Location start, Location direction) {
		Location testPosition = new Location(start);
		testPosition.add(direction);
		if (!shipHits.contains(testPosition))
			return false;
		testPosition.add(direction);
		if (!shipHits.contains(testPosition))
			return false;
		if (debugAI)
			System.out.println("Smarter match found AT: " + start + " TO: " + testPosition);
		return true;
	}

	private List<Location> getAdjacentSmartMoves() {
		List<Location> result = new ArrayList<>();
		for (Location shipHitPos : shipHits) {
			List<Location> adjacentPositions = getAdjacentCells(shipHitPos);
			for (Location adjacentPosition : adjacentPositions) {
				if (!result.contains(adjacentPosition) && validMoves.contains(adjacentPosition)) {
					result.add(adjacentPosition);
				}
			}
		}
		if (debugAI) {
			printPositionList("Ship Hits: ", shipHits);
			printPositionList("Adjacent Smart Moves: ", result);
		}
		return result;
	}

	private void printPositionList(String messagePrefix, List<Location> data) {
		String result = "[";
		for (int i = 0; i < data.size(); i++) {
			result += data.get(i);
			if (i != data.size() - 1) {
				result += ", ";
			}
		}
		result += "]";
		System.out.println(messagePrefix + " " + result);
	}

	private List<Location> getAdjacentCells(Location position) {
		List<Location> result = new ArrayList<>();
		if (position.x != 0) {
			Location left = new Location(position);
			left.add(Location.GAUCHE);
			result.add(left);
		}
		if (position.x != GridCanvas.CANVAS_WIDTH - 1) {
			Location right = new Location(position);
			right.add(Location.DROITE);
			result.add(right);
		}
		if (position.y != 0) {
			Location up = new Location(position);
			up.add(Location.HAUT);
			result.add(up);
		}
		if (position.y != GridCanvas.CANVAS_HEIGHT - 1) {
			Location down = new Location(position);
			down.add(Location.BAS);
			result.add(down);
		}
		return result;
	}


	private void updateShipHits(Location testPosition) {
		Sign marker = playerGrid.getSignAtLocation(testPosition);
		if (marker.shipHere()) {
			shipHits.add(testPosition);
			// Check to find if this was the last place to hit on the targeted ship
			List<Location> allPositionsOfLastShip = marker.getShipHere().busyLocations();
			if (debugAI)
				printPositionList("Last Ship", allPositionsOfLastShip);
			boolean hitAllOfShip = containsAllPositions(allPositionsOfLastShip, shipHits);
			// If it was remove the ship data from history to now ignore it
			if (hitAllOfShip) {
				for (Location shipPosition : allPositionsOfLastShip) {
					for (int i = 0; i < shipHits.size(); i++) {
						if (shipHits.get(i).equals(shipPosition)) {
							shipHits.remove(i);
							if (debugAI)
								System.out.println("Removed " + shipPosition);
							break;
						}
					}
				}
			}
		}
	}

	private boolean containsAllPositions(List<Location> positionsToSearch, List<Location> listToSearchIn) {
		for (Location searchPosition : positionsToSearch) {
			boolean found = false;
			for (Location searchInPosition : listToSearchIn) {
				if (searchInPosition.equals(searchPosition)) {
					found = true;
					break;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}
}
