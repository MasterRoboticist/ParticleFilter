package main;

import java.awt.image.BufferedImage;

import simulation.Map;
import simulation.Simulation;
import util.ImageReader;


public class Main {

	public static void main(String[] args) {
		
//		new Display();
		
		//load map into array
		BufferedImage mapImage = ImageReader.readImage("white10x10.png");

		
		double robotGridWidth = 1000;
		int nbots = 1000;
		
		Map map = new Map(mapImage.getWidth(), mapImage.getHeight(), robotGridWidth, mapImage);
		Simulation sim = new Simulation(nbots, map);
		
		sim.summary("short");
		
		sim.setLeftWheelVel(10);
		sim.setRightWheelVel(10);
		
		for(int i = 0; i < 100; i++) {
			sim.step();
		}
		sim.summary("short");
	}

}

