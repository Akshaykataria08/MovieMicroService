package com.moviebooking.movie.enums;

public enum ShowCSVHeader {

	THEATRE_ID("Theatre Id"), MOVIE_ID("Movie Id"), SHOW_START_TIME("Show Start Time"), SHOW_END_TIME("Show End Time"),
	LANGUAGE("Language");

	private String csvHeader;

	private ShowCSVHeader(String csvHeader) {
		this.csvHeader = csvHeader;
	}

	public String getCsvHeader() {
		return csvHeader;
	}
}
