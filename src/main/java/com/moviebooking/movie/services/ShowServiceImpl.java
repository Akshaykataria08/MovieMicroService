package com.moviebooking.movie.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.domains.Theatre;
import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.InternalServerErrorException;
import com.moviebooking.movie.exceptions.ResourceNotFoundException;
import com.moviebooking.movie.repositories.ShowRepository;
import com.moviebooking.movie.utils.Commons;

@Service
public class ShowServiceImpl implements ShowService {

	@Autowired
	private ShowRepository showRepository;
	
	@Autowired
	private MovieServie movieServie;
	
	@Value("${seatMap-service-url}")
	private String seatMapServiceUrl;
	
	@Override
	public Show addShow(String theatreId, Show show) {
		if(this.isShowEmpty(show)) {
			throw new CustomException("Show data must not be empty");
		}
		Movie movie = movieServie.getMovie(show.getMovieId());
		show.setMovie(movie);
		show.setTheatreId(theatreId);
		show = showRepository.save(show);
		boolean response = WebClient.builder().build().post().uri(seatMapServiceUrl + "/{showId}", show.getShowId()).retrieve().bodyToMono(Boolean.class).block();
		if(!response) {
			showRepository.delete(show);
			throw new InternalServerErrorException("Couldn't able to create show");
		}
		return show;
	}


	@Override
	public List<Show> getAllShowsInTheatre(Theatre theatre) {
		if(theatre == null || theatre.getShows() == null) {
			throw new InternalServerErrorException("Something happend at our end");
		}
		return theatre.getShows();
	}

	@Override
	public boolean removeShow(Show show) {
		if(show == null || Commons.isStringEmpty(show.getShowId())) {
			throw new InternalServerErrorException("Something happend at our end");
		}
		showRepository.delete(show);
		WebClient.builder().build().delete().uri(seatMapServiceUrl + "/{showId}", show.getShowId()).retrieve().bodyToMono(Boolean.class).block();
		return true;
	}

	@Override
	public Show getShow(String showId) {
		if(Commons.isStringEmpty(showId)) {
			throw new CustomException("Show id must not be null");
		}
		return showRepository.findById(showId).orElseThrow(() -> new ResourceNotFoundException("Show Not found"));
	}

	@Override
	public boolean removeAllShowsOfTheatre(List<Show> shows) {
		if(Commons.isListEmpty(shows)) {
			return true;
		}
		showRepository.deleteAll(shows);
		return true;
	}

	private boolean isShowEmpty(Show show) {
		return (show == null || Commons.isStringEmpty(show.getTheatreId()) || Commons.isStringEmpty(show.getMovieId()) || show.getLanguage() == null
				|| show.getShowStartTime() == null || show.getShowEndTime() == null);
	}


	@Override
	public Show updateShow(Show show) {
		return showRepository.save(show);
	}
}
