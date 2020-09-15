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
	
	private static double CNDF(double x)
	{
	    int neg = (x < 0d) ? 1 : 0;
	    if ( neg == 1) 
	        x *= -1d;

	    double k = (1d / ( 1d + 0.2316419 * x));
	    double y = (((( 1.330274429 * k - 1.821255978) * k + 1.781477937) *
	                   k - 0.356563782) * k + 0.319381530) * k;
	    y = 1.0 - 0.398942280401 * Math.exp(-0.5 * x * x) * y;
	    
	    return (1d - neg) * y + neg * (1d - y);
	}

}

