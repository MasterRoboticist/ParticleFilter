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
		
		JButton scatterButton = new JButton("Scatter");
		scatterButton.addActionListener((e) -> {
			app.sim.scatter();
			app.simPanel.repaint();
		});
		
		// add components
		GBC.addComp(this::add, 0, 0, stepButton, new GBC());
		GBC.addComp(this::add, 0, 20, scatterButton, new GBC());
	}
	
	private final App app;
}
