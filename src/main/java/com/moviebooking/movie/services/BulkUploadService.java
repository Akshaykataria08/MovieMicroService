package com.moviebooking.movie.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface BulkUploadService {

	boolean uploadTheatreCSV(MultipartFile file) throws IOException;

	boolean uploadLocationCSV(MultipartFile file) throws IOException;

	boolean uploadMovieCSV(MultipartFile file) throws IOException;

	boolean uploadShowCSV(MultipartFile file) throws IOException;

}
