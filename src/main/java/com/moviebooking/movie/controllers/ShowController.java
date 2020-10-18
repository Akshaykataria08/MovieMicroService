package com.moviebooking.movie.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.requestdto.ShowRequest;
import com.moviebooking.movie.responsedto.ShowDetailsResponse;
import com.moviebooking.movie.responsedto.ShowResponse;
import com.moviebooking.movie.services.TheatreService;

@RestController
@RequestMapping("/v1/show")
public class ShowController {

	@Autowired
	private TheatreService theatreService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@DeleteMapping("/{showId}")
	public boolean removeShow(@PathVariable(required = true) String showId) {
		return theatreService.removeShow(showId);
	}
	
	@PostMapping
	public ShowResponse addShow(@Valid @RequestBody ShowRequest showRequest) {

		Show show = modelMapper.map(showRequest, Show.class);
		show = theatreService.addShow(show);
		if (show != null) {
			return modelMapper.map(show, ShowResponse.class);
		}
		return null;
	}
	
	@GetMapping("/info/{showId}")
	public ShowDetailsResponse getShowDetails(@PathVariable String showId) {
		return modelMapper.map(theatreService.getShowFullDetails(showId), ShowDetailsResponse.class);
	}
}
