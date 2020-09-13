package robot;

import simulation.Map;

public abstract class Sensor {
	// Future: position on Chassis
	public Sensor(double angle) {
		this.angle = angle;
	}
	
	public final double angle;
	
	public abstract double read(Map map);
		
}
