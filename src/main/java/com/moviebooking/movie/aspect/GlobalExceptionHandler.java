package com.moviebooking.movie.aspect;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.InternalServerErrorException;
import com.moviebooking.movie.exceptions.ResourceAlreadyExists;
import com.moviebooking.movie.exceptions.ResourceNotFoundException;
import com.moviebooking.movie.exceptions.UnauthorizedException;
import com.moviebooking.movie.responsedto.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String NOT_FOUND = "NOT_FOUND";
	private static final String CONFLICT = "CONFLICT";
	private static final String BAD_REQUEST = "BAD_REQUEST";
	private static final String UNAUTHORIZED = "UNAUTHORIZED";
	private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException ex) {
		ExceptionResponse response = new ExceptionResponse(ex.getMessage(), NOT_FOUND, LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceAlreadyExists.class)
	public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceAlreadyExists ex) {
		ExceptionResponse response = new ExceptionResponse(ex.getMessage(), CONFLICT, LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFound(CustomException ex) {
		ExceptionResponse response = new ExceptionResponse(ex.getMessage(), BAD_REQUEST, LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFound(UnauthorizedException ex) {
		ExceptionResponse response = new ExceptionResponse(ex.getMessage(), UNAUTHORIZED, LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ExceptionResponse> ioException(IOException ex) {
		ExceptionResponse response = new ExceptionResponse(ex.getMessage(), INTERNAL_SERVER_ERROR, LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<ExceptionResponse> internalServerErrorException(InternalServerErrorException ex) {
		ExceptionResponse response = new ExceptionResponse(ex.getMessage(), INTERNAL_SERVER_ERROR, LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> allExceptions(Exception ex) {
		ExceptionResponse response = new ExceptionResponse("Something happened at our end", INTERNAL_SERVER_ERROR,
				LocalDateTime.now());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
