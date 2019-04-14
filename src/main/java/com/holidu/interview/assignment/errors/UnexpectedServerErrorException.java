package com.holidu.interview.assignment.errors;

public class UnexpectedServerErrorException extends RuntimeException{

	public UnexpectedServerErrorException() {
		super("Unexpected Error occurred");
	}
}
