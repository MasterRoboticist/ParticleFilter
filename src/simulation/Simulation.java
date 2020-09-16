package simulation;

import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import robot.Chassis;
import robot.IRSensor;
import robot.Robot;
import robot.Sensor;
import robot.Wheel;
import util.Gaussian;
import util.Vector;

public class Simulation {
	
	public int nbots;
	public int moves = 1;
	double rvel = 0, lvel = 0;
	int redistrStepSize = 2;
	double dt = 1; //seconds
	int rescatterMin = 20; //minimum number of steps between rescatterings
	int lastScatter = 0;
	
	private Robot trueBot;
	private ArrayList<Robot> simBots;
	
	Gaussian realWheelDistrib = new Gaussian(0, 0.0001);
	Gaussian fakeWheelDistrib = new Gaussian(0, 2);
	Gaussian realSensorDistr = new Gaussian(0, 5);
	Gaussian fakeSensorDistr = new Gaussian(0, 10);
	double wheelDist = 10;
	Map map;
	Wheel[] realWheels = {new Wheel(realWheelDistrib, -wheelDist/2, 0), new Wheel(realWheelDistrib, wheelDist/2, 0)};
	Wheel[] fakeWheels = {new Wheel(fakeWheelDistrib, -wheelDist/2, 0), new Wheel(fakeWheelDistrib, wheelDist/2, 0)};
	double[] irSensorAngles = {0,Math.PI/8,Math.PI/4, Math.PI*3/4, Math.PI/2, -Math.PI/8, -Math.PI/4, -Math.PI*3/4, -Math.PI/2};
	Sensor[] realSensors = new Sensor[irSensorAngles.length];
	Sensor[] fakeSensors = new Sensor[irSensorAngles.length];
	
	
	public Simulation(int nbots, Map map) {
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
		System.out.println(map.robotPos2ObstacleMap(r.position).toString());
	}
	
	/**
	 * redistributes the location of the bots according to how likely a position is to be correct
	 */
	public void redistribute() {
		double[] probabilities = calcWeights();
//		System.out.println("Best Bot is " + indexOfBiggest(probabilities) +" with prob: " + most(probabilities));
		double sumProbs = sum(probabilities);
		
		if(avg(probabilities) < .05 && moves > lastScatter + rescatterMin) {
			System.out.println("rescattering");
			scatter();
			return;
		}
		
		Vector[] coords = new Vector[nbots];
		double[] angles = new double[nbots];
		for(int i = 0; i < nbots; i++) {
			coords[i] = simBots.get(i).position.getCopy();
			angles[i] = simBots.get(i).angle;
		}
		
		for(Robot bot: simBots) {
			double rand = Math.random()*sumProbs;
			double currSum = probabilities[0];
			int currBotNum = 0;
			while(currSum < rand) {
				currBotNum++;
				currSum += probabilities[currBotNum];
			}
			bot.position = coords[currBotNum].getCopy();
			bot.angle = angles[currBotNum];
			
//			System.out.print(currBotNum+",");
		}
//		System.out.println("");
		
	}
	
	private double least(double[] d) {
		// TODO Auto-generated method stub
		return d[indexOfSmallest(d)];
	}



	private int indexOfSmallest(double[] d) {
		int smallestindex = 0;
		double smallest = d[0];
		for(int i = 0; i < d.length; i++) {
			if(d[i] < smallest) {
				smallestindex = i;
				smallest = d[i];
			}
		}
		return smallestindex;
	}

	
	private double most(double[] d) {
		// TODO Auto-generated method stub
		return d[indexOfBiggest(d)];
	}



	private int indexOfBiggest(double[] d) {
		int biggestindex = 0;
		double biggest = d[0];
		for(int i = 0; i < d.length; i++) {
			if(d[i] > biggest) {
				biggestindex = i;
				biggest = d[i];
			}
		}
		return biggestindex;
	}



	private double[] calcWeights() {
		double[] probs = new double[nbots];
		double[] trueReadings = trueBot.getAllSensorReadings();
		for(int i = 0; i < nbots; i++) {
			probs[i] = calcWeight(simBots.get(i), trueReadings);
		}
		return probs;
	}
	
	private double calcWeight(Robot bot, double[] sensorReadings) {
		double sum = 0;
		for(int i = 0; i < sensorReadings.length; i++) {
			sum += bot.sensors[i].distr.getProbability(sensorReadings[i], bot.getSensorReadings(i));
		}
		//Ensure has at least some chance of getting chosen, even if all probablities are 0
		return 10*sum/(double)sensorReadings.length + 1;
		
	}
	
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
		return sum(list)/(double)list.length;
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
				System.out.println(String.format("	Sensor 0 Reading: %.3f", bot.getSensorReading()));
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
			System.out.println(String.format("	Average position: <%.1f, %.1f>", avg(xpos), avg(ypos)));
			System.out.println(String.format("	Average angle: %.2f", avg(angle)));
			System.out.println(String.format("	Average sensor 0 reading: %.3f", avg(sensorReading[0])));
			System.out.println("	Average Similarity: " + avg(probabilities));
			System.out.println("	--");
			
			System.out.println("Best Sim Robot: ");
			int index = indexOfBiggest(probabilities);
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
