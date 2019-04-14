package com.holidu.interview.assignment.controller;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.holidu.interview.assignment.errors.ApiError;
import com.holidu.interview.assignment.errors.InvalidRadiusException;
import com.holidu.interview.assignment.errors.InvalidURLException;
import com.holidu.interview.assignment.errors.UnexpectedServerErrorException;



/**
 * Catch the exception and return custom ApiError
 * @author ahmettanakol
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidURLException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleInvalidURLException(InvalidURLException ex) {
		ApiError apiError = 
				new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
		return apiError;
	}

	@ExceptionHandler(InvalidRadiusException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleInvalidRadiusException(InvalidRadiusException ex) {
		ApiError apiError = 
				new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
		return apiError;
	}


	@ExceptionHandler(UnexpectedServerErrorException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ApiError handleUnexpectedServerErrorException(UnexpectedServerErrorException ex) {
		ApiError apiError = 
				new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getMessage());
		return apiError;
	}

}