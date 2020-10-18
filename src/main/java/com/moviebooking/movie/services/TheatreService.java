package com.moviebooking.movie.services;

import java.util.List;
import java.util.Set;

import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.domains.Theatre;
import com.moviebooking.movie.responsedto.ShowDetails;

public interface TheatreService {

	public List<Theatre> getAllTheatreInLocation(String locationId);

	public Theatre addTheatre(Theatre theatre);

	public List<Movie> getAllMoviesInTheatre(String theatreId);

	public List<Show> getAllShowsOfMovieInTheatre(String theatreId, String movieId);

	public Theatre updateTheatre(Theatre theatre);

	public boolean deleteTheatre(String theatreId);

	public Theatre getTheatre(String theatreId);

	public Show addShow(Show show);

	public List<Show> getAllShowsInTheatre(String theatreId);

	public boolean removeShow(String showId);

	public boolean addTheatres(Set<Theatre> theatres);

	public List<Theatre> getAllTheatresPlayingAMovie(String locationId, String movieName);

	public List<Movie> getAllMoviesInCity(String locationId, Double rating);

	public boolean deleteAllTheatresWithLocationId(List<String> theatreIds);

	public boolean removeShowsOfMovie(String locationId, String movieId);

	public List<Theatre> getTheatresFromIdsForMovieInLocation(String locationId, String movieId);

	public ShowDetails getShowFullDetails(String showId);
	
	public void updateMovieInShow(Movie movie);
}
