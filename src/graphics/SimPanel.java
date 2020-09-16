package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import robot.Robot;
import simulation.Simulation;
import util.ArrayIter;

@SuppressWarnings("serial")
public class SimPanel extends JPanel {
	
	public static final int width = 600, height = 600;

	public SimPanel(Simulation sim) {
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		
		this.sim = sim;
	}
	
	private boolean lazyDraw = true; // controls whether simbots are drawn as images or points
	private final Simulation sim;
	
	@Override
	public void paintComponent(Graphics g) {
		var g2 = (Graphics2D) g;
		Point loc = new Point();
		Dimension size = getSize();
		
		Robot[] allBots = new Robot[sim.getNumSimBots() + 1];
		sim.getSimRobots().toArray(allBots);
		allBots[allBots.length-1] =  sim.getRealRobot();
		
		renderMapAt(g2, loc, size);
		renderRobotsAt(g2, loc, size, new ArrayIter<>(sim.getRealRobot()));
		if (lazyDraw) {
			lazyRenderRobotsAt(g2, loc, size, sim.getSimRobots());
		} else {
			renderRobotsAt(g2, loc, size, sim.getSimRobots());
		}
	}
	
	
	
		
	public AffineTransform getTransform(Point loc, Dimension size) {
		double mapw = sim.getMap().img.getWidth(), maph = sim.getMap().img.getHeight();
		double scale = Math.min(size.width / mapw, size.height / maph);
		return new AffineTransform(scale, 0, 0, scale, loc.x, loc.y);
	}
	
	public void renderMapAt(Graphics2D g, Point loc, Dimension size) {
		g.drawImage(sim.getMap().img, getTransform(loc, size), null);
	}
	
	public void lazyRenderRobotsAt(Graphics2D g, Point loc, Dimension size, Iterable<Robot> bots) {
		g = (Graphics2D) g.create();
		g.transform(getTransform(loc, size));
		g.setColor(Color.GRAY);
		int diam = 9;
		for (Robot bot : bots) {
			Point botloc = sim.getMap().robotPos2Pixel(bot.position);
			g.fillOval(botloc.x - diam/2, botloc.y - diam/2, diam, diam);
		}
	}
	
	public void renderRobotsAt(Graphics2D g, Point loc, Dimension size, Iterable<Robot> bots) {
		g = (Graphics2D) g.create();
		g.transform(getTransform(loc, size));
		for (Robot bot : bots) {
			Point botloc = sim.getMap().robotPos2Pixel(bot.position);
			BufferedImage sprite = bot.getSprite();
			double spriteOrientation = 3*Math.PI/2;
			AffineTransform tx = AffineTransform.getRotateInstance(bot.angle + spriteOrientation, sprite.getWidth()/2, sprite.getHeight()/2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			var rotatedSprite = op.filter(sprite, null);
			double desiredBotSize = size.getWidth() / 40.0; // not quite right name
			double scale = 1.0 * sprite.getWidth() / sim.getMap().img.getWidth() * desiredBotSize;
			g.drawImage(rotatedSprite, new AffineTransform(1/scale, 0, 0, 1/scale, botloc.x - rotatedSprite.getWidth()/scale/2, botloc.y - rotatedSprite.getHeight()/scale/2), null);
		}
	}
	
}
