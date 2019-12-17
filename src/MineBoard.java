import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MineBoard extends JPanel{
	
	private int[][] board;
	private int[][] mines;
	private boolean ended;
	private boolean won;
	
	public int boardLength;
    private BufferedImage[] img;
    public final int COURT_WIDTH = 300;
    public final int COURT_HEIGHT = 300;
    
	private int cell_size;
	
	private int countOfMines;

	
    
    private JLabel status;
    
    private LinkedList<int[][]> prevMoves;

	public MineBoard(JLabel status, int length, int numMines) {
		
		boardLength = length;
		countOfMines = numMines;
		board = new int[boardLength][boardLength];
		mines = new int[boardLength][boardLength];
		ended = false;
		prevMoves = new LinkedList<int[][]>();
		won = false;
		cell_size = COURT_WIDTH/boardLength;
		
		img = new BufferedImage[12];

		this.setPreferredSize(new Dimension(COURT_WIDTH, COURT_HEIGHT));
		
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				board[i][j] = 10;
			}
		}
		
		
		for (int i = 0; i < 12; i ++) {
			String x = "files/" + i + ".png";
			try {
				img[i] = ImageIO.read(new File(x));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		prevMoves.add(MineBoard.copyArray(board));
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
				if (!ended) {
					
					int x = e.getX();
					int y = e.getY();
					
					int row = x/cell_size;
					int col = y/cell_size;
					
					int[][] prevBoard = MineBoard.copyArray(board);
					
					if (board[row][col] != 10 || board[row][col] != 11) {
						
						if (e.isShiftDown() || e.isMetaDown()) {
							
							if (board[row][col] == 10) board[row][col] = 11;
							else if (board[row][col] == 11) board[row][col] = 10;
							
				        }
						
						else {
							((MineBoard) e.getSource()).reveal(row, col);
						}
						
						prevMoves.add(MineBoard.copyArray(board));
						 
						repaint();
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			
		});
		
		this.status = status;
		
	}
	
	public static int[][] copyArray(int[][] original) {
		
		int[][] newArr = new int[original.length][original.length];
		
		for (int i = 0; i < original.length; i++) {
			for (int j = 0; j < original.length; j++) {
				newArr[i][j] = original[i][j];
			}
		}
		return newArr;
	}
	
	public boolean checkWon() {
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				if (board[i][j] == 10 && mines[i][j] != 9) return false;
			}
		}
		return true;
	}
	
	
	public static boolean arrayEquals(int[][] arr1, int[][] arr2) {
		for (int i = 0; i < arr1.length; i++) {
			for (int j = 0; j < arr1[0].length; j++) {
				if (arr1[i][j] != arr2[i][j]) return false;
			}
		}
		return true;
	}
	
	public void addMines(int row, int col){
		mines[row][col] = 9;
	}
	
	public String checkStatus() {
		return status.getText();
	}
	
	public void reset() {
		for (int row = 0; row < boardLength; row++) {
        	for (int col = 0; col < boardLength; col++) {
        		board[row][col] = 10;
        	}
        }
		ended = false;
		setMines();
		
		status.setText("Running...");
		repaint();
	}
	
	public void undo() {
		
		if (prevMoves.size() >= 2) {
			
			prevMoves.removeLast();
			int[][] newBoard = prevMoves.getLast();
			
			for (int i = 0; i < boardLength; i++) {
				for (int j = 0; j < boardLength; j++) {
					board[i][j] = newBoard[i][j];
				}
			}
		}
		
		else {
			
			for (int i = 0; i < boardLength; i++) {
				for (int j = 0; j < boardLength; j++) {
					board[i][j] = 10;
				}
			}
		}
		
		ended = checkLost();
		won = checkWon();
		if (!ended || !won) status.setText("Running...");
		
		repaint();	
	}
	
	public boolean checkLost() {
				
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				if (board[i][j] == 9) return true;
			}
		}
		return false;
	}
	
	public int getCurrent(int row, int col) {
		return board[row][col];
	}
	
	public void reveal(int row, int col) {
		
		int val = mines[row][col];
		
		if (val == 0) {
			
			int num = this.countSurrounding(row, col);
			
			board[row][col] = num;

			if (num == 0) {
				
				for (int i = Math.max(0, row-1); i <= Math.min(boardLength-1, row+1); i++) {
					for (int j = Math.max(0, col-1); j <= Math.min(boardLength-1, col+1); j++) {
						
						if (board[i][j] == 10) {
							this.reveal(i, j);
						}
					}
				}
			}
			
			won = checkWon();
			if (won) status.setText("You won!!!");
			
		}
		else {
			ended = true;
			board[row][col] = 9;
			status.setText("You lost :(");			
		}
		repaint();
	}
	
	public int getBoardLength() {
		return boardLength;
	}
	
	public int countSurrounding(int row, int col) {
		
		int numMines = 0;
		
		for (int i = Math.max(0, row-1); i <= Math.min(boardLength-1, row+1); i++) {
			for (int j = Math.max(0, col-1); j <= Math.min(boardLength-1, col+1); j++) {
				if (mines[i][j] == 9) numMines++;
			}
		}
		return numMines;
	}
	
	
	public void setMines() {
			
			int mine = countOfMines;
			
			for (int i = 0; i < boardLength; i++) {
				for (int j = 0; j < boardLength; j++) {
					mines[i][j] = 0;
				}
			}
			
			while (mine > 0) {
				int x = (int) (Math.random()*boardLength);
				int y = (int) (Math.random()*boardLength);
				mines[x][y] = 9;
				mine--;
			}
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < boardLength; row++) {
        	for (int col = 0; col < boardLength; col++) {
        		int x = board[row][col];
        		g.drawImage(img[x], row*cell_size, col*cell_size, 
        				cell_size, cell_size, this);
        	}
        }
    }

}
