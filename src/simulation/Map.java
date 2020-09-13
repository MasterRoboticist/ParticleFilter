package simulation;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.Vector;

public class Map {
	final private int height;
	final private int width;
	final public BufferedImage img;
	
	public final double robotGridWidth, robotGridHeight;
	public final double wallGridSquareLength;
	
	private boolean[][] obstacleMap;
	
	public Map(int height, int width, double robotGridWidth, BufferedImage img) {
		this.height = height;
		this.width = width;
		this.img = img;
		
		obstacleMap = makeObstacleMap(img);
		
		this.robotGridWidth = robotGridWidth;
		wallGridSquareLength = robotGridWidth/obstacleMap.length;
		System.out.println(wallGridSquareLength);
		System.out.println(obstacleMap.length);
		this.robotGridHeight = this.wallGridSquareLength * obstacleMap[0].length;
		System.out.println(robotGridHeight);
	}

	public static boolean[][] makeObstacleMap(BufferedImage img){
		boolean[][] obstacleMap = new boolean[img.getWidth()][img.getHeight()];
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				
				obstacleMap[x][y] = (img.getRGB(x, y) & 0x00ffffff) > 0 || (img.getRGB(x,y) & 0xff000000) == 0;
//				if((img.getRGB(x, y)) == 0) {
//					System.out.println(Integer.toHexString(img.getRGB(x, y)));
//					System.out.println(obstacleMap[x][y]);
//				}
				
			}
		}
		
		//make png of obstacle map
//		BufferedImage outImage = new BufferedImage(obstacleMap.length, obstacleMap[0].length,
//                BufferedImage.TYPE_INT_RGB);
//        for (int i = 0; i < obstacleMap[0].length; i++) {
//            for (int j = 0; j < obstacleMap.length; j++) {
//                if (obstacleMap[j][i]) {
//                    outImage.setRGB (j, i, 0xffffffff);
//                } else {
//                    outImage.setRGB (j, i, 0x00000000);
//                }
//            }
//        }
//        try {
//            // Save as PNG
//            File file = new File("outimage.png");
//            ImageIO.write(outImage, "png", file);
//        } catch (IOException e) {}
//		
		return obstacleMap;
	}
	
	public boolean isWall(Vector pos) {
		pos = this.robotPos2ObstacleMap(pos);
		try {
			boolean b = !obstacleMap[(int)pos.x()][(int)pos.y()];
			return false;
//			return !obstacleMap[(int)pos.x()][(int)pos.y()];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("out of bounds");
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
		return new Point((int)(obstaclePos.x()), (int)(obstaclePos.y()));
	}
	
	public Vector pixel2ObstacleMap(Point pixel) {
		return new Vector(pixel.x, pixel.y);
	}
	
	public Vector obstacleMap2RobotPos(Vector obstaclePos) {
		return new Vector(obstaclePos.x()/this.wallGridSquareLength, obstaclePos.y()/this.wallGridSquareLength);
	}
	
	public Vector robotPos2ObstacleMap(Vector robotPos) {
		return new Vector(robotPos.x()*this.wallGridSquareLength, robotPos.y()*this.wallGridSquareLength);
	}
	
}
