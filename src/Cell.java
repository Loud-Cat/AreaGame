
public class Cell {
	int row, col;
	
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cell other)
			return other.row == row && other.col == col;
		return false;
	}
}
