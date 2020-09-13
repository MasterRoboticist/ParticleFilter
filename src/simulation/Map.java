package simulation;

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
		
		this.robotGridWidth = robotGridWidth;
		wallGridSquareLength = robotGridWidth/obstacleMap.length;
		this.robotGridHeight = this.wallGridSquareLength * obstacleMap[0].length;
		
		makeObstacleMap();
		
	}

	public void makeObstacleMap(){
		boolean[][] obstacleMap = new boolean[img.getWidth()][img.getHeight()];
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				obstacleMap[x][y] = img.getRGB(x, y) > 0;
			}
		}
	}
	
	public boolean isWall(Vector pos) throws IndexOutOfBoundsException {
		return !obstacleMap[(int) (pos.x() / this.wallGridSquareLength)][(int) (pos.y() / this.wallGridSquareLength)];
	}
}
