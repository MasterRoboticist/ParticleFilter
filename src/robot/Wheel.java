package robot;

import util.Distribution;
import util.Vector;

public class Wheel {
	//for future: position on chassis, wheel height, wheel friction, isDriven
	public Wheel(Distribution distr, double x, double y) {
		this.distr = distr;
		position = new Vector(x, y);
	}
	
	public final Distribution distr;
	public final Vector position;

	public double spin(double d) {
		return d + distr.generateRand();
	}

}
