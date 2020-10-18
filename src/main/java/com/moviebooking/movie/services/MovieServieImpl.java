package com.moviebooking.movie.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.ResourceNotFoundException;
import com.moviebooking.movie.repositories.MovieRepository;
import com.moviebooking.movie.utils.Commons;

@Service
public class MovieServieImpl implements MovieServie {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private TheatreService theatreService;

	@Override
	public Movie addMovie(Movie movie) {
		if (this.isMovieEmpty(movie)) {
			throw new CustomException("Movie data must be there");
		}
		return movieRepository.save(movie);
	}

	@Override
	public List<Movie> getAllMovies() {
		return new ArrayList<Movie>((Collection<? extends Movie>) movieRepository.findAll());
	}

	@Override
	public Movie getMovie(String movieId) {
		if (Commons.isStringEmpty(movieId)) {
			throw new CustomException("MovieId must not be null");
		}
		return movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
	}

	@Override
	public Movie updateMovie(Movie movie) {
		if (movie == null) {
			throw new CustomException("Movie must not be empty");
		}
		Movie updatedMovie = this.getMovie(movie.getMovieId());
		if (!Commons.isStringEmpty(movie.getMovieName())) {
			updatedMovie.setMovieName(movie.getMovieName());
		}
		if (movie.getRating() != null) {
			updatedMovie.setRating(movie.getRating());
		}
		movie = movieRepository.save(updatedMovie);
		theatreService.updateMovieInShow(movie);
		return movie;
	}

	@Override
	public boolean addMovies(Set<Movie> movies) {
		if (movies.isEmpty()) {
			return false;
		}
		int moviesToSaveCount = movies.size();
		Set<Movie> existedMovies = new HashSet<Movie>((Collection<? extends Movie>) movieRepository.findAll());
		movies = movies.stream().filter(movie -> !existedMovies.contains(movie)).collect(Collectors.toSet());
		movies = new HashSet<Movie>((Collection<? extends Movie>) movieRepository.saveAll(movies));
		return moviesToSaveCount == movies.size();
	}

	private boolean isMovieEmpty(Movie movie) {
		return (movie == null || Commons.isStringEmpty(movie.getMovieName())
				|| Commons.isStringEmpty(movie.getDuration().toString()) || Commons.isListEmpty(movie.getGenres())
				|| Commons.isListEmpty(movie.getLanguages()) || Commons.isListEmpty(movie.getCast())
				|| movie.getRating() == null || movie.getReleaseDate() == null);
	}
}
