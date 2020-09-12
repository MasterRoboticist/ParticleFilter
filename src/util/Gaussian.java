package util;

import java.util.Random;

public class Gaussian implements Distribution {
	double stdDev, center;
	Random random = new Random();
	
	public Gaussian(double center, double stdDev) {
		this.center = center;
		this.stdDev = stdDev;
	}
	
	
	public double generateRand() {
		return center + stdDev * random.nextGaussian();
	}
}
