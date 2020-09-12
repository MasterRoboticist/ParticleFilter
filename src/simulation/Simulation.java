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
	
	private Robot trueBot;
	private List<Robot> simBots;
	private boolean[][] grid;
	
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
	
	public void redistribute() {
		
	}
	
	public double[] shootAllLasers(Robot r) {
		
	}
	
	/**
	 * Determines the distance between the position and the nearest wall in the given direction. 
	 * @param pos the position
	 * @param angle the angle
	 * @return the distance
	 */
	public double shootLaser(Vector pos, double angle) {
		return 0;
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

}
