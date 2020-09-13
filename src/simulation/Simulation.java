package simulation;

import java.util.ArrayList;
import java.util.List;

import robot.Robot;
import util.Distribution;
import util.Gaussian;
import util.Vector;

public class Simulation {
	
	public final double robotGridWidth, robotGridHeight;
	public final double wallGridSquareLength;
	public int nbots;
	
	private Robot trueBot;
	private ArrayList<Robot> simBots;
	private boolean[][] grid;
	
	public Simulation(boolean[][] grid, int nbots, double robotGridWidth) {
		simBots = new ArrayList<>(nbots);
		this.grid = grid;
		
		this.robotGridWidth = robotGridWidth;
		wallGridSquareLength = robotGridWidth/grid.length;
		this.robotGridHeight = this.wallGridSquareLength * grid[0].length;
		
		// create the robots
		Distribution distr = new Gaussian(1, 0);
		trueBot = new Robot(10, distr);
		for(int i = 0; i < nbots; i++) {
			simBots.add(new Robot(10, distr));
		}
		
		// scatter all the robots
		randomizeLocation(trueBot);
		scatter();
	}
	
	
	
	private Robot newSimBot() {
		return new Robot();
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
			x = Math.random() * this.robotGridWidth;
			y = Math.random() * this.robotGridWidth;
			r.setPosition(x, y);
		} while(!isWall(r.position));
		
		r.setPosition(x, y);
	}
	
	/**
	 * redistributes the location of the bots according to how likely a position is to be correct
	 */
	public void redistribute() {
		
	}
	
	public double[][] readSensors() {
		double[][] readings = new double[simBots.size()][trueBot.sensors.length];
		for(int i = 0; i < simBots.size(); i++) {
			readings[i] = simBots.get(i).getAllSensorReadings();
		}
		return readings;
	}
	
	
	public boolean isWall(Vector robotPos) {
		return !grid[(int) (robotPos.x() / this.wallGridSquareLength)][(int) (robotPos.y() / this.wallGridSquareLength)];
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
		if(this.isWall(r.position)) {
			r.setPosition(oldPos);
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
	

}
