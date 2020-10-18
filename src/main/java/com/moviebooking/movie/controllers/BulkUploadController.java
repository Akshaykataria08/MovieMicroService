package com.moviebooking.movie.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moviebooking.movie.services.BulkUploadService;

@RestController
@RequestMapping("/v1/upload/csv")
public class BulkUploadController {

	@Autowired
	private BulkUploadService bulkUploadService;

	@PostMapping("/location")
	public boolean uploadLocations(@RequestParam("file") MultipartFile file) throws IOException {
		return bulkUploadService.uploadLocationCSV(file);
	}

	@PostMapping("/theatre")
	public boolean uploadTheatreCSV(@RequestParam("file") MultipartFile file) throws IOException {
		return bulkUploadService.uploadTheatreCSV(file);
	}

	@PostMapping("/movie")
	public boolean uploadMovieCSV(@RequestParam("file") MultipartFile file) throws IOException {
		return bulkUploadService.uploadMovieCSV(file);
	}

	@PostMapping("/show")
	public boolean uploadShowCSV(@RequestParam("file") MultipartFile file) throws IOException {
		return bulkUploadService.uploadShowCSV(file);
	}
}
