package simulation;

import java.util.ArrayList;

import robot.Chassis;
import robot.IRSensor;
import robot.Robot;
import robot.Sensor;
import robot.Wheel;
import util.Gaussian;
import util.Vector;

public class Simulation {
	
	public int nbots;
	public int moves = 0;
	double rvel = 0, lvel = 0;
	int redistrStepSize = 1;
	
	private Robot trueBot;
	private ArrayList<Robot> simBots;
	
	Gaussian realWheelDistrib = new Gaussian(1, .1);
	Gaussian fakeWheelDistrib = new Gaussian(1, .1);
	Gaussian realSensorDistr = new Gaussian(1, 1);
	Gaussian fakeSensorDistr = new Gaussian(1, 1);
	double wheelDist = 10;
	Map map;
	Wheel[] realWheels = {new Wheel(realWheelDistrib, -wheelDist/2, 0), new Wheel(realWheelDistrib, wheelDist/2, 0)};
	Wheel[] fakeWheels = {new Wheel(fakeWheelDistrib, -wheelDist/2, 0), new Wheel(fakeWheelDistrib, wheelDist/2, 0)};
	Sensor[] realSensors;
	Sensor[] fakeSensors;
	
	
	public Simulation(int nbots, Map map) {
		simBots = new ArrayList<>(nbots);
		this.map = map;
		realSensors = new Sensor[] {new IRSensor(0, realSensorDistr, map)};
		fakeSensors = new Sensor[] {new IRSensor(0, fakeSensorDistr, map)};
		
		// create the robots
		trueBot = new Robot(0, 0, 0, new Chassis(realWheels), realSensors);
		for(int i = 0; i < nbots; i++) {
			simBots.add(newSimBot());
		}
		
		// scatter all the robots
		randomizeLocation(trueBot);
		scatter();
	}
	
	
	
	private Robot newSimBot() {
		return new Robot(0, 0, 0, new Chassis(fakeWheels), fakeSensors);
	}
	
	//Does this need 
	public void step() {
		if(moves < redistrStepSize) {
			moveSims(.1);
			moves++;
		}
		else {
			redistribute();
			moves = 0;
		}
	}
	
	/**
	 * Scatters the simulated bots.
	 */
	public void scatter() {
		for (Robot r : simBots)
			randomizeLocation(r);
	}
	
	private void randomizeLocation(Robot r) {
		double x, y;
		do {
			x = Math.random() * map.robotGridWidth;
			y = Math.random() * map.robotGridWidth;
			r.setPosition(x, y);
		} while(!map.isWall(r.position));
		r.angle = Math.random() * 360;
	}
	
	/**
	 * redistributes the location of the bots according to how likely a position is to be correct
	 */
	public void redistribute() {
		double[] probabilities = calcWeights();
		double sumProbs = sum(probabilities);
		
		if(avg(probabilities) < 30) {
			scatter();
			return;
		}
		
		Vector[] coords = new Vector[simBots.size()];
		for(int i = 0; i < simBots.size(); i++) 
			coords[i] = simBots.get(i).position.getCopy();
		
		for(Robot bot: simBots) {
			double rand = Math.random()*sumProbs;
			double currSum = probabilities[0];
			int currBot = 0;
			while(currSum < rand) {
				currBot++;
				currSum += probabilities[currBot];
			}
			bot.position = coords[currBot].getCopy();
		}
		
	}
	
	private double[] calcWeights() {
		double[] probs = new double[simBots.size()];
		double[] trueReadings = trueBot.getAllSensorReadings();
		for(int i = 0; i < simBots.size(); i++) {
			probs[i] = calcWeight(simBots.get(i), trueReadings);
		}
		return probs;
	}
	
	private double calcWeight(Robot bot, double[] sensorReadings) {
		double sum = 0;
		for(int i = 0; i < sensorReadings.length; i++) {
			sum += bot.sensors[i].distr.getProbability(sensorReadings[i]);
		}
		
		return sum/sensorReadings.length;
		
	}
	
	public double[][] readSensors() {
		double[][] readings = new double[simBots.size()][trueBot.sensors.length];
		for(int i = 0; i < simBots.size(); i++) {
			readings[i] = simBots.get(i).getAllSensorReadings();
		}
		return readings;
	}
	
	public void moveSims(double dt) {
		for(Robot r: this.simBots) {
			moveIt(r, dt);
		}
	}
	
	public void moveIt(Robot r, double dt) {
		Vector oldPos = r.position.getCopy();
		r.move(lvel, rvel, dt);
		if(map.isWall(r.position)) {
			r.setPosition(oldPos);
		}
	}
	
	public double sum(double[] list) {
		double sum = 0;
		for(double dbl: list) {
			sum+= dbl;
		}
		return sum;
	}
	
	public double avg(double[] list) {
		return sum(list)/list.length;
	}
	
	public void summary() {
		System.out.println("True Robot: ");
		System.out.println("	Position: " + trueBot.position.toString());
		System.out.println(String.format("	Angle: %.2f", trueBot.angle));
		System.out.println(String.format("	Sensor Reading: %.03f", trueBot.getSensorReading()));
		System.out.println("\n Simulated Robots: ");
		System.out.println("	--");

		for(Robot bot: simBots) {
			System.out.println("	Position: " + bot.position.toString());
			System.out.println(String.format("	Angle: %.2f", bot.angle));
			System.out.println(String.format("	Sensor Reading: %.3f", bot.getSensorReading()));
			System.out.println("	--");
		}
	}
	
	/*Functions for graphics*/
	
	public int getNumSimBots() {
		return nbots;
	}
	public void setNumSimBots(int num) {
		nbots = num;
		//remove robots if the number of bots has decreased
		while(simBots.size() > nbots) {
			simBots.remove(0);
		}
		//add bots if the number of bots has increased
		while(simBots.size() < nbots) {
			Robot bot = newSimBot();
			randomizeLocation(bot);
			simBots.add(bot);
		}
	}
	
	public ArrayList<Robot> getSimRobots() {
		return simBots;
	}
	public Robot getRealRobot() {
		return trueBot;
	}
	
	// James added this
	public Map getMap() {
		return map;
	}

	
	/**
	 * Sets the velocity of the left wheel, in robot units.
	 * @param vel ya
	 */
	public void setLeftWheelVel(double vel) {
		lvel = vel;
	}
	
	/**
	 * Sets the velocity of the right wheel, in robot units.
	 * @param vel ya
	 */
	public void setRightWheelVel(double vel) {
		rvel = vel;
	}
	
	public void setScatterStepSize(int n) {
		redistrStepSize = n;
	}
	
	public int getScatterStepSize() {
		return redistrStepSize;
	}

}
