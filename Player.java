public class Player {
	private Strategy s;
	private Game g;
	private int sign;
	public Player(Strategy s, int sign) {
		this.s = s;
		this.sign = sign;
	}

	public void setGame(Game g) {
		this.g = g;
	}

	public Move getPrev() {
		return g.getPrev();
	}

	public Move getMove(int[][] board) {
		return s.getMove(board);
	}

	public int getSign() {
		return sign;
	}
	public int turn() {
		return g.getTurn();
	}
	@Override
	public String toString() {
		return s.toString();
	}
}
