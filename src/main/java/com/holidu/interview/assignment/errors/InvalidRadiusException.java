package com.holidu.interview.assignment.errors;

public class InvalidRadiusException extends RuntimeException {

	public InvalidRadiusException() {
		super("Radius value is invalid");
	}
}
