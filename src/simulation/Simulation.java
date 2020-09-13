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
	public boolean moving = true;
	
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
	Sensor[] realSensors = {new IRSensor(0, realSensorDistr, map)};
	Sensor[] fakeSensors = {new IRSensor(0, fakeSensorDistr, map)};
	
	
	
	public Simulation(int nbots, Map map) {
		simBots = new ArrayList<>(nbots);
		this.map = map;
		
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
		if(moving) {
			
		}
		else {
			redistribute();
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
		
		r.setPosition(x, y);
	}
	
	/**
	 * redistributes the location of the bots according to how likely a position is to be correct
	 */
	public void redistribute() {
		double[] probabilities = calcWeights();
		double sumProbs = sum(probabilities);
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
	
	public void moveSims(double velL, double velR, double time, double dt) {
		for(Robot r: this.simBots) {
			for(int i = 0; i < time/dt; i++) {
				moveIt(r, velL, velR, dt);				
			}
		}
	}
	
	public void moveIt(Robot r, double velL, double velR, double dt) {
		Vector oldPos = r.position.getCopy();
		r.move(velL, velR, dt);
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
	


}
