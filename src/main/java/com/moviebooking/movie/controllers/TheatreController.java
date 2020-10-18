package com.moviebooking.movie.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.movie.domains.Theatre;
import com.moviebooking.movie.requestdto.TheatreCreateRequest;
import com.moviebooking.movie.requestdto.TheatreUpdateRequest;
import com.moviebooking.movie.responsedto.TheatreResponse;
import com.moviebooking.movie.services.TheatreService;

@RestController
@RequestMapping("/v1/theatre")
public class TheatreController {

	@Autowired
	private TheatreService theatreService;

	@Autowired
	private ModelMapper modelMapper;

//	Theater

	@GetMapping("/{theatreId}")
	public TheatreResponse getTheatre(@PathVariable(required = true) String theatreId) {
		Theatre theatre = theatreService.getTheatre(theatreId);
		return modelMapper.map(theatre, TheatreResponse.class);
	}

	@PostMapping
	public TheatreResponse addTheatre(@Valid @RequestBody TheatreCreateRequest theatreRequest) {
		Theatre theatre = modelMapper.map(theatreRequest, Theatre.class);
		theatre = theatreService.addTheatre(theatre);
		return modelMapper.map(theatre, TheatreResponse.class);
	}

	@PutMapping
	public TheatreResponse updateTheatre(@Valid @RequestBody TheatreUpdateRequest theatreRequest) {
		Theatre theatre = modelMapper.map(theatreRequest, Theatre.class);
		theatre = theatreService.updateTheatre(theatre);
		return modelMapper.map(theatre, TheatreResponse.class);
	}

	@DeleteMapping("/{theatreId}")
	public boolean deleteTheatre(@PathVariable(required = true) String theatreId) {
		return theatreService.deleteTheatre(theatreId);
	}

//	// Show
//	@GetMapping("/{theatreId}/show")
//	public ShowListResponse getAllShowsInTheatre(@PathVariable(required = true) String theatreId) {
//		return new ShowListResponse(theatreService.getAllShowsInTheatre(theatreId).stream().map(show -> {
//			return modelMapper.map(show, ShowResponse.class);
//		}).collect(Collectors.toList()));
//	}

//	// Movies Played in a particular theatre
//	@GetMapping("/{theatreId}/movie")
//	public MovieListResponse getAllMoviesInTheatre(@PathVariable(required = true) String theatreId) {
//		return new MovieListResponse(theatreService.getAllMoviesInTheatre(theatreId).stream().map(movie -> {
//			return modelMapper.map(movie, MovieResponse.class);
//		}).collect(Collectors.toList()));
//	}
}
