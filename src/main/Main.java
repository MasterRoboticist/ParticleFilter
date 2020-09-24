package main;

import java.awt.image.BufferedImage;

import simulation.Map;
import simulation.Simulation;
import util.ImageReader;


public class Main {
	static double stdDev = 10.5;

	public static void main(String[] args) {
		
		
		System.out.println(getProbability(2,24));
	}

	public static double getProbability(double outputNum, double idealNum) {
		if(stdDev == 0) {
			return outputNum == idealNum ? 1 : 0;
		}
		double standardNormalNum = ((outputNum-idealNum)/stdDev);
		double r = 2*CNDF(- Math.abs(standardNormalNum));
		if(r >= 1 && standardNormalNum != 0) {
			System.out.println("ERROR CDNF(" + standardNormalNum + ") = 1");
		}
		return r;
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

