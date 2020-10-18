package com.moviebooking.movie.requestdto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class LocationRequest {

	@NonNull
	@NotEmpty
	private String city;
	
	@NonNull
	@NotEmpty
	private String state;
}
