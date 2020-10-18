package com.moviebooking.movie.repositories;

import java.util.List;
import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.moviebooking.movie.domains.MovieTheatres;

@EnableScan
public interface MovieTheatresRepository extends CrudRepository<MovieTheatres, String>{

	List<MovieTheatres> findByLocationId(String locationId);
	List<MovieTheatres> findByMovieId(String movieId);
	Optional<MovieTheatres> findByLocationIdAndMovieId(String locationId, String movieId);
	
}
