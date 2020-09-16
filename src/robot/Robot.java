package robot;

import java.awt.image.BufferedImage;

import util.ImageReader;
import util.Vector;

public class Robot {
	public Vector position;
	public double angle; // in rad
	public Chassis chassis;
	public Sensor[] sensors;
	public double[] sensorReadings;
	Vector lastPos;
	double lastAngle;
	
	
	static BufferedImage simSprite = ImageReader.readImage("rollerRobotStanding.png");
	static BufferedImage trueSprite = ImageReader.readImage("rollerRobotStandingRed.png");
	BufferedImage sprite = simSprite;
	
	
	public Robot(double x, double y, double angle, Chassis chassis, Sensor[] sensors) {
		this.chassis = chassis;
		this.sensors = sensors;
		position = new Vector(x, y);
		this.angle = angle;
	}
	
	public void setToRealBotSprite() {
		sprite = trueSprite;
	}

	/**
	 * Moves the robot forward by about the given distance. Deviation follows a Gaussian distribution. 
	 * @param targDist given distance
	 */
	public void move(double leftWheelVel, double rightWheelVel, double dt) {
		double[] changeInPos = chassis.drive(new double[] {leftWheelVel, rightWheelVel}, dt);
		Vector oldPos = position.getCopy();
		position.plus(new Vector(changeInPos[0], changeInPos[1]).rotate(angle));
		if(sensors[0].map.isWall(position)) {
			double maxPosChange = sensors[0].map.apprDistToWall(position, angle, .3);
			position = oldPos.plus(new Vector(maxPosChange*Math.cos(angle), maxPosChange*Math.sin(angle)));
		}
		angle += changeInPos[2];
		angle %= 2*Math.PI;
		
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
	
	/* for graphics */
	public double getSensorReading() {
		if(sensors.length > 1) {
			System.out.println("Warning: Calling 'getSensorReading()' is ambiguous.  Multiple sensors available.");
		}
		return getSensorReadings(0);
	}
	
	public double getSensorReadings(int sensornum) {
		if(sensorReadings == null || !(lastPos.equals(position) && angle == lastAngle)) {
			lastPos = position.getCopy();
			lastAngle = angle;
			return sensors[sensornum].read(position, angle);
		}
		else return sensorReadings[sensornum];
	}
	
	public double[] getAllSensorReadings() {
		double[] readings = new double[sensors.length];
		for(int i = 0; i < sensors.length; i++) {
			readings[i] = getSensorReadings(i);
		}
		return readings;
	}
	
	/**
	 * Returns the sprite representing this robot 
	 * @return the sprite
	 */
	public BufferedImage getSprite() {
		return sprite;
	}
}
