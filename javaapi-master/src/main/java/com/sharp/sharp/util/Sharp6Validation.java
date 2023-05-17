package com.sharp.sharp.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class Sharp6Validation {

	public static boolean isEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns true of the map is null or is empty.
	 * 
	 * @param map
	 * @return true | false
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns true if the objet is null.
	 * 
	 * @param object
	 * @return true | false
	 */
	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns true if the input array is null or its length is zero.
	 * 
	 * @param array
	 * @return true | false
	 */
	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns true if the input string is null or its length is zero.
	 * 
	 * @param string
	 * @return true | false
	 */
	public static boolean isEmpty(String string) {
		if (string == null || string.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String getGMTTimeStampWithoutZone() {
		SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		// System.out.println("Current Date and Time in GMT time zone: " +
		// gmtDateFormat.format(new Date()).toString());
		return gmtDateFormat.format(new Date()).toString();
	}
}
