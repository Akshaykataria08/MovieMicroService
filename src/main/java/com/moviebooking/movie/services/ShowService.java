package com.moviebooking.movie.services;

import java.util.List;

import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.domains.Theatre;

public interface ShowService {

	public Show addShow(String string, Show show);
	
	public List<Show> getAllShowsInTheatre(Theatre theatre);

	public Show getShow(String showId);
	
	public boolean removeShow(Show show);

	public boolean removeAllShowsOfTheatre(List<Show> list);

	public Show updateShow(Show show);
}
