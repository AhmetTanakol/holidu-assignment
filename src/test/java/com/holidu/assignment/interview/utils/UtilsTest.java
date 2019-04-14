package com.holidu.assignment.interview.utils;

import static org.hamcrest.Matchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.holidu.interview.assignment.utils.Utils;

public class UtilsTest {

	Utils utils;

	@Before
	public void loadUtils() {
		utils = new Utils();
	}

	@Test
	public void convertMetersToFeet() {
		Assert.assertThat(utils.convertMetersToFeet(4), is(12.8));
	}

	@Test
	public void givenPointIsWithinRadius() {
		Assert.assertTrue(utils.isWithinRadius(0, 0, 1, 1, 2));
	}

	@Test
	public void givenPointIsNotWithinRadius() {
		Assert.assertFalse(utils.isWithinRadius(0, 0, 4, 4, 2));
	}
}
