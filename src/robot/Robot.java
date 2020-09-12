package robot;

import util.Distribution;
import util.Vector;

public class Robot {
	public final Vector position;
	public double angle; // in rad
	public Chassis chassis;
	public Sensor[] sensors;
	
	
	public Robot(double x, double y, double angle, Chassis chassis, Sensor[] sensors) {
		this.chassis = chassis;
		this.sensors = sensors;
		position = new Vector(x, y);
		this.angle = angle;
	}

	/**
	 * Moves the robot forward by about the given distance. Deviation follows a Gaussian distribution. 
	 * @param targDist given distance
	 */
	public void move(double leftWheelVel, double rightWheelVel, double dt) {
		double[] changeInPos = chassis.drive(new double[] {leftWheelVel, rightWheelVel}, dt);
		position.plus(new Vector(changeInPos[0], changeInPos[1]).rotate(angle));
		angle += changeInPos[2];
		
	}
	
	public void setPosition(double x, double y) {
		position.setComponents(x, y);
	}
	
	public void setPosition(Vector v) {
		position.setComponents(v.comps);
	}
	
	public double[] getPosition() {
		return position.comps;
	}
	
	public double getX() {
		return position.comps[0];
	}
	
	public double getY() {
		return position.comps[1];
	}
}
