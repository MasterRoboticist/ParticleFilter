package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.ImageReader;

@SuppressWarnings("serial")
public class Display {
	
	public static final int width = 600, height = 500;
	
	public Display() {
		frame = new JFrame("Particle Filter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		image = ImageReader.readImage("logo.png");
		
		panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};
		panel.setPreferredSize(new Dimension(width, height));
		frame.add(panel);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private final JFrame frame;
	private final JPanel panel;
	private final BufferedImage image;
}
