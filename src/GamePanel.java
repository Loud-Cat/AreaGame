import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel {
	static int gameWidth = 600;
	static int gameHeight = 600;
	
	static int rows = 30;
	static int cols = rows;
	static int max = rows * cols;
	static int cellSize = gameWidth / rows;
	
	JPanel buttonWrapper;
	JButton startButton;
	
	JLabel turnLabel;
	JPanel[] colorPreviews;
	JButton[] colorButtons;
	
	static final Color RED = Color.RED;
	static final Color YELLOW = Color.YELLOW;
	static final Color GREEN = Color.GREEN;
	static final Color BLUE = Color.BLUE.brighter();
	static final Color PURPLE = Color.MAGENTA.darker();
	static final Color NONE = Color.LIGHT_GRAY;
	
	final int NOT_PLAYING = 0;
	final int PLAYER_ONE = 1;
	final int PLAYER_TWO = 2;
	
	int currentPlayer = NOT_PLAYING;
	
	Color[][] grid;
	Player playerOne, playerTwo;
	ProgressPanel progress;
	
	public GamePanel() {
		super();
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		setSize(gameWidth, gameHeight);
		setPreferredSize( new Dimension(gameWidth, gameHeight) );
		
		startButton = createButton();
		startButton.setText("START");
		startButton.setPreferredSize( new Dimension(100, 50) );
		
		buttonWrapper = new JPanel();
		buttonWrapper.setBorder( BorderFactory.createEmptyBorder(0, 0, 10, 0) );
		buttonWrapper.setOpaque(false);
		
		startButton.addActionListener(evt -> {
			start();
			progress.reset();
			progress.repaint();
		});
		
		buttonWrapper.add(startButton);
		add(buttonWrapper, BorderLayout.SOUTH);
		
		turnLabel = new JLabel("Click START to begin");
		turnLabel.setFont( turnLabel.getFont().deriveFont(16f) );
		
		colorPreviews = new JPanel[] { createPanel(), createPanel(), createPanel() };
		colorButtons = new JButton[] { createButton(), createButton(), createButton() };
		
		for (int i = 0; i < 3; i++) {
			final int index = i;
			colorButtons[i].addActionListener(evt -> buttonAction(index));
		}
		
		colorPreviews[0].setBackground(RED);
		colorPreviews[1].setBackground(BLUE);
		colorPreviews[2].setBackground(PURPLE);
		
		colorButtons[0].setText("RED");
		colorButtons[1].setText("BLUE");
		colorButtons[2].setText("PURPLE");
		
		playerOne = new Player();
		playerTwo = new Player();
		reset();
	}
	
	public void setProgressPanel(ProgressPanel panel) {
		progress = panel;
	}
	
	public static boolean isValid(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}
	
	public JPanel createPanel() {
		JPanel out = new JPanel();
		out.setPreferredSize( new Dimension(50, 50) );
		out.setOpaque(true);
		
		return out;
	}
	
	public JButton createButton() {
		JButton out = new JButton("Color");
		out.setFont( out.getFont().deriveFont(20f) );
		
		return out;
	}
	
	public static Color createColor() {
		double v = Math.random();
		double fifth = 1.0 / 5;
		
		if (v < 1 * fifth)
			return RED;
		else if (v < 2 * fifth)
			return YELLOW;
		else if (v < 3 * fifth)
			return GREEN;
		else if (v < 4 * fifth)
			return BLUE;
		return PURPLE;
	}
	
	public void reset() {
		grid = new Color[rows][cols];
		
		for (int row = 0; row < rows; row++) {
			grid[row] = new Color[cols];
			Arrays.fill(grid[row], NONE);
		}
	}
	
	public void start() {
		buttonWrapper.setVisible(false);
		
		currentPlayer = PLAYER_ONE;
		turnLabel.setText("It is now Player 1's turn");
		
		for (Color[] row : grid) {
			for (int c = 0; c < cols; c++)
				row[c] = createColor();
		}
		
		
		playerOne.start(rows - 1, 0);
		playerTwo.start(0, cols - 1);
		
		playerOne.makeUniqueOrigin(grid, playerTwo);
		
		progress.setColorOne(grid[rows - 1][0]);
		progress.setColorTwo(grid[0][cols - 1]);
		updateColors();
		
		repaint();
		
		for (JButton button : colorButtons)
			button.setEnabled(true);
	}
	
	public void win(int player) {
		switch(player) {
			case PLAYER_ONE:
				turnLabel.setText("Player 1 has won!");
				break;
			case PLAYER_TWO:
				turnLabel.setText("Player 2 has won!");
				break;
			default:
				return;
		}
		
		for (JButton button : colorButtons)
			button.setEnabled(false);
		
		buttonWrapper.setVisible(true);
	}
	
	public void buttonAction(int btn) {
		Player player, opponent;
		Color selected = colorPreviews[btn].getBackground();
		
		if (currentPlayer == PLAYER_ONE) {
			player = playerOne;
			opponent = playerTwo;
			currentPlayer = PLAYER_TWO;
			
			turnLabel.setText("It is now Player 2's turn");
			
			if ( progress != null )
				progress.setColorOne(selected);
		}
		else if (currentPlayer == PLAYER_TWO) {
			player = playerTwo;
			opponent = playerOne;
			currentPlayer = PLAYER_ONE;
			
			turnLabel.setText("It is now Player 1's turn");
			
			if ( progress != null)
				progress.setColorTwo(selected);
		}
		else
			return;
		
		player.search(grid, selected);
		player.fill(grid, selected);
		
		repaint();
		updateColors();
		
		if (progress != null) {
			progress.setScoreOne( playerOne.cells.size(), max );
			progress.setScoreTwo( playerTwo.cells.size(), max );
			progress.repaint();
		}
		
		if ( progress.getScoreOne() >= 50)
			win(PLAYER_ONE);
		else if ( progress.getScoreTwo() >= 50)
			win(PLAYER_TWO);
	}
	
	public void updateColors() {
		List<Color> available = new ArrayList<>( List.of(RED, YELLOW, GREEN, BLUE, PURPLE) );
		
		available.remove( progress.c1 );
		available.remove( progress.c2 );
		
		for (int i = 0; i < 3; i++) {
			Color color = available.get(i);
			colorPreviews[i].setBackground(color);
			colorButtons[i].setText( getColorName(color) );
		}
	}
	
	public String getColorName(Color color) {
		if ( color.equals(RED) )
			return "RED";
		else if ( color.equals(BLUE) )
			return "BLUE";
		else if ( color.equals(PURPLE) )
			return "PURPLE";
		else if ( color.equals(YELLOW) )
			return "YELLOW";
		else if ( color.equals(GREEN) )
			return "GREEN";
		else
			return "NONE";
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int x = c * cellSize;
				int y = r * cellSize;
				
				g2.setColor(grid[r][c]);
				g2.fillRect(x, y, cellSize, cellSize);
				
				g2.setColor(Color.BLACK);
				
				if (currentPlayer == NOT_PLAYING) {
					g2.drawRect(x, y, cellSize, cellSize);
				}
			}
		}
		
		playerOne.paintBorders(g2);
		playerTwo.paintBorders(g2);
	}
}

