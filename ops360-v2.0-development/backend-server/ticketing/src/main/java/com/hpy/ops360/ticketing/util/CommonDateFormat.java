package com.hpy.ops360.ticketing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CommonDateFormat {

	public String getStringFormattedDate(String inputDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM ''yy, hh:mm a");
		Date parsed;
		try {
			// Parsing the input date string
			parsed = inputFormat.parse(inputDate);
			log.info("Parsed date:{}", parsed);

			// Formatting the parsed date to the desired format
			String formattedDate = outputFormat.format(parsed);
			log.info("Formatted date:{}", formattedDate);

			return formattedDate;
		} catch (ParseException e) {

			log.error("Invalid date format:{}", e);
		}
		return "";
	}

	public Date getFormattedDate(String inputDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM ''yy, hh:mm a");
		Date parsed = new Date();
		try {
			// Parsing the input date string
			parsed = inputFormat.parse(inputDate);
			log.debug("Parsed date: " + parsed);

			// Formatting the parsed date to the desired format
			String formattedDate = outputFormat.format(parsed);
			log.debug("Formatted date: " + formattedDate);

			// Parsing the formatted date back to a Date object (not necessary)
			Date outputParsed = outputFormat.parse(formattedDate);
			log.debug("Parsed back to Date: " + outputParsed);
			return parsed;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsed;

	}

	public LocalDate getLocalDateFormat(String inputDate) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			LocalDate parsedDate = LocalDate.parse(inputDate, inputFormatter);
			log.debug("parsedDate" + parsedDate);
//			String formattedDate = outputFormatter.format(parsedDate);
//			log.debug("Formatted date: " + formattedDate);
			return parsedDate;
		} catch (DateTimeParseException e) {
			System.err.println("Error parsing date: " + e.getMessage());
		}

		return null;
	}

	public LocalDateTime getLocalDateTimeFormat(String inputDate) {
	    if (inputDate == null || inputDate.trim().isEmpty()) {
	        return null;
	    }

	    List<DateTimeFormatter> possibleFormatters = List.of(
		        DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH), // e.g. 12/06/2025 03:52 PM
		        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")    
		        //dd/MM/yyyy H:mm// e.g. 12/06/2025 15:52
		    );

	    for (DateTimeFormatter formatter : possibleFormatters) {
	        try {
	            return LocalDateTime.parse(inputDate, formatter);
	        } catch (DateTimeParseException ignored) {
	            // Try next format
	        }
	    }

	    throw new IllegalArgumentException("Unrecognized date format: " + inputDate);
	}

	
	public LocalDateTime getLocalDateTimeFormatCm(String inputDate) {
		try {
			if (inputDate == null || inputDate.trim().isEmpty()) {
				return null;
			}

			DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd/MM/").optionalStart()
					.appendPattern("yyyy").optionalEnd().optionalStart().appendPattern("yy").optionalEnd()
					.appendPattern(" HH:mm").toFormatter();
			return LocalDateTime.parse(inputDate, formatter);

		} catch (DateTimeParseException e) {
			log.error("Error parsing date: {} - {}", inputDate, e.getMessage());
			throw new DateTimeParseException("Invalid date format. Please use format: dd/MM/yy HH:mm", inputDate,
					e.getErrorIndex());
		}
	}
	
	public static LocalDateTime parseToLocalDateTimeCm(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        try {
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + input);
            return null;
        }
    }

	public String getDownTimeInHrs(int downtimeInMins) {
		return String.format("%d Hrs", downtimeInMins / 60);
	}

}
