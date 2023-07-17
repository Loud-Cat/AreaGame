import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.UIManager;

import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.Rectangle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;

public class ProgressPanel extends JPanel {
	Color c1, c2;
	int p1, p2;
	
	public ProgressPanel() {
		super();
		setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		
		setPreferredSize( new Dimension(100, 25) );
		
		p1 = 0;
		p2 = 0;
	}
	
	public Color getColorOne() {
		return c1;
	}
	
	public void setColorOne(Color color) {
		c1 = color;
	}
	
	public Color getColorTwo() {
		return c2;
	}
	
	public void setColorTwo(Color color) {
		c2 = color;
	}
	
	public int getScoreOne() {
		return p1;
	}
	
	public void setScoreOne(int score, int max) {
		p1 = (score * 100) / max;
	}
	
	public int getScoreTwo() {
		return p2;
	}
	
	public void setScoreTwo(int score, int max) {
		p2 = (score * 100) / max;
	}
	
	public void reset() {
		p1 = 0;
		p2 = 0;
		
		c1 = null;
		c2 = null;
	}
	
	public Color getReadableColor(Color color) {
		double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
		return luminance > 0.5 ? Color.BLACK : Color.WHITE;
	}
	
	public static void drawBottomRightText(Graphics2D g2, String text, int x, int y, Color color) {
		// Set the color of the text
		g2.setColor(color);

		// Get the font metrics of the current font
		FontMetrics fontMetrics = g2.getFontMetrics();
		
		// Get the size in pixels the text takes up
		Rectangle rect = (Rectangle) fontMetrics.getStringBounds(text, g2)
			.getBounds();
		
		// Draw the text
		g2.drawString(text, x - rect.width, y);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (c1 == null)
			c2 = Color.BLACK;
		
		if (c2 == null)
			c2 = Color.BLACK;
		
		int width = getWidth();
		int height = getHeight();
		
		int extent = (p1 * width) / 100;
		g2.setColor(c1);
		g2.fillRect(0, 0, extent, height);
		
		int limit = (p2 * width) / 100;
		g2.setColor(c2);
		g2.fillRect(width - limit, 0, limit, height);
		
		g2.setFont( UIManager.getFont("Label.font").deriveFont(18f) );
		g2.setStroke( new BasicStroke(5) );
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		Color left = (p1 < 5) ? Color.BLACK : getReadableColor(c1);
		Color right = (p2 < 5) ? Color.BLACK : getReadableColor(c2);
		
		g2.setColor(left);
		g2.drawString(p1 + "%", 5, height - 5);
		
		drawBottomRightText(g2,
			p2 + "%",
			width - 5, height - 5,
			right);
	}
}

