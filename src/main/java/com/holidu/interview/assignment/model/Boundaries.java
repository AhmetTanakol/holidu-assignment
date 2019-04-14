package com.holidu.interview.assignment.model;

public class Boundaries {
	private int maxX;
	private int minX;
	private int maxY;
	private int minY;

	/**
	 * A circle can be described with this cartesian equation: (x − a)^2 + (y − b)^2 = r^2
	 * (a,b) is the center point.
	 * 
	 * In order to make a search in the given radius,
	 * we need to find maximum and minimum boundaries for both x and y
	 * 
	 * To find max boundary for an axis, add radius to the corresponding point of the center
	 * Example: boundary of y axis is equal to 5 where b is equal to 2 and r is 3
	 * 
	 * Math proof:
	 * Example: (3,2) is center point and radius=3
	 * 
	 * (x-3)^2 + (y-2)^2 = 3^2
	 * x-3 = SQRT(-(y^2-4y-5))
	 * 
	 * If we set left hand side to 0;
	 * 
	 * 0 = SQRT(-((y+1)(y-5))) where x = 3
	 * (y+1) = 0 => y = -1, x = 3
	 * (y-5) = 0 => y = 5, x = 3
	 * 
	 * Boundaries of y are -1 and 5
	 *
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Boundaries(int x, int y, int radius) {
		this.maxX = new Integer(x + radius);
		this.minX = new Integer(x - radius);
		this.maxY = new Integer(y + radius);
		this.minY = new Integer(y - radius);
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

}
