package com.hpy.ops360.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy, hh.mm a");
    private static final DateTimeFormatter outputFormatterForFullMonth = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh.mm a");
    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale.ENGLISH);
    private static final DateTimeFormatter lastUpdatedDateformatter = DateTimeFormatter.ofPattern("MMM’yy; hh:mm a");

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

    public static String formatIsoToCustomDate(String isoDateStr) {
        if (isoDateStr == null || isoDateStr.trim().isEmpty()) {
            return "";
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateStr, isoFormatter);
            return dateTime.format(customFormatter);
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse ISO date string: {}", isoDateStr, e);
            return "";
        }
    }
    
    
    public static String formatPreviousDayTimestamp() {
        try {
            LocalDateTime previousDay = LocalDateTime.now()
                    .minusDays(1)
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(0)
                    .withSecond(59)
    	            .withNano(0);  // This line removes the nanoseconds
            String formattedDate = previousDay.format(outputFormatterForFullMonth);
         // Ensure only "am/pm" is in uppercase
            String[] parts = formattedDate.split(" ");
            String timeWithAmPm = parts[parts.length - 1].toUpperCase(); // Convert only the AM/PM part to uppercase
            parts[parts.length - 1] = timeWithAmPm;
            formattedDate = String.join(" ", parts);
            return formattedDate;
        } catch (Exception e) {
            logger.error("Failed to format previous day timestamp", e);
            return "";
        }
    }
    
    public static String getFormattedLastUpdatedDateTime() {
        // Get current time
        LocalDateTime now = LocalDateTime.now();

        // Subtract one day and set time to 23:59
        LocalDateTime yesterday = now.minusDays(1).withHour(23).withMinute(59).withSecond(0).withNano(0);

        // Get the day of the month with suffix
        int dayOfMonth = yesterday.getDayOfMonth();
        String daySuffix = getDaySuffix(dayOfMonth);
        String formattedDate=yesterday.format(lastUpdatedDateformatter);
        
        // Ensure only "am/pm" is in uppercase
        String[] parts = formattedDate.split(" ");
        String timeWithAmPm = parts[parts.length - 1].toUpperCase(); // Convert only the AM/PM part to uppercase
        parts[parts.length - 1] = timeWithAmPm;
        formattedDate = String.join(" ", parts);
        // Format the rest of the date
        
        return dayOfMonth + daySuffix + " " + formattedDate;
    }
    
    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
}