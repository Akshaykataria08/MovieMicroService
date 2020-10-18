package com.moviebooking.movie.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.movie.domains.Location;
import com.moviebooking.movie.requestdto.LocationRequest;
import com.moviebooking.movie.responsedto.LocationListResponse;
import com.moviebooking.movie.responsedto.LocationResponse;
import com.moviebooking.movie.responsedto.MovieListResponse;
import com.moviebooking.movie.responsedto.MovieResponse;
import com.moviebooking.movie.responsedto.TheatreListResponse;
import com.moviebooking.movie.responsedto.TheatreResponse;
import com.moviebooking.movie.services.LocationService;
import com.moviebooking.movie.services.TheatreService;

@RestController
@RequestMapping("/v1/location")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private TheatreService theatreService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public LocationListResponse getAllLocations() {
		return new LocationListResponse(locationService.getAllLocations().stream().map(location -> {
			return modelMapper.map(location, LocationResponse.class);
		}).collect(Collectors.toList()));
	}

	@PostMapping
	public LocationResponse addLocation(@Valid @RequestBody LocationRequest locationRequest) {
		Location location = modelMapper.map(locationRequest, Location.class);
		return modelMapper.map(locationService.addLocation(location), LocationResponse.class);
	}

	@DeleteMapping("/{locationId}")
	public boolean deleteLocation(@PathVariable(required = true) String locationId) {
		return locationService.deleteLocation(locationId);
	}

	@DeleteMapping("/{locationId}/movie/{movieId}")
	public boolean deleteShowsOfMovieInLocation(@PathVariable String locationId, @PathVariable String movieId) {
		return theatreService.removeShowsOfMovie(locationId, movieId);
	}

	@GetMapping("/{locationId}/movie")
	public MovieListResponse getAllMoviesInCity(@PathVariable String locationId,
			@RequestParam(required = false) Double rating) {

		return new MovieListResponse(theatreService.getAllMoviesInCity(locationId, rating).stream().map(movie -> {
			return modelMapper.map(movie, MovieResponse.class);
		}).collect(Collectors.toList()));
	}

	@GetMapping("/{locationId}/movie/{movieId}/theatre")
	public TheatreListResponse getTheatresWithMovieInLocation(@PathVariable String locationId,
			@PathVariable String movieId) {
		return new TheatreListResponse(
				theatreService.getTheatresFromIdsForMovieInLocation(locationId, movieId).stream().map(theatre -> {
					return modelMapper.map(theatre, TheatreResponse.class);
				}).collect(Collectors.toList()));
	}

	@GetMapping("/{locationId}/theatre")
	public TheatreListResponse getAllTheatreInLocation(@PathVariable String locationId) {
		return new TheatreListResponse(theatreService.getAllTheatreInLocation(locationId).stream().map(theatre -> {
			return modelMapper.map(theatre, TheatreResponse.class);
		}).collect(Collectors.toList()));
	}

//	// list of Theatres Playing a particular movie in the selected city
//	@GetMapping("/{locationId}/movie/{movieName}/theatre")
//	public TheatreListResponse getAllTheatresPlayingAMovie(@PathVariable String locationId,
//			@PathVariable String movieName) {
//		return new TheatreListResponse(
//				theatreService.getAllTheatresPlayingAMovie(locationId, movieName).stream().map(theatre -> {
//					return modelMapper.map(theatre, TheatreResponse.class);
//				}).collect(Collectors.toList()));
//	}
}
