package graphics;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

import util.GBC;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	
	
	public ControlPanel(App app) {
		this.app = app;
		setLayout(new GridBagLayout());
		
		int fps = 30;
		animator = new Timer(1000/fps, this::doPlay);
		
		// make components
		
		playButton = new JButton("Play");
		playButton.addActionListener(this::togglePlay);
		
		stepButton = new JButton("Step");
		stepButton.addActionListener(this::doStep);
		
		JButton scatterButton = new JButton("Scatter");
		scatterButton.addActionListener((e) -> {
			app.sim.scatter();
			app.simPanel.repaint();
		});
		
		int maxSpeed = 20;
		int sliderHeight = 20;
		
		JSlider lvelSlider = new JSlider(-maxSpeed, maxSpeed, maxSpeed/2);
		lvelSlider.addChangeListener((e) -> app.sim.setLeftWheelVel(lvelSlider.getValue()));
		lvelSlider.setMinimumSize(new Dimension(sliderHeight, 2*maxSpeed));
		lvelSlider.setFocusable(false);
		
		JSlider rvelSlider = new JSlider(-maxSpeed, maxSpeed, maxSpeed/2);
		rvelSlider.setFocusable(false);
		rvelSlider.setMinimumSize(new Dimension(sliderHeight, 2*maxSpeed));
		rvelSlider.addChangeListener((e) -> app.sim.setRightWheelVel(rvelSlider.getValue()));
		
		JPanel nMovesPanel = new JPanel(new GridBagLayout());
		JTextField nMovesField = new JTextField(2);
		FocusListener nMovesListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {}
			@Override
			public void focusLost(FocusEvent e) {
				app.sim.setScatterStepSize(Integer.parseInt(nMovesField.getText()));
			}
		};
		nMovesField.addFocusListener(nMovesListener);
		nMovesField.addActionListener((e) -> app.sim.setScatterStepSize(Integer.parseInt(nMovesField.getText())));
		nMovesField.setMinimumSize(new Dimension(32, 10));
		nMovesField.setText(app.sim.getScatterStepSize()+"");
		GBC.addComp(nMovesPanel::add, 0, 0, new JLabel("Redistribute every "), new GBC());
		GBC.addComp(nMovesPanel::add, 1, 0, nMovesField, new GBC());
		GBC.addComp(nMovesPanel::add, 2, 0, new JLabel(" moves"), new GBC().anchor(GBC.WEST).weight(1, 0));
		
		JPanel metaPanel = new JPanel(new GridBagLayout());
		JSlider dtSlider = new JSlider(1, 200, 100);
		dtSlider.addChangeListener((e) -> app.sim.setDT(dtSlider.getValue() / 100.0));
		
		JSlider fpsSlider = new JSlider(1, 60, fps);
		fpsSlider.addChangeListener((e) -> setFPS(fpsSlider.getValue()));
		
		JCheckBox useKeysBox = new JCheckBox("Use arrow keys");
		useKeysBox.addActionListener((e) -> app.setUseKeys(useKeysBox.isSelected()));
		
		JCheckBox usePointSimBotsBox = new JCheckBox("Use point sim bots");
		usePointSimBotsBox.addActionListener((e) -> app.setPointSimBots(usePointSimBotsBox.isSelected()));
		
		JPanel nBotsPanel = new JPanel(new GridBagLayout());
		JTextField nBotsField = new JTextField(10);
		FocusListener nBotsListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {}
			@Override
			public void focusLost(FocusEvent e) {
				setNumBots(Integer.parseInt(nBotsField.getText()));
			}
		};
		nBotsField.addFocusListener(nBotsListener);
		nBotsField.addActionListener((e) -> setNumBots(Integer.parseInt(nBotsField.getText())));
		nBotsField.setMinimumSize(new Dimension(32, 10));
		nBotsField.setText(app.sim.getNumSimBots() + "");
		GBC.addComp(nBotsPanel::add, 0, 0, new JLabel("Number of Simulated Robots: "), new GBC());
		GBC.addComp(nBotsPanel::add, 1, 0, nBotsField, new GBC());
		
		// add components
		GBC.addComp(this::add, 0, 0, playButton, new GBC().fill(GBC.BOTH).dim(1, 2));
		GBC.addComp(this::add, 0, 2, stepButton, new GBC().fill(GBC.HORIZONTAL));
		GBC.addComp(this::add, 0, 3, scatterButton, new GBC().fill(GBC.HORIZONTAL));
		
		GBC.addComp(this::add, 1, 0, new JLabel("Left wheel"), new GBC());
		GBC.addComp(this::add, 2, 0, lvelSlider, new GBC().fill(GBC.HORIZONTAL));
		GBC.addComp(this::add, 1, 1, new JLabel("Right wheel"), new GBC());
		GBC.addComp(this::add, 2, 1, rvelSlider, new GBC().fill(GBC.HORIZONTAL));
		
		GBC.addComp(this::add, 1, 2, useKeysBox, new GBC().anchor(GBC.WEST).dim(2, 1));
		GBC.addComp(this::add, 1, 3, usePointSimBotsBox, new GBC().anchor(GBC.WEST).dim(2, 1));
		GBC.addComp(this::add, 1, 4, nMovesPanel, new GBC().fill(GBC.BOTH).dim(2, 1));

		
		GBC.addComp(metaPanel::add, 0, 0, new JLabel("dt"), new GBC());
		GBC.addComp(metaPanel::add, 1, 0, dtSlider, new GBC().fill(GBC.HORIZONTAL));
		GBC.addComp(metaPanel::add, 0, 1, new JLabel("fps"), new GBC());
		GBC.addComp(metaPanel::add, 1, 1, fpsSlider, new GBC().fill(GBC.HORIZONTAL));
		GBC.addComp(metaPanel::add, 0, 2, nBotsPanel, new GBC().fill(GBC.BOTH).dim(2, 1));
		GBC.addComp(this::add, 0, 5, metaPanel, new GBC().dim(3, 1).fill(GBC.BOTH));
		
	}
	

	// Fields
	
	private final App app;
	private final Timer animator;
	
	private final JButton playButton, stepButton;
	
	
	// Methods
	
	private void doStep(ActionEvent e) {
		boolean keys = app.useKeys;
		app.setUseKeys(false);
		app.sim.step();
		app.simPanel.repaint();
		app.setUseKeys(keys);
	}
	
	private void doPlay(ActionEvent e) {
		if(!((app.sim.getLeftWheelVel() == 0 && app.sim.getRightWheelVel() == 0) || (app.useKeys && app.lefton == 0 && app.righton == 0))) {
			app.sim.step();
			app.simPanel.repaint();
		}
	}
	
	private void togglePlay(ActionEvent e) {
		if (animator.isRunning()) {
			stop();
			
		} else {
			play();
		}
	}
	
	void play() {
		animator.start();
		playButton.setText("Stop");
		stepButton.setEnabled(false);
	}
	
	void stop() {
		animator.stop();
		playButton.setText("Play");
		stepButton.setEnabled(true);
	}
	
	private void setFPS(int fps) {
		animator.setDelay(1000 / fps);
	}
	
	private void setNumBots(int n) {
		app.sim.setNumSimBots(n);
		app.simPanel.repaint();
	}
}
