package robot;

import util.Distribution;

public class IRSensor extends Sensor{

	// for future: multiple types of sensors
	public IRSensor(double angle, Distribution distr) {
		super(angle);
		this.distr = distr;
	}
	
	public final Distribution distr;
	
}
