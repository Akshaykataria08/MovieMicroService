package com.moviebooking.movie.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moviebooking.movie.domains.Address;
import com.moviebooking.movie.domains.Location;
import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.domains.Theatre;
import com.moviebooking.movie.enums.LocationCSVHeader;
import com.moviebooking.movie.enums.MovieCSVHeader;
import com.moviebooking.movie.enums.ShowCSVHeader;
import com.moviebooking.movie.enums.TheatreCSVHeader;
import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.ResourceNotFoundException;
import com.moviebooking.movie.requestdto.LocationRequest;
import com.moviebooking.movie.requestdto.MovieCreateRequest;
import com.moviebooking.movie.requestdto.ShowRequest;
import com.moviebooking.movie.requestdto.TheatreCreateRequest;
import com.moviebooking.movie.utils.CSVUtility;
import com.moviebooking.movie.utils.Commons;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BulkUploadServiceImpl implements BulkUploadService {

	@Autowired
	private TheatreService theatreService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private MovieServie movieServie;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public boolean uploadLocationCSV(MultipartFile file) throws IOException {

		Iterable<CSVRecord> records = this.getRecords(file);
		Set<Location> locations = new HashSet<Location>();

		for (CSVRecord record : records) {
			Location location = this.getLocationFromCSVRecord(record);
			if (locations.contains(location)) {
				log.warn("Duplicate records found with city: " + location.getCity() + " and state: "
						+ location.getState());
			} else {
				locations.add(location);
			}
		}
		return locationService.addLocations(locations);
	}

	@Override
	public boolean uploadTheatreCSV(MultipartFile file) throws IOException {

		Iterable<CSVRecord> records = this.getRecords(file);
		Set<Theatre> theatres = new HashSet<Theatre>();

		for (CSVRecord record : records) {

			Theatre theatre = this.getTheatreFromCSVRecord(record);
			Location location = new Location(theatre.getAddress().getCity(), theatre.getAddress().getState());

			Location persistedLocation = locationService.getLocationByCityAndState(location);
			if (persistedLocation == null) {
				log.error("Location " + location.getCity() + ", " + location.getState() + " of theatre "
						+ theatre.getTheatreName() + " does not exists");
				throw new ResourceNotFoundException(
						"Location of theatre " + theatre.getTheatreName() + " does not exists");
			}
			theatre.setLocationId(persistedLocation.getLocationId());

			if (theatres.contains(theatre)) {
				log.warn("Duplicate Theatre:" + theatre.getTheatreName() + " found");
			} else {
				theatres.add(theatre);
			}
		}
		return theatreService.addTheatres(theatres);
	}

	@Override
	public boolean uploadMovieCSV(MultipartFile file) throws IOException {
		Iterable<CSVRecord> records = this.getRecords(file);
		Set<Movie> movies = new HashSet<Movie>();

		for (CSVRecord record : records) {
			Movie movie = this.getMovieFromCSVRecord(record);
			if (movies.contains(movie)) {
				log.warn("Duplicate movie: " + movie.getMovieName() + " found");
			} else {
				movies.add(movie);
			}
		}
		return movieServie.addMovies(movies);
	}

	@Override
	public boolean uploadShowCSV(MultipartFile file) throws IOException {
		Iterable<CSVRecord> records = this.getRecords(file);
		Set<Show> shows = new HashSet<Show>();

		for (CSVRecord record : records) {

			Show show = this.getShowFromCSVRecord(record);
			if (shows.contains(show)) {
				log.warn("Duplicate Show found with Theatre Id: " + show.getTheatreId() + " and Movie ID: "
						+ show.getMovieId());
			} else {
				shows.add(show);
			}
		}
		shows.stream().forEach(theatreService::addShow);
		return true;
	}

	private Location getLocationFromCSVRecord(CSVRecord record) {
		String city = record.get(LocationCSVHeader.CITY);
		String state = record.get(LocationCSVHeader.STATE);

		if (Commons.isStringEmpty(city) || Commons.isStringEmpty(state)) {
			throw new CustomException("City or State can't be null or empty");
		}

		LocationRequest locationRequest = new LocationRequest(city, state);
		return modelMapper.map(locationRequest, Location.class);
	}

	private Theatre getTheatreFromCSVRecord(CSVRecord record) {

		String theatreName = record.get(TheatreCSVHeader.THEATRE_NAME.getCsvHeader());
		String ratingString = record.get(TheatreCSVHeader.RATING.getCsvHeader());
		String building = record.get(TheatreCSVHeader.BUILDING.getCsvHeader());
		String area = record.get(TheatreCSVHeader.AREA.getCsvHeader());
		String city = record.get(TheatreCSVHeader.CITY.getCsvHeader());
		String state = record.get(TheatreCSVHeader.STATE.getCsvHeader());
		String pincodeString = record.get(TheatreCSVHeader.PINCODE.getCsvHeader());

		if (Commons.isStringEmpty(theatreName) || Commons.isStringEmpty(ratingString) || Commons.isStringEmpty(building)
				|| Commons.isStringEmpty(area) || Commons.isStringEmpty(city) || Commons.isStringEmpty(state)
				|| Commons.isStringEmpty(pincodeString)) {
			throw new CustomException("All Theatre fields are mandatory");
		}

		TheatreCreateRequest theatreCreateRequest = new TheatreCreateRequest();

		Address address = new Address();
		address.setArea(area);
		address.setBuilding(building);
		address.setCity(city);
		address.setPincode(pincodeString);
		address.setState(state);

		theatreCreateRequest.setAddress(address);
		theatreCreateRequest.setTheatreName(theatreName);
		theatreCreateRequest.setRating(Double.parseDouble(ratingString));

		return modelMapper.map(theatreCreateRequest, Theatre.class);
	}

	private Movie getMovieFromCSVRecord(CSVRecord record) {

		String movieName = record.get(MovieCSVHeader.MOVIE_NAME.getCsvHeader());
		String durationString = record.get(MovieCSVHeader.DURATION.getCsvHeader());
		String ratingString = record.get(MovieCSVHeader.RATING.getCsvHeader());
		String releaseDate = record.get(MovieCSVHeader.RELEASE_DATE.getCsvHeader());
		String castStringList = record.get(MovieCSVHeader.CAST.getCsvHeader());
		String genresStringList = record.get(MovieCSVHeader.GENRE.getCsvHeader());
		String languageStringList = record.get(MovieCSVHeader.LANGUAGE.getCsvHeader());

		if (Commons.isStringEmpty(movieName) || Commons.isStringEmpty(durationString)
				|| Commons.isStringEmpty(ratingString) || Commons.isStringEmpty(releaseDate)) {
			throw new CustomException("All Movie fields are mandatory");
		}

		MovieCreateRequest movieCreateRequest = new MovieCreateRequest();
		movieCreateRequest.setCast(CSVUtility.csvCellToStringList(castStringList));
		movieCreateRequest.setDuration(durationString);
		movieCreateRequest.setGenres(CSVUtility.csvCellToStringList(genresStringList));
		movieCreateRequest.setLanguages(CSVUtility.csvCellToStringList(languageStringList));
		movieCreateRequest.setMovieName(movieName);
		movieCreateRequest.setRating(Double.parseDouble(ratingString));
		movieCreateRequest.setReleaseDate(releaseDate);
		return modelMapper.map(movieCreateRequest, Movie.class);

	}

	private Show getShowFromCSVRecord(CSVRecord record) {
		String theatreId = record.get(ShowCSVHeader.THEATRE_ID.getCsvHeader());
		String movieId = record.get(ShowCSVHeader.MOVIE_ID.getCsvHeader());
		String languageString = record.get(ShowCSVHeader.LANGUAGE.getCsvHeader());
		String showStartTimeString = record.get(ShowCSVHeader.SHOW_START_TIME.getCsvHeader());
		String showEndTimeString = record.get(ShowCSVHeader.SHOW_END_TIME.getCsvHeader());

		if (Commons.isStringEmpty(theatreId) || Commons.isStringEmpty(movieId) || Commons.isStringEmpty(languageString)
				|| Commons.isStringEmpty(showStartTimeString) || Commons.isStringEmpty(showEndTimeString)) {
			throw new CustomException("All show Fields are mandatory");
		}

		ShowRequest showRequest = new ShowRequest();
		showRequest.setTheatreId(theatreId);
		showRequest.setLanguage(languageString);
		showRequest.setMovieId(movieId);
		showRequest.setShowStartTime(showStartTimeString);
		showRequest.setShowEndTime(showEndTimeString);
		return modelMapper.map(showRequest, Show.class);
	}

	private Iterable<CSVRecord> getRecords(MultipartFile file) throws IOException {
		try {
			return CSVUtility.getCSVRecords(file);
		} catch (IOException e) {
			throw new IOException("Oops... Something happened at our end");
		}
	}
}
