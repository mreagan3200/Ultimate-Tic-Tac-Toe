public class Game {
	private int[][] board;
	private Player p1, p2;
	private Move prev;
	private Move nb;
	private int turn = 0;
	private final int TIE = -100;
	private boolean gameOver = false;
	public Game(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
		p1.setGame(this);
		p2.setGame(this);
		prev = null;
		board = new int[9][9];
		nb = new Move(-1,-1);
	}
	public int play() {
		int winner = 0;
		while(true) {
			if(!hasMoves())
				break;
			Move m = p1.getMove(Game.copy(board));
			if(Game.isValid(m, prev, board))
				makeMove(m, 1);
			else throw new RuntimeException("invalid move");
			int state = Game.checkMain(board);
			if(state != 0 && state != TIE) {
				winner = 1;
				break;
			}
			turn++;
			if(!hasMoves())
				break;
			m = p2.getMove(Game.copy(board));
			if(Game.isValid(m, prev, board))
				makeMove(p2.getMove(board), -1);
			else throw new RuntimeException("invalid move");
			state = Game.checkMain(board);
			if(state != 0 && state != TIE) {
				winner = -1;
				break;
			}
			turn++;
		}
		gameOver = true;
		return winner;
	}
	public boolean isGameOver() {
		return gameOver;
	}
	private void makeMove(Move move, int player) {
		board[move.getRow()][move.getCol()] = player;
		if(Game.getSubs(board)[move.getRow()%3][move.getCol()%3] == 0)
			nb = new Move(move.getRow()%3, move.getCol()%3);
		else nb = new Move(-1,-1);
		prev = move;
	}
	private boolean hasMoves() {
		for(int r = 0; r < 9; r++) {
			for(int c = 0; c < 9; c++) {
				Move m = new Move(r, c);
				if(Game.isValid(m, prev, board))
					return true;
			}
		}
		return false;
	}

	public Move getPrev() {
		return prev;
	}

	public int[][] getBoard() {
		return Game.copy(board);
	}
	public Move getnb() {
		return nb;
	}
	public int getTurn() {
		return turn;
	}
	public Player getPlayer(int i) {
		switch(i) {
		case 1:
			return p1;
		case 2:
			return p2;
		default:
			return null;
		}
	}
	public static boolean isValid(Move m, Move prev, int[][] b) {
		if(prev == null)
			return b[m.getRow()][m.getCol()] == 0;
		int nbr = prev.getRow() % 3, nbc = prev.getCol() % 3;
		int br = m.getRow() / 3, bc = m.getCol() / 3;
		int[][] subs = Game.getSubs(b);
		if(subs[nbr][nbc] == 0) {
			if(Game.isInBoard(m, nbr, nbc))
				return b[m.getRow()][m.getCol()] == 0;
			else return false;
		}
		else {
			return subs[br][bc] == 0 && b[m.getRow()][m.getCol()] == 0;
		}
	}
	public static int[][] copy(int[][] b) {
		int[][] temp = new int[9][9];
		for(int r = 0; r < 9; r++) {
			for(int c = 0; c < 9; c++) {
				temp[r][c] = b[r][c];
			}
		}
		return temp;
	}
	public static int checkMain(int[][] b) {
		return Game.checkSub(Game.getSubs(b), 0, 0);
	}
	public static int[][] getSubs(int[][] b) {
		int[][] bb = new int[3][3];
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 3; c++) {
				bb[r][c] = Game.checkSub(b, r, c);
			}
		}
		return bb;
	}
	public static int checkSub(int[][] b, int br, int bc) {
		int[] c = new int[8];
		c[0] = b[3*br][3*bc] + b[3*br][3*bc + 1] + b[3*br][3*bc + 2];
		c[1] = b[3*br + 1][3*bc] + b[3*br + 1][3*bc + 1] + b[3*br + 1][3*bc + 2];
		c[2] = b[3*br + 2][3*bc] + b[3*br + 2][3*bc + 1] + b[3*br + 2][3*bc + 2];
		c[3] = b[3*br][3*bc] + b[3*br + 1][3*bc] + b[3*br + 2][3*bc];
		c[4] = b[3*br][3*bc + 1] + b[3*br + 1][3*bc + 1] + b[3*br + 2][3*bc + 1];
		c[5] = b[3*br][3*bc + 2] + b[3*br + 1][3*bc + 2] + b[3*br + 2][3*bc + 2];
		c[6] = b[3*br][3*bc] + b[3*br + 1][3*bc + 1] + b[3*br + 2][3*bc + 2];
		c[7] = b[3*br][3*bc + 2] + b[3*br + 1][3*bc + 1] + b[3*br + 2][3*bc];
		for(int i = 0; i < c.length; i++) {
			if(c[i] == 3)
				return 1;
			else if(c[i] == -3)
				return -1;
		}
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				if(b[row][col] == 0)
					return 0;
			}
		}
		return -100;
	}
	public static int[] getSums(int[][] b, int br, int bc) {
		int[] c = new int[8];
		c[0] = b[3*br][3*bc] + b[3*br][3*bc + 1] + b[3*br][3*bc + 2]; //top horizontal
		c[1] = b[3*br + 1][3*bc] + b[3*br + 1][3*bc + 1] + b[3*br + 1][3*bc + 2]; //middle horizontal
		c[2] = b[3*br + 2][3*bc] + b[3*br + 2][3*bc + 1] + b[3*br + 2][3*bc + 2]; //bottom horizontal
		c[3] = b[3*br][3*bc] + b[3*br + 1][3*bc] + b[3*br + 2][3*bc]; //first column
		c[4] = b[3*br][3*bc + 1] + b[3*br + 1][3*bc + 1] + b[3*br + 2][3*bc + 1]; //second column
		c[5] = b[3*br][3*bc + 2] + b[3*br + 1][3*bc + 2] + b[3*br + 2][3*bc + 2]; //third column
		c[6] = b[3*br][3*bc] + b[3*br + 1][3*bc + 1] + b[3*br + 2][3*bc + 2]; // diagonal \
		c[7] = b[3*br][3*bc + 2] + b[3*br + 1][3*bc + 1] + b[3*br + 2][3*bc]; // diagonal /
		return c;
	}

	public static boolean isInBoard(Move m, int br, int bc) {
		return m.getRow() >= br*3 && m.getRow() < (br + 1)*3 && m.getCol() >= bc*3 && m.getCol() < (bc + 1)*3;
	}

	public static void test(Strategy s1, Strategy s2, int games) {
		int c1 = 0, c2 = 0;
		for(int i = 0; i < games; i++) {
			if(Math.random() < 0.5) {
				Player p1 = new Player(s1,  1);
				Player p2 = new Player(s2, -1);
				s1.setPlayer(p1);
				s2.setPlayer(p2);
				int temp = new Game(p1, p2).play();
				if(temp == 1)
					c1++;
				else if(temp == -1)
					c2++;
			}
			else {
				Player p1 = new Player(s1,  1);
				Player p2 = new Player(s2, -1);
				s1.setPlayer(p1);
				s2.setPlayer(p2);
				int temp = new Game(p2, p1).play();
				if(temp == 1)
					c2++;
				else if(temp == -1)
					c1++;
			}
		}
		System.out.println(s1.toString() + ": " + c1 + ", " + s2.toString() + ": " + c2);
	}

	public static void display() {
		Display d = new Display();
		Strategy s1 = new HumanStrategy(d);
		Strategy s2 = new HumanStrategy(d);
		Player p1 = new Player(s1,  1);
		Player p2 = new Player(s2, -1);
		s1.setPlayer(p1);
		s2.setPlayer(p2);
		Game g = new Game(p1, p2);
		d.setGame(g);
		System.out.println(g.play());
	}
	public static void main(String[] args) {
		//		test(new MichaelStrategy(3), new RandomStrategy(), 10);
		display();
	}
}
