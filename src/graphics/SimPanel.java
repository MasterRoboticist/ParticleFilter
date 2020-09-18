package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import robot.Robot;
import simulation.Simulation;
import util.ArrayIter;
import util.Util;

@SuppressWarnings("serial")
public class SimPanel extends JPanel implements MouseListener, MouseMotionListener {
	
	public static final int width = 600, height = 600;

	public SimPanel(Simulation sim) {
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.sim = sim;
	}
	
	private boolean lazyDraw = false; // controls whether simbots are drawn as images or points
	private final Simulation sim;
	
	@Override
	public void paintComponent(Graphics g) {
		var g2 = (Graphics2D) g;
		Point loc = new Point();
		Dimension size = getSize();
		lastLoc.setLocation(loc);
		lastSize.setSize(size);
		
		renderMapAt(g2, loc, size);
		if (lazyDraw) {
			renderRobotsAt(g2, loc, size, new ArrayIter<>(sim.getRealRobot()));
			lazyRenderRobotsAt(g2, loc, size, sim.getSimRobots());
		} else {
			renderRobotsAt(g2, loc, size, sim.getSimRobots());
			renderRobotsAt(g2, loc, size, new ArrayIter<>(sim.getRealRobot()));
		}
	}
	
	
	private final Point lastLoc = new Point();
	private final Dimension lastSize = new Dimension();
		
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
		
		double desiredBotSize = getSpriteWidth();
		double scale = 1.0 * sim.getSpriteWidth() / sim.getMap().img.getWidth() * desiredBotSize;
		
		for (Robot bot : bots) {
			Point botloc = getBotPixel(bot);
			BufferedImage sprite = bot.getSprite();
			g.drawImage(sprite, new AffineTransform(1/scale, 0, 0, 1/scale, botloc.x - sprite.getWidth()/scale/2, botloc.y - sprite.getHeight()/scale/2), null);
		
		}
	}
	
	public void setLazyDraw(Boolean b) {
		lazyDraw = b;
	}
	
	private Point getBotPixel(Robot bot) {
		return sim.getMap().robotPos2Pixel(bot.position);
	}
	
	private double getSpriteWidth() {
		return sim.getSpriteWidth() / 10.0;
	}



	// dragging robot stuff
	private boolean isDragging = false;
//	private final Point dragPos = new Point();
	
	private void setBotLoc(Robot r, Point pix) {
		var tf = getTransform(lastLoc, lastSize);
		try {
			tf.inverseTransform(pix, pix);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		r.setPosition(sim.getMap().pixel2RobotPos(pix));
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) { }




	@Override
	public void mousePressed(MouseEvent e) {
		var tf = getTransform(lastLoc, lastSize);
		Point botloc = getBotPixel(sim.getRealRobot());
		tf.transform(botloc, botloc);
		Point clickLoc = e.getPoint();
		if (Util.dist2(botloc, clickLoc) < Math.pow(getSpriteWidth(), 2)) {
			isDragging = true;
			setBotLoc(sim.getRealRobot(), clickLoc);
		}
	}




	@Override
	public void mouseReleased(MouseEvent e) {
		isDragging = false;
	}




	@Override
	public void mouseEntered(MouseEvent e) { }




	@Override
	public void mouseExited(MouseEvent e) {
		isDragging = false;
	}




	@Override
	public void mouseDragged(MouseEvent e) {
		if (isDragging)
			setBotLoc(sim.getRealRobot(), e.getPoint());
	}




	@Override
	public void mouseMoved(MouseEvent e) { }
	
}
