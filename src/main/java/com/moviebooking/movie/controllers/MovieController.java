package com.moviebooking.movie.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.requestdto.MovieCreateRequest;
import com.moviebooking.movie.requestdto.MovieUpdateRequest;
import com.moviebooking.movie.responsedto.MovieListResponse;
import com.moviebooking.movie.responsedto.MovieResponse;
import com.moviebooking.movie.responsedto.ShowListResponse;
import com.moviebooking.movie.responsedto.ShowResponse;
import com.moviebooking.movie.services.MovieServie;
import com.moviebooking.movie.services.TheatreService;

@RestController
@RequestMapping("/v1/movie")
public class MovieController {

	@Autowired
	private MovieServie movieService;
	
	@Autowired
	private TheatreService theatreService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	public MovieResponse addMovie(@Valid @RequestBody MovieCreateRequest movieRequest) {
		Movie movie = modelMapper.map(movieRequest, Movie.class);
		movie = movieService.addMovie(movie);
		return modelMapper.map(movie, MovieResponse.class);
	}

	@GetMapping
	public MovieListResponse getAllMovies() {
		return new MovieListResponse(movieService.getAllMovies().stream().map(movie -> {
			return modelMapper.map(movie, MovieResponse.class);
		}).collect(Collectors.toList()));
	}

	@GetMapping("/{movieId}")
	public MovieResponse getMovie(@PathVariable(required = true) String movieId) {
		return modelMapper.map(movieService.getMovie(movieId), MovieResponse.class);
	}
	
	@PutMapping
	public MovieResponse updateMovie(@Valid @RequestBody MovieUpdateRequest movieRequest) {
		Movie movie = modelMapper.map(movieRequest, Movie.class);
		movie = movieService.updateMovie(movie);
		return modelMapper.map(movie, MovieResponse.class);
	}
	
	// Get All shows when Movie card is selected from frontEnd
	@GetMapping("/{movieId}/theatre/{theatreId}/show")
	public ShowListResponse getAllShowsOfMovieInTheatre(@PathVariable(required = true) String theatreId,
			@PathVariable(required = true) String movieId) {
		return new ShowListResponse(
				theatreService.getAllShowsOfMovieInTheatre(theatreId, movieId).stream().map(show -> {
					return modelMapper.map(show, ShowResponse.class);
				}).collect(Collectors.toList()));
	}
}
