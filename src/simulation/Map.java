package simulation;

import java.awt.Point;
import java.awt.image.BufferedImage;

import util.Vector;

public class Map {
	final private int height;
	final private int width;
	final public BufferedImage img;
	
	public final double robotGridWidth, robotGridHeight;
	public final double wallGridSquareLength;
	
	private boolean[][] obstacleMap;
	
	public Map(int robotGridWidth, BufferedImage img) {
		this.height = img.getHeight();
		this.width = img.getWidth();
		this.img = img;
		
		obstacleMap = makeObstacleMap(img);
		
		this.robotGridWidth = robotGridWidth;
		wallGridSquareLength = robotGridWidth/obstacleMap.length;
		this.robotGridHeight = this.wallGridSquareLength * obstacleMap[0].length;
	}

	public static boolean[][] makeObstacleMap(BufferedImage img){
		boolean[][] obstacleMap = new boolean[img.getWidth()][img.getHeight()];
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				obstacleMap[x][y] = img.getRGB(x, y) > 0;
			}
		}
		return obstacleMap;
	}
	
	public boolean isWall(Vector pos) {
		try {
			return !obstacleMap[(int) (pos.x() / this.wallGridSquareLength)][(int) (pos.y() / this.wallGridSquareLength)];
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
	}
	
	/**
	 * Converts between robot meters and pixels in the image.
	 * @param pos robot position, in meters (i.e. {@link robot.Robot#position})
	 * @return a point specifying which pixel in {@link Map#img} the robot position corresponds to
	 */
	public Point robotPos2Pixel(Vector pos) {
		// TODO
		return null;
	}

	/**
	 * Converts between pixels in the image and robot meters. Inverse of {@link Map#robotPos2Pixel(Vector)}
	 * @param pixel a point specifying a pixel on the image
	 * @return the center of that pixel, in robot coordinates
	 */
	public Vector pixel2RobotPos(Point pixel) {
		// TODO
		return null;
	}
	
	// TODO you might want to add methods to convert between robot position and obstacle map indices, too, unless they are the same
}
