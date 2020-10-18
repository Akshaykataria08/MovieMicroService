package com.moviebooking.movie.utils;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class Commons {

	public static boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	public static boolean isStringEmpty(String str) {
		return StringUtils.stripToEmpty(str).isEmpty();
	}
	
	public static <E> boolean isListEmpty(Collection<E> collection) {
		return collection == null || collection.isEmpty();
	}
}
