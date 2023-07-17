import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.util.ArrayList;
import java.util.List;

public class Player {
	ArrayList<Cell> cells;
	
	public Player() {
		cells = new ArrayList<>();
	}
	
	public void start(int row, int col) {
		cells.clear();
		
		cells.add( new Cell(row, col) );
	}
	
	public void makeUniqueOrigin(Color[][] grid, Player other) {
		if ( cells == null || other.cells == null)
			return;
		
		if ( cells.isEmpty() || other.cells.isEmpty() )
			return;
		
		Cell one = cells.get(0);
		Cell two = other.cells.get(0);
		
		while ( grid[one.row][one.col].equals(grid[two.row][two.col]) )
			grid[one.row][one.col] = GamePanel.createColor();
	}
	
	public boolean contains(int row, int col) {
		return cells.contains( new Cell(row, col) );
	}
	
	/** Searches the grid of colors for a given color
	  * around the existing cells of this object. */
	public void search(Color[][] grid, Color color) {
		boolean stop = false;
		
		while ( !stop ) {
			stop = true;
			
			for (int i = 0; i < cells.size(); i++) {
				Cell cell = cells.get(i);
				
				Cell top = new Cell(cell.row - 1, cell.col);
				Cell left = new Cell(cell.row, cell.col - 1);
				Cell bottom = new Cell(cell.row + 1, cell.col);
				Cell right = new Cell(cell.row, cell.col + 1);
				
				// top
				if ( hasColor(grid, top, color) ) {
					cells.add(top);
					stop = false;
				}
				
				// left
				if (  hasColor(grid, left, color) ) {
					cells.add(left);
					stop = false;
				}
				
				// bottom
				if ( hasColor(grid, bottom, color) ) {
					cells.add(bottom);
					stop = false;
				}
				
				// right
				if ( hasColor(grid, right, color) ) {
					cells.add(right);
					stop = false;
				}
			}
		}
	}
	
	public boolean hasColor(Color[][] grid, Cell cell, Color color) {
		if ( !GamePanel.isValid(cell.row, cell.col) )
			return false;
		
		if ( contains(cell.row, cell.col) )
			return false;
		
		return grid[cell.row][cell.col].equals(color);
	}
	
	public void fill(Color[][] grid, Color color) {
		for (Cell cell : cells) {
			grid[cell.row][cell.col] = color;
		}
	}
	
	public void paintBorders(Graphics2D g2) {
		int cellSize = GamePanel.cellSize;
		
		g2.setStroke( new BasicStroke(2) );
		
		for (Cell cell : cells) {
			int r = cell.row;
			int c = cell.col;
			
			int x = c * cellSize;
			int y = r * cellSize;
			
			// top
			if ( !contains(r - 1, c) )
				g2.drawLine(x, y, x + cellSize, y);
			
			// left
			if ( !contains(r, c - 1) )
				g2.drawLine(x, y, x, y + cellSize);
			
			// bottom
			if ( !contains(r + 1, c) )
				g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
			
			// right
			if ( !contains(r, c + 1) )
				g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
		}
	}
}

