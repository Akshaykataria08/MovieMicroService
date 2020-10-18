package com.moviebooking.movie.enums;

public enum Genre {
	THRILLER("Thriller"), COMEDY("Comedy"), ROMANCE("Romance"), ACTION("Action"), SCI_FI("Sci-Fi"), ADVENTURE("Adventure"), HORROR("Horror"), DRAMA("Drama"), SPORT("Sport"), MYSTERY("Mystery");

	private String value;
	
	private Genre(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
