package simulation;

import java.util.ArrayList;

import robot.Chassis;
import robot.IRSensor;
import robot.Robot;
import robot.Sensor;
import robot.Wheel;
import util.Gaussian;
import util.Util;
import util.Vector;

public class Simulation {
	
	public int nbots;
	public int moves = 1;
	double rvel = 0, lvel = 0;
	double rOn = 1, lOn = 1;  // modifier for rvel, lvel
	int redistrStepSize = 2;
	private double dt = 1; //seconds
	int rescatterMin = 20; //minimum number of steps between rescatterings
	int lastScatter = 0;
	
	private Robot trueBot;
	private ArrayList<Robot> simBots;
	
	public static final Gaussian realWheelDistrib = new Gaussian(0, .5);
	public static final Gaussian fakeWheelDistrib = new Gaussian(0, 2);
	public static final Gaussian realSensorDistr = new Gaussian(0, 5);
	public static final Gaussian fakeSensorDistr = new Gaussian(0, 10);
	
	double wheelDist = 10;
	Map map;
	Wheel[] realWheels = {new Wheel(realWheelDistrib, -wheelDist/2, 0), new Wheel(realWheelDistrib, wheelDist/2, 0)};
	Wheel[] fakeWheels = {new Wheel(fakeWheelDistrib, -wheelDist/2, 0), new Wheel(fakeWheelDistrib, wheelDist/2, 0)};
	double[] irSensorAngles = {0,Math.PI/8,Math.PI/4, Math.PI*3/4, Math.PI/2, -Math.PI/8, -Math.PI/4, -Math.PI*3/4, -Math.PI/2};
	Sensor[] realSensors = new Sensor[irSensorAngles.length];
	Sensor[] fakeSensors = new Sensor[irSensorAngles.length];
	
	
	public Simulation(int nbots, Map map) {
		Robot.loadSprites();
		
		simBots = new ArrayList<>(nbots);
		this.nbots = nbots;
		this.map = map;
		for(int i = 0; i < irSensorAngles.length; i++) {
			realSensors[i] = new IRSensor(irSensorAngles[i], realSensorDistr, map);
			fakeSensors[i] = new IRSensor(irSensorAngles[i], fakeSensorDistr, map);
		}
		
		// create the robots
		trueBot = new Robot(950, 315, Math.PI, new Chassis(realWheels), realSensors);
		trueBot.setToRealBotSprite();
		for(int i = 0; i < nbots; i++) {
			simBots.add(newSimBot());
		}
		
		// scatter all the robots
//		randomizeLocation(trueBot);
		scatter();
	}
	
	
	
	private Robot newSimBot() {
		return new Robot(0, 0, 0, new Chassis(fakeWheels), fakeSensors);
	}
	
	//Does this need 
	public void step() {
		if(moves % redistrStepSize != 0) {
			moveIt(trueBot, dt);
			moveSims(dt);
			moves++;
		}
		else {
			redistribute();
			moves++;
		}
	}
	
	/**
	 * Scatters the simulated bots.
	 */
	public void scatter() {
		for (Robot r : simBots)
			randomizeLocation(r);
		lastScatter = moves;
	}
	
	private void randomizeLocation(Robot r) {
		double x, y;
		do {
			x = Math.random() * map.robotGridWidth;
			y = Math.random() * map.robotGridHeight;
			r.setPosition(x, y);
		} while(map.isWall(r.position));
		r.angle = Math.random() * 2*Math.PI;
//		System.out.println(map.robotPos2ObstacleMap(r.position).toString());
	}
	
	/**
	 * redistributes the location of the bots according to how likely a position is to be correct
	 */
	public void redistribute() {
		double[] probabilities = calcWeights();
//		System.out.println("Best Bot is " + indexOfBiggest(probabilities) +" with prob: " + most(probabilities));
		
		if(Util.avg(probabilities) < .01 && moves > lastScatter + rescatterMin) {
			System.out.println("rescattering, avg: " + Util.avg(probabilities));
			scatter();
			return;
		}
		
		Vector[] coords = new Vector[nbots];
		double[] angles = new double[nbots];
		for(int i = 0; i < nbots; i++) {
			coords[i] = simBots.get(i).position.getCopy();
			angles[i] = simBots.get(i).angle;
		}
		
		double sumProbs = Util.sum(probabilities);
		
		for(Robot bot: simBots) {
			double rand = Math.random()*sumProbs;
			double currSum = probabilities[0];
			int currBotNum = 0;
			while(currSum < rand) {
				currBotNum++;
				currSum += probabilities[currBotNum];
			}
			bot.position.setComponents(coords[currBotNum]);
			bot.angle = angles[currBotNum];
			
//			System.out.print(currBotNum+",");
		}
//		System.out.println("");
		
	}


	private double[] calcWeights() {
		double[] probs = new double[nbots];
		double[] trueReadings = trueBot.getAllSensorReadings();
		for(int i = 0; i < nbots; i++) {
			probs[i] = calcWeight(simBots.get(i), trueReadings);
		}
		return probs;
	}
	
	/**
	 * Calculates the probability that the given readings are at least as bad as the given bot's readings
	 * @param bot a simrobot
	 * @param sensorReadings the real robot's readings
	 * @return the probability
	 */
	private double calcWeight(Robot bot, double[] sensorReadings) {
		double prod = 1;
		for(int i = 0; i < sensorReadings.length; i++) {
			prod *= bot.sensors[i].distr.getProbability(sensorReadings[i], bot.getSensorReadings(i))+.01;
		}
		//Ensure has at least some chance of getting chosen, even if all probablities are 0
//		return 10*prod / sensorReadings.length + 1; // this was when the probs were added
		return Math.pow(prod, 1/(double)bot.sensors.length);
	}
	
	// this is unused
	public double[][] readSensors() {
		double[][] readings = new double[nbots][trueBot.sensors.length];
		for(int i = 0; i < nbots; i++) {
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
		
		r.move(lOn*lvel, rOn*rvel, dt);
		if(map.isWall(r.position)) {
			r.setPosition(oldPos);
		}
	}
	
	public void summary(String option) {
		if(option.equals("long")) {
			System.out.println("True Robot: ");
			System.out.println("	Position: " + trueBot.position.toString());
			System.out.println(String.format("	Angle: %.2f", trueBot.angle));
			System.out.println(String.format("	Sensor 0 Reading: %.03f", trueBot.getSensorReadings(0)));
			System.out.println("\n Simulated Robots: ");
			System.out.println("	--");

			double[] probabilities = calcWeights();

			for(int i = 0; i < nbots; i++) {
				Robot bot = simBots.get(i);
				System.out.println("	Position: " + bot.position.toString());
				System.out.println(String.format("	Angle: %.2f", bot.angle));
				System.out.println(String.format("	Sensor 0 Reading: %.3f", bot.getSensorReadings(0)));
				System.out.println("	Similarity: " + probabilities[i]);
				System.out.println("	--");
			}
		}
		else {
			System.out.println("True Robot: ");
			System.out.println("	Position: " + trueBot.position.toString());
			System.out.println(String.format("	Angle: %.2f", trueBot.angle));
			System.out.println(String.format("	Sensor 0 Reading: %.03f", trueBot.getSensorReadings(0)));
			System.out.println("\n Simulated Robots: ");
			System.out.println("	--");

			double[] probabilities = calcWeights();
			double[] xpos = new double[nbots];
			double[] ypos = new double[nbots];
			double[] angle = new double[nbots];
			double[][] sensorReading = new double[nbots][simBots.get(0).sensors.length];
			for(int i = 0; i < nbots; i++) {
				xpos[i] = simBots.get(i).position.x();
				ypos[i] = simBots.get(i).position.y();
				angle[i] = simBots.get(i).angle;
				for(int j = 0; j < simBots.get(i).sensors.length; j++) 
					sensorReading[i][j] = simBots.get(i).getSensorReadings(j); 
			}
			System.out.println(String.format("	Average position: <%.1f, %.1f>", Util.avg(xpos), Util.avg(ypos)));
			System.out.println(String.format("	Average angle: %.2f", Util.avg(angle)));
			System.out.println(String.format("	Average sensor 0 reading: %.3f", Util.avg(sensorReading[0])));
			System.out.println("	Average Similarity: " + Util.avg(probabilities));
			System.out.println("	--");
			
			System.out.println("Best Sim Robot: ");
			int index = Util.indexOfBiggest(probabilities);
			System.out.println("	Position: " + simBots.get(index).position.toString());
			System.out.println("	Angle: " + simBots.get(index).angle);
			System.out.println(String.format("	Sensor 0 Reading: %.03f", simBots.get(index).getSensorReadings(0)));
			System.out.println("	Similarity: " + probabilities[index]);

		}
		System.out.println("---------------------------");
	}

	/*Functions for graphics*/
	
	public int getNumSimBots() {
		return nbots;
	}
	public void setNumSimBots(int num) {
		System.out.println("Current: " + nbots + " going to: " + num);
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
	
	
	public void setRightWheelOn(double on) {
		rOn = on;
	}
	public void setLeftWheelOn(double on) {
		lOn = on;
	}
	
	public void setScatterStepSize(int n) {
		redistrStepSize = n;
	}
	
	public int getScatterStepSize() {
		return redistrStepSize;
	}
	
	public void setDT(double dt) {
		this.dt = dt;
	}
	public double getLeftWheelVel() {
		return lvel;
	}
	
	/**
	 * Sets the velocity of the right wheel, in robot units.
	 * @param vel ya
	 */
	public double getRightWheelVel() {
		return rvel;
	}


	public double getSpriteWidth() {
		return Robot.getSpriteWidth();
	}

}
