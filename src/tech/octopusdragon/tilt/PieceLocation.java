package tech.octopusdragon.tilt;

/**
 * A grid space used to instantiate a Grid object.
 * @author Alex Gill
 *
 */
public class PieceLocation {
	
	private Space type;	// The type of space
	private int row;	// The row index
	private int column;	// The column index

	
	
	/**
	 * Instantiates a grid space
	 * @param type The type of space
	 * @param row The row index
	 * @param column The column index
	 */
	public PieceLocation(Space type, int row, int column) {
		this.type = type;
		this.row = row;
		this.column = column;
	}
	
	
	
	/**
	 * 
	 * @return The type of space
	 */
	public Space getType() {
		return type;
	}
	
	/**
	 * 
	 * @return The row index
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * 
	 * @return The column index
	 */
	public int getColumn() {
		return column;
	}
}
