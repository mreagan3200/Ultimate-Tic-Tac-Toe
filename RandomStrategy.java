import java.util.ArrayList;

public class RandomStrategy implements Strategy {
	private Player p;
	public RandomStrategy() {

	}

	@Override
	public void setPlayer(Player p) {
		this.p = p;
	}

	@Override
	public Move getMove(int[][] board) {
		ArrayList<Move> validMoves = new ArrayList<Move>();
		for(int r = 0; r < 9; r++) {
			for(int c = 0; c < 9; c++) {
				Move m = new Move(r, c);
				if(Game.isValid(m, p.getPrev(), board))
					validMoves.add(m);
			}
		}
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return validMoves.get((int)(Math.random() * validMoves.size()));
	}

	@Override
	public String toString() {
		return this.getClass().toString().substring(6);
	}

}
