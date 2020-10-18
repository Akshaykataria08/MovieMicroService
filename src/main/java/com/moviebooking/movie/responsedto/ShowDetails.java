package com.moviebooking.movie.responsedto;

import java.util.Calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowDetails {

	private Calendar showStartTime;
	private Calendar showEndTime;
	private String movieName;
	private String theatreName;
	private String city;
	private String state;
}
