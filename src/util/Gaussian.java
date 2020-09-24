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
		if (stdDev == 0) {
			return center;
		}
		return center + stdDev * random.nextGaussian();
	}


	@Override
	public double getProbability(double outputNum, double idealNum) {
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
	
	
	// returns the cumulative normal distribution function (CNDF)
	// for a standard normal: N(0,1)
	private double CNDF(double x)
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