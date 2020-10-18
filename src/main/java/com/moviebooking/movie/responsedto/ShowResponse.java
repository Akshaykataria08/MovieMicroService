package com.moviebooking.movie.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowResponse {

	private String showId;
	private String showStartTime;
	private String showEndTime;
	private String language;

}
