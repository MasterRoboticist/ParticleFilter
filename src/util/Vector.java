/**
 * @author James
 */

package util;

public class Vector {
	public Vector(double...comps) {
		dim = comps.length;
		this.comps = new double[dim];
		for (int i = 0; i < dim; i++)
			this.comps[i] = comps[i];
	}
	
	public Vector(int dim) {
		comps = new double[dim];
		this.dim = dim;
	}
	
	public final double[] comps;
	public final int dim;
	
	/**
	 * Sets this vector's components to the specified components.
	 * Assumes the correct number of components are given.
	 * @param newcomps given components
	 * @return this vector
	 */
	public Vector setComponents(double... newcomps) {
		for (int i = 0; i < dim; i++) {
			comps[i] = newcomps[i];
		}
		return this;
	}
	public Vector setComponents(Vector v) {
		return setComponents(v.comps);
	}
	
	/**
	 * Adds the given vector to this vector.
	 * @param v given vector
	 * @return this vector
	 */
	public Vector plus(Vector v) {
		for (int i = 0; i < dim; i++)
			comps[i] += v.comps[i];
		return this;
	}
	
	/**
	 * Subtracts the given vector from this vector.
	 * @param v given vector
	 * @return this vector
	 */
	public Vector minus(Vector v) {
		for (int i = 0; i < dim; i++)
			comps[i] -= v.comps[i];
		return this;
	}
	
	public double distanceTo(Vector v) {
		return Math.sqrt(Math.pow(x()-v.x(),2) + Math.pow(y()-v.y(), 2));
	}
	
	/**
	 * Multiplied the current vector by the given scalar.
	 * @param s given scalar
	 * @return this vector
	 */
	public Vector times(double s) {
		for (int i = 0; i < dim; i++)
			comps[i] *= s;
		return this;
	}
	
	public String toString() {
		String str = "<";
		for (int i = 0; i < dim-1; i++)
			str += String.format("%.1f, ", comps[i]);
		str += String.format("%.1f>", comps[dim-1]);
		return str;
	}
	
	public double x() {
		return comps[0];
	}
	
	public double y() {
		return comps[1];
	}
	
	public Vector getCopy() {
		return new Vector(comps);
	}


	/**
	 * Rotates this vector by the given angle counterclockwise.
	 * @param angle given angle, in radians
	 * @return this vector
	 */
	public Vector rotate(double angle) {
		double c = Math.cos(angle), s = Math.sin(angle);
		return setComponents(comps[0] * c - comps[1] * s, comps[0] * s + comps[1] * c);
	}
	

	/**
	 * Generates a unit vector pointing in the given direction.
	 * @param angle direction, in radians
	 * @return the unit vector
	 */
	public static Vector makeUnitRadians(double angle) {
		return new Vector(Math.cos(angle), Math.sin(angle));
	}
	
	public boolean equals(Vector v) {
		return v.x() == x() && v.y() == y();
	}
	
}
