package util;

import java.awt.Point;

public class Util {
	
	public static double dist2(Point p1, Point p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}
	

	
	public static double sum(double[] list) {
		double sum = 0;
		for(double dbl: list) {
			sum += dbl;
		}
		return sum;
	}
	
	public static double avg(double[] list) {
		return sum(list) / list.length;
	}

	

	
	public static  double least(double[] d) {
		return d[indexOfSmallest(d)];
	}



	public static  int indexOfSmallest(double[] d) {
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

	
	public static double most(double[] d) {
		return d[indexOfBiggest(d)];
	}



	public static  int indexOfBiggest(double[] d) {
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
}
