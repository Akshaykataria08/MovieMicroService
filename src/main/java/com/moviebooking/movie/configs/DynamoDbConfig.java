package com.moviebooking.movie.configs;

import javax.annotation.PostConstruct;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.moviebooking.movie.domains.Location;
import com.moviebooking.movie.domains.Movie;
import com.moviebooking.movie.domains.MovieTheatres;
import com.moviebooking.movie.domains.Show;
import com.moviebooking.movie.domains.Theatre;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.moviebooking.movie.repositories")
public class DynamoDbConfig {

	@Value("${amazon.end-point.url}")
	private String awsDynamoDBEndPoint;

	@Value("${amazon.region}")
	private String region;

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, region))
				.build();
	}

	@PostConstruct
	private void setupTables() {
		AmazonDynamoDB amazonDynamoDB = amazonDynamoDB();
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

		CreateTableRequest cityTableRequest = mapper.generateCreateTableRequest(Location.class);
		cityTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		CreateTableRequest theatreTableRequest = mapper.generateCreateTableRequest(Theatre.class);
		theatreTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		CreateTableRequest movieTableRequest = mapper.generateCreateTableRequest(Movie.class);
		movieTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		CreateTableRequest showTableRequest = mapper.generateCreateTableRequest(Show.class);
		showTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		CreateTableRequest movieTheatresTableRequest = mapper.generateCreateTableRequest(MovieTheatres.class);
		movieTheatresTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		
//		TableUtils.deleteTableIfExists(amazonDynamoDB, mapper.generateDeleteTableRequest(Show.class));
//		TableUtils.deleteTableIfExists(amazonDynamoDB, mapper.generateDeleteTableRequest(Theatre.class));
//		TableUtils.deleteTableIfExists(amazonDynamoDB, mapper.generateDeleteTableRequest(Location.class));
//		TableUtils.deleteTableIfExists(amazonDynamoDB, mapper.generateDeleteTableRequest(Movie.class));
//		TableUtils.deleteTableIfExists(amazonDynamoDB, mapper.generateDeleteTableRequest(MovieTheatres.class));
		
		TableUtils.createTableIfNotExists(amazonDynamoDB, cityTableRequest);
		TableUtils.createTableIfNotExists(amazonDynamoDB, theatreTableRequest);
		TableUtils.createTableIfNotExists(amazonDynamoDB, movieTableRequest);
		TableUtils.createTableIfNotExists(amazonDynamoDB, showTableRequest);
		TableUtils.createTableIfNotExists(amazonDynamoDB, movieTheatresTableRequest);
		
	}
}
