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
	
//	public static final int width = 600, height = 500;
	public static final String title = "Particle Filter";
	
	public App() {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		
		// make simulation stuff
		BufferedImage mapImage = ImageReader.readImage("logo.jpg");
		
		int robotGridWidth = 1000;
		int nbots = 1000;
		
		map = new Map(mapImage.getWidth(), mapImage.getHeight(), robotGridWidth, mapImage);
		sim = new Simulation(nbots, map);
		sim.setLeftWheelVel(10);
		sim.setRightWheelVel(10);
		
		// make panels
		simPanel = new SimPanel(sim);
		controlPanel = new ControlPanel(this);
		
		// add panels
		GBC.addComp(frame::add, 0, 0, simPanel, new GBC().fill(GBC.BOTH).weight(1, 1));
		GBC.addComp(frame::add, 1, 0, controlPanel, new GBC().fill(GBC.BOTH));
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	final Map map;
	final Simulation sim;
	private final JFrame frame;
	final SimPanel simPanel;
	final ControlPanel controlPanel;
}
