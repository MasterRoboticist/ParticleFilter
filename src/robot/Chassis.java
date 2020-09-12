package robot;

import util.Vector;

public class Chassis {
	
	// for future: length, width, center of gravity
	public Chassis(Wheel[] wheels) {
		this.wheels = wheels;
	}
	
	public final Wheel[] wheels;
	
	public double[] drive(double[] desiredWheelVels, double dt) {
		assert(desiredWheelVels.length == wheels.length);
		
		// only two wheels implemented
		assert(wheels.length == 2);
		
		double[] actualWheelVels = new double[wheels.length];
		
		for(int i = 0; i < wheels.length; i++) {
			actualWheelVels[i] = wheels[i].spin(desiredWheelVels[i]);
		}
		
		double wheelDist = wheels[0].position.distanceTo(wheels[1].position);
		
		return calcChangeInPose(actualWheelVels, wheelDist, dt);
	}
	
	
	public double[] calcChangeInPose(double[] wheelVels, double wheelDistance, double dt) {
		Vector changeInPos = new Vector(0,0);
		
		double radius = wheelDistance/2 * (wheelVels[0] + wheelVels[1])/(wheelVels[0] - wheelVels[1]);
		double omega = (wheelVels[0] - wheelVels[1])/wheelDistance;
		Vector ICC = new Vector(0, radius);
		changeInPos.minus(ICC).rotate(omega*dt).plus(ICC);
		
		return new double[] {changeInPos.x(), changeInPos.y(), omega*dt};
	}
	
}
