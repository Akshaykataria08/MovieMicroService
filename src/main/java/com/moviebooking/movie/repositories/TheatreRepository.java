package com.moviebooking.movie.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.moviebooking.movie.domains.Address;
import com.moviebooking.movie.domains.Theatre;

@EnableScan
public interface TheatreRepository extends CrudRepository<Theatre, String> {

	public boolean existsByTheatreNameAndAddress(String theatreName, Address address);
}
