package com.hpy.ops360.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomDateFormattor {

	public enum FormatStyle {
        FULL, SHORT, COMPACT, SYNERGY, DATABASE, HIMS_DATABASE
    }

    private static final DateTimeFormatter[] INPUT_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:s"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SS"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.S"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:s"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.S"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:s"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SS"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.S"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:s"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
    };

    public static String convert(String inputDateTime, FormatStyle style) {
    	
    	if (inputDateTime == null || inputDateTime.trim().isEmpty()) {
            log.warn("Input date-time is null or blank");
            return ""; // or return a placeholder like "N/A"
    	}
        log.info("Converting input date-time: " + inputDateTime);

        LocalDateTime dateTime = null;
        for (DateTimeFormatter formatter : INPUT_FORMATTERS) {
            try {
                dateTime = LocalDateTime.parse(inputDateTime, formatter);
                log.info("Parsed using format: " + formatter);
                break;
            } catch (Exception e) {
                // Continue trying other formats
            }
        }

        if (dateTime == null) {
            log.error("Invalid date-time format: " + inputDateTime);
            throw new IllegalArgumentException("Unsupported date-time format: " + inputDateTime);
        }

        DateTimeFormatter outputFormatter;
        switch (style) {
            case FULL:
                outputFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy, hh:mm a");
                break;
            case SHORT:
                outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
                break;
            case COMPACT:
                outputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a");
                break;
            case SYNERGY:
                outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                break;
            case DATABASE:
            	outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            	break;
            case HIMS_DATABASE:
            	outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
            			Locale.ENGLISH);
            	break;
            default:
                outputFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy, hh:mm a");
        }
        String outputFormat = dateTime.format(outputFormatter);
        log.info("Formated Output Date String: {}",outputFormat);
        return outputFormat;
    }
}

