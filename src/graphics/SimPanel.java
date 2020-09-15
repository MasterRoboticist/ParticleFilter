package graphics;

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

@SuppressWarnings("serial")
public class SimPanel extends JPanel {
	
	public static final int width = 600, height = 600;

	public SimPanel(Simulation sim) {
		setPreferredSize(new Dimension(width, height));
		
		this.sim = sim;
	}
	
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
		renderRobotsAt(g2, loc, size, allBots);
	}
	
	
	
		
	public AffineTransform getTransform(Point loc, Dimension size) {
		double mapw = sim.getMap().img.getWidth(), maph = sim.getMap().img.getHeight();
		double scale = Math.min(size.width / mapw, size.height / maph);
		return new AffineTransform(scale, 0, 0, scale, loc.x, loc.y);
	}
	
	public void renderMapAt(Graphics2D g, Point loc, Dimension size) {
		g.drawImage(sim.getMap().img, getTransform(loc, size), null);
	}
	
	public void renderRobotsAt(Graphics2D g, Point loc, Dimension size, Robot... bots) {
		System.out.println(bots.length);
		g = (Graphics2D) g.create();
		g.transform(getTransform(loc, size));
		for (Robot bot : bots) {
			Point botloc = sim.getMap().robotPos2Pixel(bot.position);
			BufferedImage sprite = bot.getSprite();
			AffineTransform tx = AffineTransform.getRotateInstance(bot.angle, sprite.getWidth()/2, sprite.getHeight()/2);
//			tx.scale(.1, .1); // TODO maybe don't scale
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			var rotatedSprite = op.filter(sprite, null);
			g.drawImage(rotatedSprite, new AffineTransform(.2, 0, 0, .2, botloc.x - rotatedSprite.getWidth()/2, botloc.y - rotatedSprite.getHeight()/2), null);
		}
	}
	
}
