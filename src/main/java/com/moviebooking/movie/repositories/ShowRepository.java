package com.moviebooking.movie.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.moviebooking.movie.domains.Show;

@EnableScan
public interface ShowRepository extends CrudRepository<Show, String> {

}
