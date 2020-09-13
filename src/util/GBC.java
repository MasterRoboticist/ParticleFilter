package util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.Border;

public class GBC extends GridBagConstraints {
	private static final long serialVersionUID = 7175900945097785698L;
	
	@FunctionalInterface
	public static interface Addable {
		void add(Component comp, Object constraints);
	}

	
	// static methods
	
	/**
	 * Returns a JPanel containing just the given component and with the given border.
	 * @param comp component
	 * @param border a border
	 * @return the panel
	 */
	public static JPanel withBorder(Component comp, Border border) {
		JPanel panel = new JPanel(new GridLayout());
		panel.setBorder(border);
		panel.add(comp);
		return panel;
	}
	
	public static void addComp(Addable base, int gbx, int gby, Component comp, GBC constraints) {
		base.add(comp, constraints.index(gbx, gby));
	}
	
	public static void addComp(Addable base, int gbx, int gby, Component comp, GBC constraints, Border border) {
		base.add(withBorder(comp, border), constraints.index(gbx, gby));
	}

	
	
	// Constructors
	
	public GBC(int gbx, int gby) {
		super();
		index(gbx, gby);
	}
	public GBC() {
		this(0, 0);
	}
	
	public GBC index(int gbx, int gby) {
		this.gridx = gbx;
		this.gridy = gby;
		return this;
	}
	
	public GBC dim(int gbwidth, int gbheight) {
		this.gridwidth = gbwidth;
		this.gridheight = gbheight;
		return this;
	}
	
	public GBC anchor(int anchor) {
		this.anchor = anchor;
		return this;
	}
	
	public GBC fill(int fill) {
		this.fill = fill;
		return this;
	}
	
	public GBC weight(double wx, double wy) {
		this.weightx = wx;
		this.weighty = wy;
		return this;
	}
	
	public GBC insets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
		return this;
	}
	
}
