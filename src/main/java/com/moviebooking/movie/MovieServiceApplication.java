package com.moviebooking.movie;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MovieServiceApplication {

	@Value("${spring.jackson.time-zone: Asia/Calcutta}")
	String timeZone;
	
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}

}
