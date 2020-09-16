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
		g = (Graphics2D) g.create();
		g.transform(getTransform(loc, size));
		for (Robot bot : bots) {
			Point botloc = sim.getMap().robotPos2Pixel(bot.position);
			BufferedImage sprite = bot.getSprite();
			double spriteOrientation = 3*Math.PI/2;
			AffineTransform tx = AffineTransform.getRotateInstance(bot.angle + spriteOrientation, sprite.getWidth()/2, sprite.getHeight()/2);
//			tx.scale(.1, .1); // TODO maybe don't scale
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			var rotatedSprite = op.filter(sprite, null);
			double desiredBotSize = size.getWidth()/20;
			double scale = size.getWidth()/sim.getMap().img.getWidth()*desiredBotSize;
			g.drawImage(rotatedSprite, new AffineTransform(1/scale, 0, 0, 1/scale, botloc.x - rotatedSprite.getWidth()/scale/2, botloc.y - rotatedSprite.getHeight()/scale/2), null);
			//changed above line to below line because robots weren't getting near edges and this seemed to be the problem
			//TODO: why do you have to divide by 10?
//			g.drawImage(rotatedSprite, new AffineTransform(.2, 0, 0, .2, botloc.x, botloc.y), null);
		}
	}
	
}
