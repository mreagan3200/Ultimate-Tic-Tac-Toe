public class Move {
	public int row, col;
	public Move(int row, int col) {
		this.row = row;
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	@Override
	public boolean equals(Object other) {
		Move o = (Move)other;
		return row == o.getRow() && col == o.getCol();
	}
}
