package com.moviebooking.movie.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviebooking.movie.domains.Location;
import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.ResourceAlreadyExists;
import com.moviebooking.movie.exceptions.ResourceNotFoundException;
import com.moviebooking.movie.repositories.LocationRepository;
import com.moviebooking.movie.utils.Commons;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private TheatreService theatreService;

	@Override
	public List<Location> getAllLocations() {
		return new ArrayList<Location>((Collection<? extends Location>) locationRepository.findAll());
	}

	@Override
	public Location addLocation(Location location) {
		if (this.locationExists(location)) {
			log.error("Location " + location.getCity() + " already exists");
			throw new ResourceAlreadyExists("Location " + location.getCity() + " already exists");
		}
		return locationRepository.save(location);
	}

	@Override
	public boolean locationExists(Location location) {
		if (this.isLocationEmpty(location)) {
			throw new CustomException("Location object should not be null or empty");
		}
		return locationRepository.existsByCityAndState(location.getCity(), location.getState());
	}

	@Override
	public boolean addLocations(Set<Location> locations) {
		if (locations.isEmpty()) {
			return false;
		}
		int locationsToSaveCount = locations.size();
		Set<Location> savedLocations = new HashSet<Location>(
				(Collection<? extends Location>) locationRepository.findAll());
		List<Location> locationsToSave = locations.stream().filter(location -> {
			return this.isLocationEmpty(location) ? false : !savedLocations.contains(location);
		}).collect(Collectors.toList());
		locations = new HashSet<Location>((Collection<? extends Location>) locationRepository.saveAll(locationsToSave));
		return locationsToSaveCount == locations.size();
	}

	@Override
	public boolean addTheatreId(String theatreId, String locationId) {
		if (Commons.isStringEmpty(theatreId)) {
			throw new CustomException("Theatre Id must not be null");
		}
		Location location = this.getLocation(locationId);
		location.getTheatreIds().add(theatreId);
		locationRepository.save(location);
		return true;
	}

	@Override
	public boolean deleteTheatreId(String theatreId, String locationId) {
		if (Commons.isStringEmpty(theatreId)) {
			throw new CustomException("Theatre Id must not be null");
		}
		Location location = this.getLocation(locationId);
		boolean flag = location.getTheatreIds().remove(theatreId);
		locationRepository.save(location);
		return flag;
	}

	@Override
	public Location getLocation(String locationId) {
		if (Commons.isStringEmpty(locationId)) {
			throw new CustomException("Location Id must not be null");
		}
		return locationRepository.findById(locationId)
				.orElseThrow(() -> new ResourceNotFoundException("Location does not exists"));
	}

	@Override
	public boolean locationExists(String locationId) {
		if (Commons.isStringEmpty(locationId)) {
			throw new CustomException("Location Id must not be null");
		}
		return locationRepository.existsById(locationId);
	}

	@Override
	public Location getLocationByCityAndState(Location location) {
		if (this.isLocationEmpty(location)) {
			throw new CustomException("Location must not be null or empty");
		}
		return locationRepository.findByCityAndState(location.getCity(), location.getState()).orElse(null);
	}

	@Override
	public boolean deleteLocation(String locationId) {
		Location location = this.getLocation(locationId);
		List<String> theatreIds = location.getTheatreIds();
		locationRepository.delete(this.getLocation(locationId));
		return theatreService.deleteAllTheatresWithLocationId(theatreIds);
	}
	
	private boolean isLocationEmpty(Location location) {
		return (location == null || Commons.isStringEmpty(location.getCity())
				|| Commons.isStringEmpty(location.getState()));
	}
}
