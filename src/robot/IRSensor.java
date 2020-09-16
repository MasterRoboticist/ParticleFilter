package robot;

import simulation.Map;
import util.Distribution;
import util.Vector;

public class IRSensor extends Sensor{

	// for future: multiple types of sensors
	public IRSensor(double angle, Distribution distr, Map map) {
		super(angle, distr, map);
	}
	
	private final static double DR = .3;
	Vector lastPos = new Vector(-1, -1);
	double lastAngle = -1;
	double lastReading;

	@Override
	public double read(Vector pos, double angle) {
		if (pos.equals(lastPos) && angle == lastAngle) {
			return lastReading;
		}
		
		lastReading = map.apprDistToWall(pos, angle, DR) + distr.generateRand();
		lastReading = lastReading < 0 ? 0 : lastReading;
		
		lastPos = pos.getCopy();
		
		
		return lastReading;
	}

}
