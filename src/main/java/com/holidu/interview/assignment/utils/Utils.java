package com.holidu.interview.assignment.utils;

public class Utils {

	/**
	 * Convert meter to foot
	 * 
	 * 1 meter ~ 3,281 feet
	 * For the sake of simplicity cast the result to integer
	 * 
	 * @param number
	 * @return
	 */
	public int convertMetersToFeet(int number) {
		return (int)(number *  3.2);
	}
	
	/**
	 * Calculate distance between 2 points, if radius is larger than distance,
	 * point is within the area.
	 * 
	 * @param x
	 * @param y
	 * @param targetX
	 * @param targetY
	 * @param radius
	 * @return
	 */
	public boolean isWithinRadius(int x, int y, int targetX, int targetY, int radius) {
		int distance = (int) Math.sqrt((targetX - x)^2 + (targetY - y)^2);
	    return distance <= radius;
	}
	
}
