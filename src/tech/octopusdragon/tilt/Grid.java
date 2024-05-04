package tech.octopusdragon.tilt;

import java.util.ArrayList;
import java.util.List;

/**
 * A grid in a Tilt game
 * @author Alex Gill
 *
 */
public class Grid {
	
	public static final int ROWS = 5;		// Number of rows
	public static final int COLUMNS = 5;	// Number of columns
	
	private Space[][] grid;	// The spaces on the grid
	private int numBlueSliders;	// The number of blue sliders in play

	public Grid() {
		grid = new Space[ROWS][COLUMNS];
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (i == ROWS / 2 && j == COLUMNS / 2) {
					grid[i][j] = Space.HOLE;
				}
				else {
					grid[i][j] = Space.EMPTY;
				}
			}
		}
		
		numBlueSliders = 0;
	}
	
	public Grid(PieceLocation... pieces) {
		this();
		for (PieceLocation piece : pieces) {
			grid[piece.getRow()][piece.getColumn()] = piece.getType();
			
			if (piece.getType() == Space.BLUE_SLIDER)
				numBlueSliders++;
		}
	}
	
	
	
	/**
	 * Tilts the game grid to the up
	 * @return An array of from and to moved coordinates of sliders.
	 */
	public FromTo[] tiltUp() {
		List<FromTo> moves = new ArrayList<FromTo>();
		
		for (int j = 0; j < COLUMNS; j++) {
			for (int i = 1; i < ROWS; i++) {
				if (grid[i][j] == Space.BLUE_SLIDER ||
						grid[i][j] == Space.GREEN_SLIDER) {
					FromTo movement = new FromTo();
					moves.add(movement);
					movement.setFromRow(i);
					movement.setFromColumn(j);
					for (int k = i; k >= 1; k--) {
						if (grid[k - 1][j] == Space.HOLE) {
							grid[k][j] = Space.EMPTY;
							movement.setToRow(k - 1);
							movement.setToColumn(j);
							break;
						} else if (grid[k - 1][j] != Space.EMPTY) {
							movement.setToRow(k);
							movement.setToColumn(j);
							break;
						}
						grid[k - 1][j] = grid[k][j];
						grid[k][j] = Space.EMPTY;
						movement.setToRow(k - 1);
						movement.setToColumn(j);
					}
				}
			}
		}
		
		return moves.toArray(new FromTo[0]);
	}

	/**
	 * Tilts the game grid down
	 * @return An array of from and to moved coordinates of sliders.
	 */
	public FromTo[] tiltDown() {
		List<FromTo> moves = new ArrayList<FromTo>();
		
		for (int j = 0; j < COLUMNS; j++) {
			for (int i = ROWS - 2; i >= 0; i--) {
				if (grid[i][j] == Space.BLUE_SLIDER ||
						grid[i][j] == Space.GREEN_SLIDER) {
					FromTo movement = new FromTo();
					moves.add(movement);
					movement.setFromRow(i);
					movement.setFromColumn(j);
					for (int k = i; k < ROWS - 1; k++) {
						if (grid[k + 1][j] == Space.HOLE) {
							grid[k][j] = Space.EMPTY;
							movement.setToRow(k + 1);
							movement.setToColumn(j);
							break;
						} else if (grid[k + 1][j] != Space.EMPTY) {
							movement.setToRow(k);
							movement.setToColumn(j);
							break;
						}
						grid[k + 1][j] = grid[k][j];
						grid[k][j] = Space.EMPTY;
						movement.setToRow(k + 1);
						movement.setToColumn(j);
					}
				}
			}
		}
		
		return moves.toArray(new FromTo[0]);
	}

	/**
	 * Tilts the game grid to the left
	 * @return An array of from and to moved coordinates of sliders.
	 */
	public FromTo[] tiltLeft() {
		List<FromTo> moves = new ArrayList<FromTo>();
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 1; j < COLUMNS; j++) {
				if (grid[i][j] == Space.BLUE_SLIDER ||
						grid[i][j] == Space.GREEN_SLIDER) {
					FromTo movement = new FromTo();
					moves.add(movement);
					movement.setFromRow(i);
					movement.setFromColumn(j);
					for (int k = j; k >= 1; k--) {
						if (grid[i][k - 1] == Space.HOLE) {
							grid[i][k] = Space.EMPTY;
							movement.setToRow(i);
							movement.setToColumn(k - 1);
							break;
						} else if (grid[i][k - 1] != Space.EMPTY) {
							movement.setToRow(i);
							movement.setToColumn(k);
							break;
						}
						grid[i][k - 1] = grid[i][k];
						grid[i][k] = Space.EMPTY;
						movement.setToRow(i);
						movement.setToColumn(k - 1);
					}
				}
			}
		}
		
		return moves.toArray(new FromTo[0]);
	}

	/**
	 * Tilts the game grid to the right
	 * @return An array of from and to moved coordinates of sliders.
	 */
	public FromTo[] tiltRight() {
		List<FromTo> moves = new ArrayList<FromTo>();
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = COLUMNS - 2; j >= 0; j--) {
				if (grid[i][j] == Space.BLUE_SLIDER ||
						grid[i][j] == Space.GREEN_SLIDER) {
					FromTo movement = new FromTo();
					moves.add(movement);
					movement.setFromRow(i);
					movement.setFromColumn(j);
					for (int k = j; k < COLUMNS - 1; k++) {
						if (grid[i][k + 1] == Space.HOLE) {
							grid[i][k] = Space.EMPTY;
							movement.setToRow(i);
							movement.setToColumn(k + 1);
							break;
						} else if (grid[i][k + 1] != Space.EMPTY) {
							movement.setToRow(i);
							movement.setToColumn(k);
							break;
						}
						grid[i][k + 1] = grid[i][k];
						grid[i][k] = Space.EMPTY;
						movement.setToRow(i);
						movement.setToColumn(k + 1);
					}
				}
			}
		}
		
		return moves.toArray(new FromTo[0]);
	}
	
	/**
	 * Returns the space type at the given place on the grid.
	 * @param row The row of the space 
	 * @param column The column of the space
	 * @return The space type
	 */
	public Space getSpace(int row, int column) {
		return grid[row][column];
	}
	
	
	
	/**
	 * 
	 * @return Whether the green slider is still on the grid
	 */
	public boolean hasGreenSlider() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (grid[i][j] == Space.GREEN_SLIDER) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	/**
	 * 
	 * @return Whether a blue slider has been sunk
	 */
	public boolean sunkBlueSlider() {
		int numBlueSlidersLeft = 0;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (grid[i][j] == Space.BLUE_SLIDER) {
					numBlueSlidersLeft++;
				}
			}
		}
		
		return numBlueSlidersLeft < numBlueSliders;
	}
	
	
	/**
	 * @return A copy of this grid
	 */
	public Grid copy() {
		Grid copy = new Grid();
		
		copy.grid = new Space[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				copy.grid[i][j] = this.grid[i][j];
			}
		}
		copy.numBlueSliders = this.numBlueSliders;
		
		return copy;
	}
	
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject.getClass() != Grid.class)
			return false;
		Grid other = (Grid) otherObject;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (other.grid[i][j] != this.grid[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}

}
