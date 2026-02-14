package com.hpy.ops360.ticketing.utils;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DateStringCleaner {
    
    // Regex patterns to clean unwanted characters
    private static final Pattern CARRIAGE_RETURN_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
    private static final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("\\s+");
    private static final Pattern UNWANTED_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s:,.']+");
    private static final Pattern DUPLICATE_TIME_PATTERN = Pattern.compile("(\\d{2})\\s+(\\d{2})\\s+");
    
    /**
     * Clean date string from stored procedure output
     * Removes \r\n, extra spaces, and unwanted characters
     * 
     * @param dateString Raw date string from stored procedure
     * @return Cleaned date string
     */
    public static String cleanDateTimeString(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return "";
        }
        
        try {
            String cleaned = dateString;
            
            log.debug("Original date string: '{}'", dateString);
            
            // Step 1: Remove carriage returns and newlines
            cleaned = CARRIAGE_RETURN_PATTERN.matcher(cleaned).replaceAll(" ");
            
            // Step 2: Remove unwanted special characters (keep only alphanumeric, spaces, colon, comma, period, apostrophe)
            cleaned = UNWANTED_CHARS_PATTERN.matcher(cleaned).replaceAll(" ");
            
            // Step 3: Fix duplicate numbers (like "06 55" should be "06:55")
            cleaned = DUPLICATE_TIME_PATTERN.matcher(cleaned).replaceAll("$1:$2 ");
            
            // Step 4: Replace multiple spaces with single space
            cleaned = EXTRA_SPACES_PATTERN.matcher(cleaned).replaceAll(" ");
            
            // Step 5: Trim leading/trailing spaces
            cleaned = cleaned.trim();
            
            // Step 6: Handle specific pattern where time gets duplicated
            // Example: "19 Aug '2025, 06:55 55 AM" -> "19 Aug '2025, 06:55 AM"
            cleaned = removeDuplicateTimeComponents(cleaned);
            
            // Step 7: Remove apostrophe from year
            // Example: "19 Aug '2025, 06:55 AM" -> "19 Aug 2025, 06:55 AM"
            cleaned = removeApostropheFromYear(cleaned);
            
            log.debug("Cleaned date string: '{}'", cleaned);
            
            return cleaned;
            
        } catch (Exception e) {
            log.error("Error cleaning date string: '{}'", dateString, e);
            return dateString.trim(); // Return original trimmed string if cleaning fails
        }
    }
    
    /**
     * Remove apostrophe from year in date string
     * Example: "19 Aug '2025, 06:55 AM" -> "19 Aug 2025, 06:55 AM"
     */
    private static String removeApostropheFromYear(String dateString) {
        if (dateString == null) {
            return "";
        }
        
        try {
            // Pattern to match apostrophe before 4-digit year
            // Example: '2025 -> 2025, '24 -> 24, '2024 -> 2024
            Pattern apostropheYearPattern = Pattern.compile("'(\\d{2,4})");
            
            String result = apostropheYearPattern.matcher(dateString).replaceAll("$1");
            
            return result;
            
        } catch (Exception e) {
            log.warn("Error removing apostrophe from year in: '{}'", dateString, e);
            return dateString;
        }
    }

    /**
     * Remove duplicate time components from date string
     * Example: "19 Aug '2025, 06:55 55 AM" -> "19 Aug '2025, 06:55 AM"
     * Example: "19 Aug '2025, 06:55:55 AM" -> "19 Aug '2025, 06:55 AM"
     */
    private static String removeDuplicateTimeComponents(String dateString) {
        if (dateString == null) {
            return "";
        }
        
        try {
            String result = dateString;
            
            // Pattern 1: Remove duplicate seconds in HH:MM:SS format where SS matches MM
            // Example: "06:55:55 AM" -> "06:55 AM"
            Pattern duplicateSecondsPattern = Pattern.compile(
                "(\\d{1,2}:\\d{2}):(\\d{2})\\s+(AM|PM)", 
                Pattern.CASE_INSENSITIVE
            );
            result = duplicateSecondsPattern.matcher(result).replaceAll("$1 $3");
            
            // Pattern 2: Remove duplicate minutes with space: "HH:MM XX AM/PM" where XX is duplicate minutes
            Pattern duplicateMinutesPattern = Pattern.compile(
                "(\\d{1,2}:\\d{2})\\s+(\\d{2})\\s+(AM|PM)", 
                Pattern.CASE_INSENSITIVE
            );
            result = duplicateMinutesPattern.matcher(result).replaceAll("$1 $3");
            
            // Pattern 3: Handle pattern: "HH MM XX AM/PM" (without colon)
            Pattern duplicateMinutesNoColonPattern = Pattern.compile(
                "(\\d{1,2})\\s+(\\d{2})\\s+\\2\\s+(AM|PM)", 
                Pattern.CASE_INSENSITIVE
            );
            result = duplicateMinutesNoColonPattern.matcher(result).replaceAll("$1:$2 $3");
            
            // Pattern 4: Remove any trailing seconds that might remain
            // Example: "06:55:30 AM" -> "06:55 AM"
            Pattern anySecondsPattern = Pattern.compile(
                "(\\d{1,2}:\\d{2}):\\d{2}\\s+(AM|PM)", 
                Pattern.CASE_INSENSITIVE
            );
            result = anySecondsPattern.matcher(result).replaceAll("$1 $2");
            
            return result;
            
        } catch (Exception e) {
            log.warn("Error removing duplicate time components from: '{}'", dateString, e);
            return dateString;
        }
    }
    
    /**
     * Validate if the cleaned date string looks reasonable
     */
    public static boolean isValidDateTimeFormat(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - should contain date pattern and time pattern
        String cleaned = dateString.trim();
        
        // Should contain month abbreviation, year (with or without apostrophe), and AM/PM
        return cleaned.matches(".*\\d{1,2}\\s+\\w{3}\\s+'?\\d{2,4}.*") && 
               cleaned.matches(".*(AM|PM).*") &&
               cleaned.length() > 10 && 
               cleaned.length() < 50;
    }
}

