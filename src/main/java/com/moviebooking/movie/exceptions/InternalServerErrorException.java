package com.moviebooking.movie.exceptions;

@SuppressWarnings("serial")
public class InternalServerErrorException extends RuntimeException{

	public InternalServerErrorException(String msg) {
		super(msg);
	}
}
