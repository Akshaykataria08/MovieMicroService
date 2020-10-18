package com.moviebooking.movie.services;

import java.util.List;
import java.util.Set;

import com.moviebooking.movie.domains.Movie;

public interface MovieServie {

	public Movie addMovie(Movie movie);

	public List<Movie> getAllMovies();

	public Movie getMovie(String movieId);

	public Movie updateMovie(Movie movie);

	public boolean addMovies(Set<Movie> movies);

}
