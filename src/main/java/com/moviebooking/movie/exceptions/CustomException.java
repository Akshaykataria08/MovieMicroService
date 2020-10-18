package com.moviebooking.movie.exceptions;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

	public CustomException(String message) {
		super(message);
	}
}
