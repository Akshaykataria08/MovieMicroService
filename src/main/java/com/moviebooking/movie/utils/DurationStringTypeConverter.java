package com.moviebooking.movie.utils;

import java.time.Duration;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class DurationStringTypeConverter implements DynamoDBTypeConverter<String, Duration>{

	@Override
	public String convert(Duration duration) {
		return duration.toString();
	}

	@Override
	public Duration unconvert(String duration) {
		return Duration.parse(duration);
	}

}
