package com.moviebooking.movie.utils;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.moviebooking.movie.enums.Language;

public class LanguageEnumListTypeConverter implements DynamoDBTypeConverter<List<String>, List<Language>>{

	@Override
	public List<String> convert(List<Language> genres) {
		List<String> ret = new ArrayList<String>();
		for(Language genre: genres) {
			ret.add(genre.toString());
		}
		return ret;
	}

	@Override
	public List<Language> unconvert(List<String> genres) {
		List<Language> ret = new ArrayList<>();
		for(String genre: genres) {
			ret.add(Language.valueOf(genre));
		}
		return ret;
	}

}
