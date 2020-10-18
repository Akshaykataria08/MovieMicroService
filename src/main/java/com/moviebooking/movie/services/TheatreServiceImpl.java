package com.moviebooking.movie.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviebooking.movie.domains.Address;
import com.moviebooking.movie.domains.Location;
import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.domains.MovieTheatres;
import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.domains.Theatre;
import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.InternalServerErrorException;
import com.moviebooking.movie.exceptions.ResourceAlreadyExists;
import com.moviebooking.movie.exceptions.ResourceNotFoundException;
import com.moviebooking.movie.repositories.MovieTheatresRepository;
import com.moviebooking.movie.repositories.TheatreRepository;
import com.moviebooking.movie.responsedto.ShowDetails;
import com.moviebooking.movie.utils.Commons;

@Service
public class TheatreServiceImpl implements TheatreService {

	@Autowired
	private LocationService locationService;

	@Autowired
	private TheatreRepository theatreRepository;

	@Autowired
	private MovieTheatresRepository movieTheatresRepository;

	@Autowired
	private MovieServie movieServie;

	@Autowired
	private ShowService showService;

	@Override
	public List<Theatre> getAllTheatreInLocation(String locationId) {
		Location location = locationService.getLocation(locationId);
		return new ArrayList<Theatre>(
				(Collection<? extends Theatre>) theatreRepository.findAllById(location.getTheatreIds()));
	}

	@Override
	public Theatre addTheatre(Theatre theatre) {

		if (this.theatreExists(theatre)) {
			throw new ResourceAlreadyExists("Theatre already exists");
		}
		Location location = locationService.getLocationByCityAndState(
				new Location(theatre.getAddress().getCity(), theatre.getAddress().getState()));
		theatre.setLocationId(location.getLocationId());
		theatre = theatreRepository.save(theatre);
		locationService.addTheatreId(theatre.getTheatreId(), location.getLocationId());
		return theatre;
	}

	private boolean theatreExists(Theatre theatre) {

		if (this.isTheatreEmpty(theatre)) {
			throw new CustomException("Please provide theatre details");
		}
		return theatreRepository.existsByTheatreNameAndAddress(theatre.getTheatreName(), theatre.getAddress());
	}

	@Override
	public List<Movie> getAllMoviesInTheatre(String theatreId) {

		Theatre theatre = this.getTheatre(theatreId);

		return theatre.getShows().stream().map(show -> {
			return show.getMovie();
		}).collect(Collectors.toList());
	}

	@Override
	public List<Show> getAllShowsOfMovieInTheatre(String theatreId, String movieId) {
		if (movieId == null || movieId.isEmpty()) {
			throw new CustomException("MovieId must not be null");
		}

		Theatre theatre = this.getTheatre(theatreId);
		return theatre.getShows().stream().filter(show -> {
			return show.getMovie().getMovieId().equals(movieId);
		}).collect(Collectors.toList());
	}

	@Override
	public Theatre updateTheatre(Theatre theatre) {

		if (theatre == null) {
			throw new CustomException("Please provide Theatre data to update it");
		}

		Theatre updatedTheatre = this.getTheatre(theatre.getTheatreId());
		if (theatre.getRating() != null) {
			updatedTheatre.setRating(theatre.getRating());
		}
		if (!Commons.isStringEmpty(theatre.getTheatreName())) {
			updatedTheatre.setTheatreName(theatre.getTheatreName());
		}
		if (this.theatreExists(updatedTheatre)) {
			throw new CustomException("Theatre update operation failed");
		}
		return theatreRepository.save(updatedTheatre);
	}

	@Override
	public boolean deleteTheatre(String theatreId) {
		Theatre theatre = this.getTheatre(theatreId);

		showService.removeAllShowsOfTheatre(theatre.getShows());
		theatreRepository.delete(theatre);
		locationService.deleteTheatreId(theatreId, theatre.getLocationId());
		return true;
	}

	@Override
	public Theatre getTheatre(String theatreId) {
		if (Commons.isStringEmpty(theatreId)) {
			throw new CustomException("Theatre Id must not be empty");
		}
		return theatreRepository.findById(theatreId)
				.orElseThrow(() -> new ResourceNotFoundException("Theatre Does not exists"));
	}

	@Override
	public Show addShow(Show show) {
		if (show == null) {
			throw new CustomException("Please provide show data");
		}
		Theatre theatre = this.getTheatre(show.getTheatreId());
		show = showService.addShow(theatre.getTheatreId(), show);
		theatre.getShows().add(show);
		theatre = theatreRepository.save(theatre);

		MovieTheatres movieTheatres = movieTheatresRepository
				.findByLocationIdAndMovieId(theatre.getLocationId(), show.getMovieId())
				.orElse(new MovieTheatres(theatre.getLocationId(), show.getMovieId()));
		
		HashMap<String, Integer> theatreIds = movieTheatres.getTheatreIds();
		if (theatreIds.containsKey(theatre.getTheatreId())) {
			theatreIds.put(theatre.getTheatreId(), theatreIds.get(theatre.getTheatreId()) + 1);
		} else {
			theatreIds.put(theatre.getTheatreId(), 1);
		}
		movieTheatresRepository.save(movieTheatres);
		return show;
	}

	@Override
	public List<Show> getAllShowsInTheatre(String theatreId) {
		Theatre theatre = this.getTheatre(theatreId);
		return showService.getAllShowsInTheatre(theatre);
	}

	@Override
	public boolean removeShow(String showId) {
		Show show = showService.getShow(showId);
		Theatre theatre = this.getTheatre(show.getTheatreId());
		theatre.getShows().remove(show);
		theatreRepository.save(theatre);
		boolean flag = showService.removeShow(show);

		MovieTheatres movieTheatres = movieTheatresRepository
				.findByLocationIdAndMovieId(theatre.getLocationId(), show.getMovieId())
				.orElseThrow(() -> new InternalServerErrorException("Something Happend at our end"));

		HashMap<String, Integer> theatreIds = movieTheatres.getTheatreIds();
		if (theatreIds.get(theatre.getTheatreId()) > 1) {
			theatreIds.put(theatre.getTheatreId(), theatreIds.get(theatre.getTheatreId()) - 1);
		} else {
			theatreIds.remove(theatre.getTheatreId());
		}
		if (theatreIds.isEmpty()) {
			movieTheatresRepository.delete(movieTheatres);
		} else {
			movieTheatresRepository.save(movieTheatres);
		}
		return flag;
	}

	@Override
	public boolean addTheatres(Set<Theatre> theatres) {

		if (theatres.isEmpty()) {
			return false;
		}
		int theatresToSaveCount = theatres.size();
		int theatresAddedCount = 0;
		theatres = theatres.stream().filter(theatre -> {
			if (this.theatreExists(theatre)) {
				System.out.println("Theatre " + theatre.getTheatreName() + " already exists");
				return false;
			}
			return true;
		}).collect(Collectors.toSet());

		theatres = new HashSet<Theatre>((Collection<? extends Theatre>) theatreRepository.saveAll(theatres));
		theatres.stream().forEach(theatre -> {
			locationService.addTheatreId(theatre.getTheatreId(), theatre.getLocationId());
		});
		theatresAddedCount = theatres.size();
		return theatresToSaveCount == theatresAddedCount;
	}

	@Override
	public List<Theatre> getAllTheatresPlayingAMovie(String locationId, String movieName) {
		if (Commons.isStringEmpty(locationId) || Commons.isStringEmpty(movieName)) {
			throw new CustomException("Please provide locationId and movie name");
		}

		Set<Theatre> theatres = new HashSet<Theatre>();
		for (Theatre theatre : this.getAllTheatreInLocation(locationId)) {
			for (Show show : theatre.getShows()) {
				if (show.getMovie().getMovieName().equalsIgnoreCase(movieName)) {
					theatres.add(theatre);
				}
			}
		}
		return new ArrayList<Theatre>(theatres);
	}

	@Override
	public List<Movie> getAllMoviesInCity(String locationId, Double rating) {

		List<Movie> movies = movieTheatresRepository.findByLocationId(locationId).stream().map(movieTheatre -> {
			return movieServie.getMovie(movieTheatre.getMovieId());
		}).collect(Collectors.toList());

		if (rating != null) {
			if (rating >= 0 && rating <= 10) {
				movies = movies.stream().filter(movie -> movie.getRating() >= rating).collect(Collectors.toList());
			} else {
				throw new CustomException("Rating must be in the range of 0-10");
			}
		}

		return movies;
	}

	private boolean isTheatreEmpty(Theatre theatre) {
		return (theatre == null || Commons.isStringEmpty(theatre.getTheatreName())
				|| this.isAddressEmpty(theatre.getAddress()) || theatre.getRating() == null);
	}

	private boolean isAddressEmpty(Address address) {
		return (address == null || Commons.isStringEmpty(address.getArea())
				|| Commons.isStringEmpty(address.getBuilding()) || Commons.isStringEmpty(address.getCity())
				|| Commons.isStringEmpty(address.getState()) || address.getPincode() == null);
	}

	@Override
	public boolean deleteAllTheatresWithLocationId(List<String> theatreIds) {
		for (String theatreId : theatreIds) {
			theatreRepository.deleteById(theatreId);
		}
		return true;
	}

	@Override
	public boolean removeShowsOfMovie(String locationId, String movieId) {
		List<String> theatreIds = locationService.getLocation(locationId).getTheatreIds();
		for (String theatreId : theatreIds) {
			Theatre theatre = this.getTheatre(theatreId);
			Set<Show> shows = theatre.getShows().stream().filter(show -> {
				return show.getMovie().getMovieId().equals(movieId);
			}).collect(Collectors.toSet());
			this.removeShows(theatre, shows);
		}

		return false;
	}

	private void removeShows(Theatre theatre, Set<Show> shows) {
		for (Show show : shows) {
			theatre.getShows().remove(show);
			showService.removeShow(show);
		}
		theatreRepository.save(theatre);
	}

	@Override
	public List<Theatre> getTheatresFromIdsForMovieInLocation(String locationId, String movieId) {
		return movieTheatresRepository.findByLocationIdAndMovieId(locationId, movieId)
				.orElseThrow(() -> new ResourceNotFoundException("No Theatres found")).getTheatreIds().keySet().stream()
				.map(theatreId -> {
					return this.getTheatre(theatreId);
				}).collect(Collectors.toList());
	}

	@Override
	public ShowDetails getShowFullDetails(String showId) {
		Show show = showService.getShow(showId);
		Theatre theatre = this.getTheatre(show.getTheatreId());

		ShowDetails response = new ShowDetails();
		response.setMovieName(show.getMovie().getMovieName());
		response.setShowStartTime(show.getShowStartTime());
		response.setShowEndTime(show.getShowEndTime());
		response.setCity(theatre.getAddress().getCity());
		response.setState(theatre.getAddress().getState());
		response.setTheatreName(theatre.getTheatreName());
		return response;
	}

	@Override
	public void updateMovieInShow(Movie movie) {

		movieTheatresRepository.findByMovieId(movie.getMovieId()).stream().forEach(movieTheatre -> {
			movieTheatre.getTheatreIds().keySet().stream().map(theatreId -> {
				return this.getTheatre(theatreId);
			}).forEach(theatre -> {
				theatre.getShows().stream().forEach(show -> {
					if (show.getMovie().getMovieId().equals(movie.getMovieId())) {
						show.setMovie(movie);
						show = showService.updateShow(show);
					}
				});
				theatreRepository.save(theatre);
			});
		});
	}
}
