package com.moviebooking.movie.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationResponse {

	private String locationId;
	private String city;
	private String state;

}
