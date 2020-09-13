package graphics;

import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import simulation.Map;
import simulation.Simulation;
import util.GBC;
import util.ImageReader;

public class App {
	
	public static void main(String[] args) {
		new App();
	}
	
	public static final int width = 600, height = 500;
	
	public App() {
		frame = new JFrame("Particle Filter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		
		// make simulation stuff
		BufferedImage mapImage = ImageReader.readImage("logo.png");
		
		int robotGridWidth = 1000;
		int nbots = 100;
		
		Map map = new Map(mapImage.getWidth(), mapImage.getHeight(), robotGridWidth, mapImage);
		Simulation sim = new Simulation(nbots, map);
		
		// make components
		simPanel = new SimPanel(sim);
		
		// add components
		GBC.addComp(frame::add, 0, 0, simPanel, new GBC().fill(GBC.BOTH).weight(1, 1));
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private final JFrame frame;
	private final SimPanel simPanel;
}
