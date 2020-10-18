package com.moviebooking.movie.configs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moviebooking.movie.enums.Genre;
import com.moviebooking.movie.enums.Language;
import com.moviebooking.movie.exceptions.CustomException;
import com.moviebooking.movie.exceptions.InternalServerErrorException;

@Configuration
public class BeansDTOMapperConfig {

	@Value("${spring.jackson.date-format: yyyy-MM-dd HH:mm}")
	String dateFormat;
	
	private SimpleDateFormat sdf;
	
	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);

		this.convertStringToLanguageEnum(mapper);
		this.convertStringToGenreEnum(mapper);
		this.convertStringToDuration(mapper);
		this.convertStringToCalendar(mapper);
		
		this.convertLanguageToString(mapper);
		this.convertGenreToString(mapper);
		this.convertDurationToString(mapper);
		this.convertCalendarToString(mapper);
		return mapper;
	}

	private void convertCalendarToString(ModelMapper mapper) {
		mapper.createTypeMap(Calendar.class, String.class).setConverter(context -> {
			if(context.getSource() == null) {
				throw new InternalServerErrorException("Something happend at our end");
			}
			sdf = new SimpleDateFormat(dateFormat);
			return sdf.format(context.getSource().getTime());
		});
	}

	private void convertStringToCalendar(ModelMapper mapper) {
		mapper.createTypeMap(String.class, Calendar.class).setConverter(context -> {
			if(context.getSource() == null || context.getSource().isEmpty()) {
				throw new CustomException("Please specify date");
			}
			sdf = new SimpleDateFormat(dateFormat);
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeZone(TimeZone.getDefault());
			try {
				calendar.setTime(sdf.parse(context.getSource()));
			} catch (ParseException e) {
				throw new CustomException("Unparsable date: " + context.getSource());
			}
			return calendar;
		});
	}

	private void convertLanguageToString(ModelMapper mapper) {

		mapper.createTypeMap(Language.class, String.class).setConverter(context -> {
			if (context.getSource() == null) {
				throw new InternalServerErrorException("Something happend at our end");
			}
			return WordUtils.capitalizeFully(context.getSource().name());
		});
	}

	private void convertGenreToString(ModelMapper mapper) {
		mapper.createTypeMap(Genre.class, String.class).setConverter(context -> {
			if (context.getSource() == null) {
				throw new InternalServerErrorException("Something happend at our end");
			}
			return WordUtils.capitalizeFully(context.getSource().getValue());
		});
	}

	private void convertStringToGenreEnum(ModelMapper mapper) {
		mapper.createTypeMap(String.class, Genre.class).setConverter(context -> {
			if (context.getSource() == null || context.getSource().isEmpty()) {
				throw new CustomException("Please specify Language");
			}
			HashMap<String, Genre> valueMap = new HashMap<>();
			for (Genre genre : Genre.values()) {
				valueMap.put(genre.getValue().toUpperCase(), genre);
			}
			return valueMap.get(StringUtils.upperCase(context.getSource()));
		});
	}

	private void convertStringToLanguageEnum(ModelMapper mapper) {
		mapper.createTypeMap(String.class, Language.class).setConverter(context -> {
			if (context.getSource() == null || context.getSource().isEmpty()) {
				throw new CustomException("Please specify Language");
			}
			return Language.valueOf(StringUtils.upperCase(context.getSource()));
		});
	}

	private void convertStringToDuration(ModelMapper mapper) {

		mapper.createTypeMap(String.class, Duration.class).setConverter(context -> {
			String durationString = context.getSource();
			String[] time = durationString.split(":");
			try {
				long hours = Long.parseLong(time[0].trim());
				long minutes = Long.parseLong(time[1].trim());
				return Duration.parse("PT" + hours + "H" + minutes + "M");
			} catch (Exception e) {
				throw new CustomException("Unparsable Duration");
			}
		});
	}

	private void convertDurationToString(ModelMapper mapper) {

		mapper.createTypeMap(Duration.class, String.class).setConverter(context -> {
			StringBuilder strBuilder = new StringBuilder();
			long hours = context.getSource().toHours();
			long minutes = context.getSource().toMinutes();
			minutes = minutes % 60;
			strBuilder.append(hours);
			strBuilder.append(":");
			strBuilder.append(minutes);
			return strBuilder.toString();
		});
	}
}
