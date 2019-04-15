import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Display extends JComponent implements MouseListener {
	private JFrame frame;
	private Game game;
	private final int WIDTH = 450;
	private int row;
	private int col;

	public Display() {
		frame = new JFrame("UTTT");
		frame.setPreferredSize(new Dimension(WIDTH + 10,WIDTH + 35));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.requestFocus();
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(this);
		frame.getContentPane().addMouseListener(this);
		frame.setVisible(true);
		row = -1;
		col = -1;
	}
	public void setGame(Game g) {
		game = g;
	}
	@Override
	public void paintComponent(Graphics g) {
		int turn = game.getTurn();
		if((turn & 1) == 1)
			frame.setTitle("UTTT " + game.getPlayer(2).toString() + " (O)'s turn (turn " + turn + ")");
		else frame.setTitle("UTTT " + game.getPlayer(1).toString() + " (X)'s turn (turn " + turn + ")");
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, WIDTH);
		g.setColor(Color.LIGHT_GRAY);
		Move b = game.getnb();
		if(b.getRow() == -1 && !game.isGameOver())
			g.fillRect(0, 0, WIDTH, WIDTH);
		else g.fillRect(b.getCol() * WIDTH/3, b.getRow() * WIDTH/3, WIDTH/3, WIDTH/3);
		int[][] board = game.getBoard();
		int[][] subs = Game.getSubs(board);
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 3; c++) {
				if(subs[r][c] == 1) {
					g.setColor(Color.CYAN);
					g.fillRect(c * WIDTH/3, r* WIDTH/3, WIDTH/3, WIDTH/3);
				}
				else if(subs[r][c] == -1) {
					g.setColor(Color.PINK);
					g.fillRect(c * WIDTH/3, r* WIDTH/3, WIDTH/3, WIDTH/3);
				}
			}
		}
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
		for(int r = 0; r < 9; r++) {
			for(int c = 0; c < 9; c++) {
				if(board[r][c] == 1) {
					g.setColor(Color.BLUE);
					g.drawString("X", c * WIDTH/9 + 5, r * WIDTH/9 + 43);
				}
				else if(board[r][c] == -1) {
					g.setColor(Color.RED);
					g.drawString("O", c * WIDTH/9 + 5, r * WIDTH/9 + 43);
				}
			}
		}

		g.setColor(Color.BLACK);
		for(int i = 1; i < 9; i++) {
			int temp = WIDTH/9 * i;
			if(i%3==0) {
				g.fillRect(temp - 2, 0, 4, WIDTH);
				g.fillRect(0, temp - 4, WIDTH, 4);
			}
			else {
				g.fillRect(temp-1, 0, 2, WIDTH);
				g.fillRect(0, temp - 1, WIDTH, 2);
			}
		}
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 150));
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 3; c++) {
				if(subs[r][c] == 1) {
					g.setColor(Color.BLUE);
					g.drawString("X", c * WIDTH/3 + 20, r * WIDTH/3 + 128);
				}
				else if(subs[r][c] == -1) {
					g.setColor(Color.RED);
					g.drawString("O", c * WIDTH/3 + 16, r * WIDTH/3 + 130);
				}
			}
		}
		Graphics2D g2d = (Graphics2D) g;
		int temp = Game.checkMain(board);
		if(temp != 0) {
			if(temp == 1)
				g.setColor(Color.BLUE);
			else if(temp == -1)
				g.setColor(Color.RED);
			int[] c = Game.getSums(Game.getSubs(board), 0, 0);
			if(Math.abs(c[0]) == 3) //top horizontal
				g.fillRoundRect(WIDTH/18, WIDTH/7, (WIDTH * 8)/9, 20, 100, 50);
			if(Math.abs(c[1]) == 3) //middle horizontal
				g.fillRoundRect(WIDTH/18, (int) ((WIDTH * 3.35)/7), (WIDTH * 8)/9, 20, 100, 50);
			if(Math.abs(c[2]) == 3) //bottom horizontal
				g.fillRoundRect(WIDTH/18, (int) ((WIDTH * 5.7)/7), (WIDTH * 8)/9, 20, 100, 50);
			if(Math.abs(c[3]) == 3)
				g.fillRoundRect(WIDTH/7, WIDTH/18, 20, (WIDTH * 8) / 9, 50, 100);
			if(Math.abs(c[4]) == 3)
				g.fillRoundRect((int) ((WIDTH * 3.35)/7), WIDTH/18, 20, (WIDTH * 8) / 9, 50, 100);
			if(Math.abs(c[5]) == 3)
				g.fillRoundRect((int) ((WIDTH * 5.7)/7), WIDTH/18, 20, (WIDTH * 8) / 9, 50, 100);
			if(Math.abs(c[6]) == 3) {
				g2d.rotate(Math.toRadians(45));
				g2d.fillRoundRect(27, -7, (int) (WIDTH * 1.3), 20, 100, 50);
				g2d.rotate(Math.toRadians(315));
			}
			if(Math.abs(c[7]) == 3) {
				g2d.translate((WIDTH * 2)/3 + 18, 0);
				g2d.rotate(Math.toRadians(315));
				g2d.fillRoundRect((int) (WIDTH * -1.16), 85, (int) (WIDTH * 1.3), 20, 100, 50);
			}
		}
		repaint();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		row = y/(WIDTH/9);
		col = x/(WIDTH/9);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	public Move getMove() {
		if(row == -1 && col == -1)
			return null;
		else {

			int tempr = row;
			int tempc = col;
			row = -1;
			col = -1;
			return new Move(tempr, tempc);
		}
	}
}
