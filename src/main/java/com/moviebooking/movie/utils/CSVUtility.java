package com.moviebooking.movie.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.moviebooking.movie.exceptions.CustomException;

public class CSVUtility {

	private static String mimeType = "text/csv";
	
	private static final String CSV_IN_CELL_DELIMITER = ",";
	
	public static boolean isCSV(MultipartFile file) {
		return mimeType.equals(file.getContentType());
	}
	
	public static Iterable<CSVRecord> getCSVRecords(MultipartFile file) throws IOException {
		if(!isCSV(file)) {
			throw new CustomException("File Tye Should be CSV");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
		Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		csvParser.close();
		reader.close();
		return csvRecords;
	}
	
	public static List<String> csvCellToStringList(String string) {

		List<String> ret = new ArrayList<String>();
		String[] values = string.split(CSV_IN_CELL_DELIMITER);
		ret = Arrays.asList(values);
		return ret.stream().map(StringUtils::trim).filter(chunk -> !chunk.isEmpty()).collect(Collectors.toList());
	}
}
