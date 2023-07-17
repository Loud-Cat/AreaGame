import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class AreaGame extends JFrame {
	ProgressPanel progress;
	GamePanel game;
	
	public AreaGame() {
		super("Area");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel container = new JPanel();
		container.setLayout( new BoxLayout(container, BoxLayout.Y_AXIS) );
		container.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) );
		
		progress = new ProgressPanel();
		container.add(progress);
		container.add( Box.createRigidArea(new Dimension(0, 5)) );
		
		game = new GamePanel();
		game.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		container.add(game);
		
		game.setProgressPanel(progress);
		
		JPanel buttonPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		buttonPanel.setLayout(layout);
		container.add(buttonPanel);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets.top = 5;
		
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridwidth = 3;
		c.gridy = 0;
		buttonPanel.add(game.turnLabel, c);
		
		c.gridwidth = 1;
		c.gridy = 1;
		c.gridx = -1;
		
		buttonPanel.add(game.colorPreviews[0], c);
		buttonPanel.add(game.colorPreviews[1], c);
		buttonPanel.add(game.colorPreviews[2], c);
		
		c.gridy = 2;
		
		buttonPanel.add(game.colorButtons[0], c);
		buttonPanel.add(game.colorButtons[1], c);
		buttonPanel.add(game.colorButtons[2], c);
		
		add(container);
		pack();
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName()
			);
		}
		catch (Exception e) {
			System.out.println("Warning: could not set Look and Feel");
		}
		
		SwingUtilities.invokeLater(() -> {
			AreaGame window = new AreaGame();
			window.setVisible(true);
		});
	}
}

