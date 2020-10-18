package com.moviebooking.movie.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.moviebooking.movie.domains.Movie;

@EnableScan
public interface MovieRepository extends CrudRepository<Movie, String> {

}
