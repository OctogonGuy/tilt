package tech.octopusdragon.tilt;

import java.util.Stack;

/**
 * Represents a game of Tilt with a game board.
 * @author Alex Gill
 *
 */
public class Game {
	
	private Grid grid;		// The grid
	private Stack<Grid> gameStates;	// Previous grids for undo
	private int numMoves;	// The number of tilts the player has done

	
	
	/**
	 * Creates a game with an empty grid with a hole in the center
	 */
	public Game() {
		grid = new Grid();
		gameStates = new Stack<Grid>();
		numMoves = 0;
	}

	/**
	 * Creates a game with a grid grid with pieces to start out with
	 * @param pieces The pieces
	 */
	public Game(PieceLocation... pieces) {
		grid = new Grid(pieces);
		gameStates = new Stack<Grid>();
		numMoves = 0;
	}
	
	/**
	 * Tilts the grid in the given direction
	 * @param direction The direction to tilt
	 * @return An array of from and to moved coordinates of sliders.
	 */
	public FromTo[] tilt(Direction direction) {
		gameStates.push(grid.copy());
		
		FromTo[] fromTo = null;
		
		switch (direction) {
		case UP:
			fromTo = grid.tiltUp();
			break;
		case RIGHT:
			fromTo = grid.tiltRight();
			break;
		case DOWN:
			fromTo = grid.tiltDown();
			break;
		case LEFT:
			fromTo = grid.tiltLeft();
			break;
		}
		
		if (grid.equals(gameStates.peek())) {
			gameStates.pop();
		}
		else {
			numMoves++;
		}
		
		return fromTo;
	}
	
	/**
	 * Undos the last move
	 */
	public void undo() {
		if (gameStates.isEmpty())
			return;
		
		grid = gameStates.pop();
		numMoves++;
	}
	
	/**
	 * Returns the space type at the given place on the grid.
	 * @param row The row of the space 
	 * @param column The column of the space
	 * @return The space type
	 */
	public Space getSpace(int row, int column) {
		return grid.getSpace(row, column);
	}
	
	
	/**
	 * @return The number of tilts the player has done
	 */
	public int getNumMoves() {
		return numMoves;
	}
	
	
	/**
	 *
	 * @return The current state of the game
	 */
	public GameState gameState() {
		if (grid.sunkBlueSlider()) {
			return GameState.LOSE;
		}
		else if (!grid.hasGreenSlider()) {
			return GameState.WIN;
		}
		else {
			return GameState.PLAYING;
		}
	}
	
	
	/**
	 * 
	 * @return Whether the game is over
	 */
	public boolean isOver() {
		return gameState() != GameState.PLAYING;
	}
}
