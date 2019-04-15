public interface Strategy {
	Move getMove(int[][] board);
	void setPlayer(Player p);
}
