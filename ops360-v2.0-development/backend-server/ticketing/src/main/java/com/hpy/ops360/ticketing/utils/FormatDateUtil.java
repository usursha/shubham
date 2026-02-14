package com.hpy.ops360.ticketing.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FormatDateUtil {
	
    public String convertToCustomFormat(String inputDateTime) {
    	log.info("****Inside convertToCustomFormat Service *******");
        log.debug("Converting input date-time: {}", inputDateTime);

        // Define potential input formats
        DateTimeFormatter[] inputFormatters = {
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
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        };

        // Attempt parsing with the provided formats
        LocalDateTime dateTime = null;
        for (DateTimeFormatter formatter : inputFormatters) {
            try {
            	log.info("Input Formatter take:- "+ dateTime);
            	log.info("Input Formatter take:- "+ inputDateTime);
                dateTime = LocalDateTime.parse(inputDateTime, formatter);
                log.debug("Parsed input date-time successfully using format: {}", formatter.toString());
                break;
            } catch (Exception e) {
                log.debug("Failed to parse using format: {}", formatter.toString());
            }
        }

        if (dateTime == null) {
            log.error("Invalid date-time format encountered: {}", inputDateTime);
            throw new IllegalArgumentException("Invalid date-time format: " + inputDateTime);
        }

        // Define the output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy, hh:mm a");
        DateTimeFormatter outputFormatter1 = DateTimeFormatter.ofPattern("dd-MMMM-yy, hh:mm a");
        DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("dd-MMMM-yy, hh:mm A");
        DateTimeFormatter outputFormatter3 = DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a");
        DateTimeFormatter outputFormatter4 = DateTimeFormatter.ofPattern("dd-MMM-yy, hh:mm a");
        DateTimeFormatter outputformatter5 = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a");
        DateTimeFormatter outputformatter6 = DateTimeFormatter.ofPattern("dd MMMM ''yy, hh:mm a");
        // Format the date-time and return
        String formattedDate = dateTime.format(outputFormatter);
        log.debug("Formatted date-time: {}", formattedDate);
        return formattedDate;
    }

    
}