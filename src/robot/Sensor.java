package robot;

import simulation.Map;
import util.Distribution;
import util.Vector;

public abstract class Sensor {
	// Future: position on Chassis
	public Sensor(double angle, Distribution distr, Map map) {
		this.angle = angle;
		this.distr = distr;
		this.map = map;
	}
	
	public final double angle;
	public final Distribution distr;
	public final Map map;
	
	public abstract double read(Vector pos, double angle);
		
}
