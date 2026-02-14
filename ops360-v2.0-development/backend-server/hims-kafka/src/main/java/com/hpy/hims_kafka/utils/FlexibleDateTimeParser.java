package com.hpy.hims_kafka.utils;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FlexibleDateTimeParser {

    private static final List<DateTimeFormatter> formatters = Arrays.asList(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ofPattern("HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("HH:mm:ss"),
        DateTimeFormatter.ofPattern("HH:mm.s"),
        DateTimeFormatter.ofPattern("HH:mm")
    );

    public static LocalDateTime parse(String input) {
        if (input == null || input.isBlank()) return null;

        for (DateTimeFormatter formatter : formatters) {
            try {
                if (formatter.toString().contains("H") && !formatter.toString().contains("y")) {
                    // Time-only format: combine with today's date
                    LocalTime time = LocalTime.parse(input, formatter);
                    return time.atDate(LocalDate.now());
                } else {
                    return LocalDateTime.parse(input, formatter);
                }
            } catch (Exception ignored) {}
        }

        // Log or handle unrecognized format
        return null;
    }
}
