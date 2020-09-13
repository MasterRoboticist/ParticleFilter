package main;

import java.awt.image.BufferedImage;

import simulation.Map;
import simulation.Simulation;
import util.ImageReader;


public class Main {

	public static void main(String[] args) {
		
//		new Display();
		
		//load map into array
		BufferedImage mapImage = ImageReader.readImage("logo.png");

		
		double robotGridWidth = 1000;
		int nbots = 10;
		
		Map map = new Map(mapImage.getWidth(), mapImage.getHeight(), robotGridWidth, mapImage);
		Simulation sim = new Simulation(nbots, map);
		
		sim.summary();

	}

}

