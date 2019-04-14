package com.holidu.assignment.interview.models;

import org.junit.Assert;
import org.junit.Test;

import com.holidu.interview.assignment.model.Boundaries;

public class BoundariesTest {

	@Test
	public void createBoundariesObject() {
		Boundaries boundaries = new Boundaries(0, 0, 2);
		Assert.assertNotNull(boundaries);
		Assert.assertTrue("It should set max value of x to 2", boundaries.getMaxX() == 2);
		Assert.assertTrue("It should set min value of x to -2", boundaries.getMinX() == -2);
		Assert.assertTrue("It should set max value of y to 2", boundaries.getMaxY() == 2);
		Assert.assertTrue("It should set min value of y to -2", boundaries.getMinY() == -2);
	}

	@Test
	public void setValuesOfFields() {
		Boundaries boundaries = new Boundaries(0, 0, 2);
		boundaries.setMaxX(4);
		boundaries.setMinX(-4);
		boundaries.setMaxY(4);
		boundaries.setMinY(-4);
		Assert.assertTrue("It should set max value of x to 2", boundaries.getMaxX() == 4);
		Assert.assertTrue("It should set min value of x to -2", boundaries.getMinX() == -4);
		Assert.assertTrue("It should set max value of y to 2", boundaries.getMaxY() == 4);
		Assert.assertTrue("It should set min value of y to -2", boundaries.getMinY() == -4);
	}
}
