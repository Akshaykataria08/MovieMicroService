package com.moviebooking.movie.repositories;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.moviebooking.movie.domains.Location;

@EnableScan
public interface LocationRepository extends CrudRepository<Location, String> {

	public boolean existsByCityAndState(String city, String state);

	public Optional<Location> findByCityAndState(String city, String state);
}
