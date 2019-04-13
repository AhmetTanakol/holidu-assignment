package com.holidu.interview.assignment.utils;

import org.json.JSONObject;

public class Utils {

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
	 * @return
	 */
	public JSONObject calculateCircleBoundaries(int x, int y, int radius) {
		JSONObject boundaries = new JSONObject();
		JSONObject xBoundaries = new JSONObject();
		xBoundaries.put("max", "" + new Integer(x + radius));
		xBoundaries.put("min", "" + new Integer(x - radius));
		boundaries.put("xBoundaries", xBoundaries);
		JSONObject yBoundaries = new JSONObject();
		yBoundaries.put("max", "" + new Integer(y + radius));
		yBoundaries.put("min", "" + new Integer(y - radius));
		boundaries.put("yBoundaries", yBoundaries);

		return boundaries;
	}

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
		return (int)(number *  3.281);
	}
}
