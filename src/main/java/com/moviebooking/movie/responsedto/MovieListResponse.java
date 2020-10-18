package com.moviebooking.movie.responsedto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieListResponse {

	private List<MovieResponse> movies = new ArrayList<MovieResponse>();
}
