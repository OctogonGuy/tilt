package tech.octopusdragon.tilt;

/**
 * Represents a pair of coordinates representing a location from which a piece
 * moved and a location to which the piece moved.
 * @author Alex Gill
 *
 */
public class FromTo {
	
	private int fromRow;
	private int fromColumn;
	private int toRow;
	private int toColumn;
	
	public FromTo() { }

	public FromTo(int fromRow, int fromColumn, int toRow, int toColumn) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
	}

	/**
	 * @return the fromRow
	 */
	public int getFromRow() {
		return fromRow;
	}

	/**
	 * @param fromRow the fromRow to set
	 */
	public void setFromRow(int fromRow) {
		this.fromRow = fromRow;
	}

	/**
	 * @return the fromColumn
	 */
	public int getFromColumn() {
		return fromColumn;
	}

	/**
	 * @param fromColumn the fromColumn to set
	 */
	public void setFromColumn(int fromColumn) {
		this.fromColumn = fromColumn;
	}

	/**
	 * @return the toRow
	 */
	public int getToRow() {
		return toRow;
	}

	/**
	 * @param toRow the toRow to set
	 */
	public void setToRow(int toRow) {
		this.toRow = toRow;
	}

	/**
	 * @return the toColumn
	 */
	public int getToColumn() {
		return toColumn;
	}

	/**
	 * @param toColumn the toColumn to set
	 */
	public void setToColumn(int toColumn) {
		this.toColumn = toColumn;
	}
	
	

}
