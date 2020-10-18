package com.moviebooking.movie.services;

import java.util.List;
import java.util.Set;

import com.moviebooking.movie.domains.Location;

public interface LocationService {

	public List<Location> getAllLocations();

	public Location addLocation(Location location);

	public boolean locationExists(Location location);

	public boolean addLocations(Set<Location> locations);

	public boolean addTheatreId(String theatreId, String city);

	public boolean deleteTheatreId(String theatreId, String city);

	public Location getLocation(String cityName);

	public boolean locationExists(String locationId);

	public Location getLocationByCityAndState(Location location);

	public boolean deleteLocation(String locationId);
}
