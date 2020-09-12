package robot;

public class Axle {
	
	public Axle(double length, Wheel lwheel, Wheel rwheel) {
		assert(1==0);
		this.length = length;
		this.lwheel = lwheel;
		this.rwheel = rwheel;
	}
	
	public final double length;
	public final Wheel lwheel, rwheel;
	
	
	public double spin(double d) {
		return 0;
	}

}
