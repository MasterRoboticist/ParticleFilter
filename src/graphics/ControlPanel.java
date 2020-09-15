package graphics;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import util.GBC;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	public ControlPanel(App app) {
		this.app = app;
		setLayout(new GridBagLayout());
		
		// make components
		
		JButton stepButton = new JButton("Step");
		stepButton.addActionListener((e) -> {
			app.sim.step();
			app.simPanel.repaint();
		});
		
		// add components
		GBC.addComp(this::add, 0, 0, stepButton, new GBC());
	}
	
	private final App app;
}
