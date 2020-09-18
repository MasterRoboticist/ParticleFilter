package graphics;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import simulation.Map;
import simulation.Simulation;
import util.GBC;
import util.ImageReader;

@SuppressWarnings("serial")
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
		BufferedImage mapImage = ImageReader.readImage("EndSlide.jpg");
		
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
		GBC.addComp(frame::add, 0, 0, simPanel, new GBC().fill(GBC.BOTH).weight(1, 1).dim(1, 2));
		GBC.addComp(frame::add, 1, 0, controlPanel, new GBC().fill(GBC.BOTH).anchor(GBC.NORTHWEST));
		
		addKeyBindings(simPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	// fields
	
	final Map map;
	final Simulation sim;
	private final JFrame frame;
	final SimPanel simPanel;
	final ControlPanel controlPanel;
	
	boolean useKeys = false;
	
	
	// methods
	
	public void setUseKeys(boolean b) {
		useKeys = b;
		handleDirs();
		
//		TODO make it play when a key is pressed and stop when key is released
		if (b) controlPanel.play();
		else controlPanel.stop();
	}
	
	private final String[] keys = {"LEFT", "UP", "RIGHT", "DOWN"};
	private final boolean[] dirs = new boolean[4];
	
	private void addKeyBindings(JComponent comp) {
		for (int i = 0; i < keys.length; i++)
			addToggleMap(comp, i);
	}
	
	private void addToggleMap(JComponent comp, int toggledir) {
		var keyStroke = keys[toggledir];
		comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyStroke), keyStroke + " on");
		comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released " + keyStroke), keyStroke + " off");
		comp.getActionMap().put(keyStroke + " on", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dirs[toggledir] = true;
				handleDirs();
			}
		});
		comp.getActionMap().put(keyStroke + " off", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dirs[toggledir] = false;
				handleDirs();
			}
		});
	}
	
	private void handleDirs() {
		if (!useKeys) {
			sim.setLeftWheelOn(1);
			sim.setRightWheelOn(1);
			return;
		}
		
		double lefton = 0;
		double righton = 0;
		
		double turnspd = .3;
		
		if (dirs[0]) {
			lefton -= turnspd;
			righton += turnspd;
		}
		if (dirs[1]) {
			lefton++;
			righton++;
		}
		if (dirs[2]) {
			lefton += turnspd;
			righton -= turnspd;
		}
		if (dirs[3]) {
			lefton--;
			righton--;
		}
				
		sim.setLeftWheelOn(lefton);
		sim.setRightWheelOn(righton);
	}

	public void setPointSimBots(boolean b) {
		simPanel.setLazyDraw(b);
		simPanel.repaint();
	}

}
