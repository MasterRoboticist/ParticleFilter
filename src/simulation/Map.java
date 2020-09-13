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
	
	public Map(int height, int width, int robotGridWidth, BufferedImage img) {
		this.height = height;
		this.width = width;
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
		return obstacleMap2Pixel(robotPos2ObstacleMap(pos));
	}

	/**
	 * Converts between pixels in the image and robot meters. Inverse of {@link Map#robotPos2Pixel(Vector)}
	 * @param pixel a point specifying a pixel on the image
	 * @return the center of that pixel, in robot coordinates
	 */
	public Vector pixel2RobotPos(Point pixel) {
		return obstacleMap2RobotPos(pixel2ObstacleMap(pixel));
	}
	
	public Point obstacleMap2Pixel(Vector obstaclePos) {
		return new Point((int)(obstaclePos.x()*width/img.getWidth()), (int)(obstaclePos.y()*height/img.getHeight()));
	}
	
	public Vector pixel2ObstacleMap(Point pixel) {
		return new Vector(pixel.x*img.getWidth()/width, pixel.y*img.getHeight()/height);
	}
	
	public Vector obstacleMap2RobotPos(Vector obstaclePos) {
		return new Vector(obstaclePos.x()*this.wallGridSquareLength, obstaclePos.y()*this.wallGridSquareLength);
	}
	
	public Vector robotPos2ObstacleMap(Vector robotPos) {
		return new Vector(robotPos.x()/this.wallGridSquareLength, robotPos.y()/this.wallGridSquareLength);
	}
	
}
