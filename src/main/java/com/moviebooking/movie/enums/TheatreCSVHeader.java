package com.moviebooking.movie.enums;

public enum TheatreCSVHeader {

	THEATRE_NAME("Theatre name"), RATING("rating"), BUILDING("Building"), AREA("Area"), CITY("City"), STATE("State"),
	PINCODE("Pincode");

	private String csvHeader;

	private TheatreCSVHeader(String csvHeader) {
		this.csvHeader = csvHeader;
	}

	public String getCsvHeader() {
		return this.csvHeader;
	}
}
