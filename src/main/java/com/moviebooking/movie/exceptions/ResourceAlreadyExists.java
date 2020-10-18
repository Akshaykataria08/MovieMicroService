package com.moviebooking.movie.exceptions;

@SuppressWarnings("serial")
public class ResourceAlreadyExists extends RuntimeException {

	public ResourceAlreadyExists(String message) {
		super(message);
	}
}
