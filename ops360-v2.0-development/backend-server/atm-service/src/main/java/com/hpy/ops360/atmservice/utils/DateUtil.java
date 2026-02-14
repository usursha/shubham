package com.hpy.ops360.atmservice.utils;
//import java.text.SimpleDateFormat;

//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.Date;
//import java.util.Locale;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private static final DateTimeFormatter inputFormatterHims = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy, hh.mm a");
	private static final DateTimeFormatter SP_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
			Locale.ENGLISH);

	public static String formatDateString(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormatter);
			return dateTime.format(outputFormatter);
		} catch (DateTimeParseException e) {
			logger.error("Failed to parse date string: {}", dateStr, e);
			return "";
		}
	}

	// Index of ATM format start
	public static String formatDateStringIndexOfATM(String dateStr) {
	    if (dateStr == null || dateStr.trim().isEmpty()) {
	        return "";
	    }

	    List<DateTimeFormatter> formatters = Arrays.asList(
	    	    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
	    	    new DateTimeFormatterBuilder()
	    	        .appendPattern("yyyy-MM-dd HH:mm:ss")
	    	        .optionalStart()
	    	        .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
	    	        .optionalEnd()
	    	        .toFormatter(),
	    	    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
	    	);


	    for (DateTimeFormatter formatter : formatters) {
	        try {
	            LocalDateTime dt = LocalDateTime.parse(dateStr, formatter);
	            return formatGroupDateIndexOfAtm(dt);
	        } catch (DateTimeParseException ignored) {
	            // Try next format
	        }
	    }


	    logger.error("Failed to parse date string In Index of ATM: {}", dateStr);
	    return "Unrecognized date format: " + dateStr;
	}


	public static String formatGroupDateIndexOfAtm(LocalDateTime date) {
		return date.format(SP_FORMATTER);
	}

//  		public static String formatFullTimestamp(Date date) {
//  		    if (date == null) {
//  		        return "";
//  		    }
//  		    try {
//  		        // Desired output: "09 Dec 2024, 08.37 PM"
//  		        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, hh.mm a", Locale.ENGLISH);
//  		        return outputFormat.format(date);
//  		    } catch (Exception e) {
//  		        logger.error("Failed to format full timestamp from Date: {}", date, e);
//  		        return "";
//  		    }
//  		}

	public static String formatFullTimestamp(Date date) {
		if (date == null) {
			log.warn("Input date for formatFullTimestamp is null. Returning empty string.");
			return "";
		}
		try {
			LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			// Desired output: "09 Dec 2024, 08.37 PM"
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh.mm a", Locale.ENGLISH);

			String formattedDate = localDateTime.format(outputFormatter);
			log.debug("Formatted date '{}' to '{}'", date, formattedDate);
			return formattedDate;
		} catch (Exception e) {
			log.error("Failed to format full timestamp from Date: {}. Error: {}", date, e.getMessage(), e);
			return "";
		}

	}
	
//	---------------------------------------------------------------------------------------
	// Input formatters to handle different variations
	private static final List<DateTimeFormatter> inputFormattersss = Arrays.asList(
	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),      // 2024-12-09 21:14:11.917
	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),       // 2024-12-09 21:14:11.91
	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),        // 2024-12-09 21:14:11.9
	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),          // 2024-12-09 21:14:11
	    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0")         // 2024-12-09 21:14:11.0
	);

	/**
	 * Format date string from input format to output format
	 * Input: "2024-12-09 21:14:11.917"
	 * Output: "09 Dec '24, 09.14 PM"
	 */
	public static String formatCallDate(String inputDateString) {
	    if (inputDateString == null || inputDateString.trim().isEmpty()) {
	        return "";
	    }
	    
	    try {
	        // Clean and parse the input date
	        String cleanedInput = cleanDateString(inputDateString.trim());
	        LocalDateTime dateTime = parseWithMultipleFormats(cleanedInput);
	        
	        // Format to output format
	        return dateTime.format(outputFormatter);
	        
	    } catch (Exception e) {
	        log.warn("Failed to format date '{}': {}", inputDateString, e.getMessage());
	        return inputDateString; // Return original if formatting fails
	    }
	}

	/**
	 * Parse date string using multiple formatters
	 */
	private static LocalDateTime parseWithMultipleFormats(String dateString) throws DateTimeParseException {
	    for (DateTimeFormatter formatter : inputFormattersss) {
	        try {
	            return LocalDateTime.parse(dateString, formatter);
	        } catch (DateTimeParseException e) {
	            // Continue to next formatter
	            continue;
	        }
	    }
	    
	    throw new DateTimeParseException(
	        "Unable to parse date string: " + dateString, 
	        dateString, 0
	    );
	}

	/**
	 * Clean date string to handle microseconds
	 */
	private static String cleanDateString(String dateStr) {
	    if (dateStr == null) {
	        return null;
	    }
	    
	    String cleaned = dateStr.trim();
	    
	    // Handle microseconds by truncating to milliseconds
	    if (cleaned.contains(".")) {
	        String[] parts = cleaned.split("\\.");
	        if (parts.length == 2) {
	            String datePart = parts[0];
	            String fractionalPart = parts[1];
	            
	            // Remove any non-numeric characters from fractional part
	            fractionalPart = fractionalPart.replaceAll("[^0-9]", "");
	            
	            // Ensure fractional part is exactly 3 digits (milliseconds)
	            if (fractionalPart.length() == 1) {
	                fractionalPart = fractionalPart + "00";
	            } else if (fractionalPart.length() == 2) {
	                fractionalPart = fractionalPart + "0";
	            } else if (fractionalPart.length() > 3) {
	                fractionalPart = fractionalPart.substring(0, 3);
	            }
	            
	            cleaned = datePart + "." + fractionalPart;
	        }
	    }
	    
	    return cleaned;
	}
	

}