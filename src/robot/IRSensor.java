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

	@Override
	public double read(Vector pos, double angle) {
		double x = pos.x(), y = pos.y();
		Vector endPos = pos.getCopy();
		Vector dr = new Vector(Math.cos(angle) * DR, Math.sin(angle) * DR);
		
		try {
			while (!map.isWall(endPos.plus(dr)));
			return pos.distanceTo(endPos);
			
		} catch (IndexOutOfBoundsException e) {
			return pos.minus(dr).distanceTo(endPos);
		}
	}
	
}
