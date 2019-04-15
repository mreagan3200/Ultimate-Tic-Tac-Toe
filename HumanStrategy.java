import java.util.ArrayList;

public class HumanStrategy implements Strategy {
	private Player p;
	private Display d;
	public HumanStrategy(Display d) {
		this.d = d;
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
		Move m = null;
		do {
			try {
				Thread.sleep(10);
				m = d.getMove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(m == null || !validMoves.contains(m));
		return m;
	}

	@Override
	public void setPlayer(Player p) {
		this.p = p;

	}
	@Override
	public String toString() {
		return this.getClass().toString().substring(6);
	}

}
