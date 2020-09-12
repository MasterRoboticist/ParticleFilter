package main;

import java.awt.image.BufferedImage;

import graphics.Display;
import robot.Chassis;
import robot.IRSensor;
import robot.Robot;
import robot.Sensor;
import robot.Wheel;
import util.Gaussian;
import util.ImageReader;


public class Main {

	public static void main(String[] args) {
		double wheelDist = 10;
		Gaussian realWheelDistrib = new Gaussian(1, .1);
		Wheel[] realWheels = {new Wheel(realWheelDistrib, -wheelDist/2, 0), new Wheel(realWheelDistrib, wheelDist/2, 0)}; 
		
		Gaussian realSensorDistr = new Gaussian(1, 1);
		Sensor[] realSensors = {new IRSensor(0, realSensorDistr)};
		
		
		Robot realBot = new Robot(0, 0, 0, new Chassis(realWheels), realSensors);
		realBot.move(1, 1, 1);
		System.out.println(realBot.getPosition()[0] + ", " + realBot.getPosition()[1]);
		
		new Display();
		
		//load map into array
		BufferedImage mapImage = ImageReader.readImage("logo.png");
		boolean[][] map = new boolean[mapImage.getWidth()][mapImage.getHeight()];
		
		for(int x = 0; x < mapImage.getWidth(); x++) {
			for(int y = 0; y < mapImage.getHeight(); y++) {
				map[x][y] = mapImage.getRGB(x, y) > 0;
			}
		}
		
		//make fake bots
		
		//hi
	}

}
