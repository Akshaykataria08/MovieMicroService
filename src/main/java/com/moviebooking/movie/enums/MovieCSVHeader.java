package com.moviebooking.movie.enums;

public enum MovieCSVHeader {

	MOVIE_NAME("Movie Name"), RATING("Rating"), RELEASE_DATE("Release Date"), DURATION("Duration"),
	LANGUAGE("Language"), GENRE("Genre"), CAST("Cast");

	private String csvHeader;

	private MovieCSVHeader(String csvHeader) {
		this.csvHeader = csvHeader;
	}

	public String getCsvHeader() {
		return csvHeader;
	}
}
