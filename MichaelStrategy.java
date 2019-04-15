import java.util.ArrayList;

public class MichaelStrategy implements Strategy {
	private Player p;
	private int movesAhead;
	private int sign;
	public MichaelStrategy(int moves) {
		movesAhead = moves;
	}

	@Override
	public void setPlayer(Player p) {
		this.p = p;
		sign = this.p.getSign();
	}

	@Override
	public Move getMove(int[][] board) {
		int[][] copy = copy(board);
		if(p.turn() == 0)
			return new Move((int)(Math.random() * 9), (int)(Math.random() * 9));
		//long time = System.currentTimeMillis();
		int[] temp = minimax(movesAhead, sign, copy);
		//System.out.println((double)(System.currentTimeMillis() - time)/1000);
		return new Move(temp[1],temp[2]);
	}
	//returns int[3] = {score, row, col}
	private int[] minimax(int movesAhead, int s, int[][] b) {
		int bestScore = (sign == s) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore;
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		ArrayList<Move> nextMoves = generateMoves(p.getPrev(), b);
		if(nextMoves.isEmpty() || movesAhead == 0) {
			bestScore = evaluate(b);
		}
		else {
			for(Move m : nextMoves) {
				b[m.getRow()][m.getCol()] = s;
				if(sign == s) {
					currentScore = minimax(movesAhead - 1, s*-1, b)[0];
					if(currentScore > bestScore) {
						bestScore = currentScore;
						bestMoves = new ArrayList<Move>();
						bestMoves.add(m);
					}
					else if(currentScore == bestScore)
						bestMoves.add(m);
				}
				else {
					currentScore = minimax(movesAhead - 1, s*-1, b)[0];
					if(currentScore < bestScore) {
						bestScore = currentScore;
						bestMoves = new ArrayList<Move>();
						bestMoves.add(m);
					}
					else if(currentScore == bestScore)
						bestMoves.add(m);
				}
				b[m.getRow()][m.getCol()] = 0;
			}
		}
		if(bestMoves.isEmpty())
			return new int[] {bestScore, -1,-1};
		Move m = bestMoves.get((int)(Math.random() * bestMoves.size()));
		return new int[] {bestScore, m.getRow(), m.getCol()};
	}
	private int evaluate(int[][] b) {
		int[][] subs = Game.getSubs(b);
		int score = 0;
		score += evaluateLine(0, 0, 0, 1, 0, 2, subs);  // row 0
		score += evaluateLine(1, 0, 1, 1, 1, 2, subs);  // row 1
		score += evaluateLine(2, 0, 2, 1, 2, 2, subs);  // row 2
		score += evaluateLine(0, 0, 1, 0, 2, 0, subs);  // col 0
		score += evaluateLine(0, 1, 1, 1, 2, 1, subs);  // col 1
		score += evaluateLine(0, 2, 1, 2, 2, 2, subs);  // col 2
		score += evaluateLine(0, 0, 1, 1, 2, 2, subs);  // diagonal
		score += evaluateLine(0, 2, 1, 1, 2, 0, subs);  // alternate diagonal
		return score;

	}
	private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, int[][] subs) {
		int score = 0;

		// First cell
		if (subs[row1][col1] == sign) {
			score = 1;
		} else if (subs[row1][col1] == sign * -1) {
			score = -1;
		}

		// Second cell
		if (subs[row2][col2] == sign) {
			if (score == 1) {   // cell1 is mySeed
				score = 10;
			} else if (score == -1) {  // cell1 is oppSeed
				return 0;
			} else {  // cell1 is empty
				score = 1;
			}
		} else if (subs[row2][col2] == sign * -1) {
			if (score == -1) { // cell1 is oppSeed
				score = -10;
			} else if (score == 1) { // cell1 is mySeed
				return 0;
			} else {  // cell1 is empty
				score = -1;
			}
		}

		// Third cell
		if (subs[row3][col3] == sign) {
			if (score > 0) {  // cell1 and/or cell2 is mySeed
				score *= 10;
			} else if (score < 0) {  // cell1 and/or cell2 is oppSeed
				return 0;
			} else {  // cell1 and cell2 are empty
				score = 1;
			}
		} else if (subs[row3][col3] == sign * -1) {
			if (score < 0) {  // cell1 and/or cell2 is oppSeed
				score *= 10;
			} else if (score > 1) {  // cell1 and/or cell2 is mySeed
				return 0;
			} else {  // cell1 and cell2 are empty
				score = -1;
			}
		}
		return score;
	}

	private ArrayList<Move> generateMoves(Move prev, int[][] b) {
		ArrayList<Move> validMoves = new ArrayList<Move>();
		for(int r = 0; r < 9; r++) {
			for(int c = 0; c < 9; c++) {
				Move m = new Move(r, c);
				if(Game.isValid(m, prev, b))
					validMoves.add(m);
			}
		}
		return validMoves;
	}
	private int[][] copy(int[][] arr) {
		int[][] copy = new int[arr.length][arr[0].length];
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[0].length; j++) {
				copy[i][j] = arr[i][j];
			}
		}
		return copy;
	}
	@Override
	public String toString() {
		return this.getClass().toString().substring(6);
	}
}
