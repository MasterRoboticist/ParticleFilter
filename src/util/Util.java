package util;

import java.awt.Point;

public class Util {
	
	public static double dist2(Point p1, Point p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}

}
