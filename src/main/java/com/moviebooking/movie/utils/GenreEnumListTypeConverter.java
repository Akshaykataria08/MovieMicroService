package com.moviebooking.movie.utils;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.moviebooking.movie.enums.Genre;

public class GenreEnumListTypeConverter implements DynamoDBTypeConverter<List<String>, List<Genre>>{

	@Override
	public List<String> convert(List<Genre> genres) {
		List<String> ret = new ArrayList<String>();
		for(Genre genre: genres) {
			ret.add(genre.toString());
		}
		return ret;
	}

	@Override
	public List<Genre> unconvert(List<String> genres) {
		List<Genre> ret = new ArrayList<>();
		for(String genre: genres) {
			ret.add(Genre.valueOf(genre));
		}
		return ret;
	}
}
