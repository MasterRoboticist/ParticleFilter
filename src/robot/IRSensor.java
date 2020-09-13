package robot;

import simulation.Map;
import util.Distribution;

public class IRSensor extends Sensor{

	// for future: multiple types of sensors
	public IRSensor(double angle, Distribution distr) {
		super(angle);
		this.distr = distr;
	}
	
	public final Distribution distr;

	@Override
	public double read(Map map) {
		boolean[][] obstacles = map.getObstacleMap();
		return 0;
	}
	
}
