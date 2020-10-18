package com.moviebooking.movie.responsedto;

import com.moviebooking.movie.domains.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TheatreResponse {

	private String theatreId;
	private String theatreName;
	private Double rating;
	private Address address;
}
