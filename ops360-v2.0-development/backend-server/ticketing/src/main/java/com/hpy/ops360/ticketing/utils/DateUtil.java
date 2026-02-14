package com.hpy.ops360.ticketing.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.hpy.ops360.ticketing.logapi.Loggable;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DateUtil {

	public static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	// Input formatters
	public static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static final DateTimeFormatter inputFormatterCm = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static final DateTimeFormatter dateGroupFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy");
	private static final DateTimeFormatter SP_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
			Locale.ENGLISH);
	private static final DateTimeFormatter SYNERGY_FORMATTER = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a",
			Locale.ENGLISH);

	public static final List<DateTimeFormatter> INPUT_FORMATTERS = Arrays.asList(
			DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
			DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a"), DateTimeFormatter.ofPattern("dd MMMM, yyyy, hh:mm a") // Added
																														// new
																														// format
	);
	// Output formatters
	public static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yyyy, hh:mm a");
	public static final DateTimeFormatter outputFormatterCm = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a");
	public static final DateTimeFormatter outputFormatterfordate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static final DateTimeFormatter etaOutputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
			Locale.ENGLISH);
	public static final DateTimeFormatter etaOutputFormatters = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a");

	// YYYY-MM-DD'T'HH:MM:SS
	public static String formatDateString(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDate dt = LocalDate.parse(dateStr, inputFormatter);
//			String newDate = formatGroupDate(dt);
//			LocalDate dt1 = LocalDate.parse(newDate, dateGroupFormatter);
			return formatGroupDate(dt);
		} catch (DateTimeParseException e) {
			logger.error("Failed to parse date string: {}", dateStr, e);
			return "";
		}
	}
	    //Index of ATM format  start 
		public static String formatDateStringIndexOfATM(String dateStr) {
			if (dateStr == null || dateStr.trim().isEmpty()) {
				return "";
			}
			try {
				LocalDateTime dt = LocalDateTime.parse(dateStr, inputFormatter);
//				String newDate = formatGroupDate(dt);
//				LocalDate dt1 = LocalDate.parse(newDate, dateGroupFormatter);
				return formatGroupDateIndexOfAtm(dt);
			} catch (DateTimeParseException e) {
				logger.error("Failed to parse date string In Index of ATM: {}", dateStr, e);
				return e.getMessage();
			}
		}
		
		public static String formatGroupDateIndexOfAtm(LocalDateTime date) {
			return date.format(SP_FORMATTER);
		}
		
		 //Index of ATM format  start 
		
	
	public static String formatDateStringCe(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDateTime dt = LocalDateTime.parse(dateStr, inputFormatter);
//			String newDate = formatGroupDate(dt);
//			LocalDate dt1 = LocalDate.parse(newDate, dateGroupFormatter);
			return dt.format(outputFormatter);
		} catch (DateTimeParseException e) {
			logger.error("Failed to parse date string: {}", dateStr, e);
			return "";
		}
	}
	
	//----hims-----start
	
	 public static String formatDateStringCehims(String dateStr) {
	        if (dateStr == null || dateStr.trim().isEmpty()) {
	            return "";
	        }
	        try {
	            LocalDateTime dt = LocalDateTime.parse(dateStr, inputFlexibleFormatter);
	            return dt.format(outputFormatters);
	        } catch (DateTimeParseException e) {
	            logger.error("Failed to parse date string: {}", dateStr, e);
	            return "";
	        }
	    }

	    public static final DateTimeFormatter inputFormatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	    public static final DateTimeFormatter inputHimsFormatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	    static DateTimeFormatter inputFlexibleFormatter = new DateTimeFormatterBuilder()
	    	    .appendPattern("yyyy-MM-dd HH:mm:ss")
	    	    .optionalStart()
	    	    .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
	    	    .optionalEnd()
	    	    .toFormatter();

	    public static final DateTimeFormatter outputFormatters = DateTimeFormatter.ofPattern("dd MMM ''yyyy, hh:mm a");

	    
	    
	    //---hims-----end
	    
	    
	
	public static String formatDateStringCeCreatedDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDateTime dt = LocalDateTime.parse(dateStr, inputFormatter);
//			String newDate = formatGroupDate(dt);
//			LocalDate dt1 = LocalDate.parse(newDate, dateGroupFormatter);
			return dt.format(outputFormatterCm);
		} catch (DateTimeParseException e) {
			logger.error("Failed to parse date string: {}", dateStr, e);
			return "";
		}
	}
	
	
	public static String formatDateStringCm(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormatter);
			return dateTime.format(outputFormatterCm);
		} catch (DateTimeParseException e) {
			logger.error("Failed to parse date string: {}", dateStr, e);
			return "";
		}
	}

	public static String formatDateStringforNewResponse(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return dateStr;
		}
		try {
			LocalDateTime dateTime;
			try {
				// First try SP format
				dateTime = LocalDateTime.parse(dateStr, SP_FORMATTER);
			} catch (DateTimeParseException e1) {
				try {
					// Then try Synergy format
					dateTime = LocalDateTime.parse(dateStr, SYNERGY_FORMATTER);
				} catch (DateTimeParseException e2) {
					// Finally try standard format
					if (dateStr.matches("\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}")) {
						dateTime = LocalDateTime.parse(dateStr, inputFormatter);
					} else {
						log.error("Failed to parse date string: {}", dateStr, e2);
						throw e2;
					}
				}
			}
			return dateTime.format(outputFormatterfordate);
		} catch (Exception e) {
			log.error("Failed to parse date string: {}", dateStr, e);
			return dateStr;
		}
	}

	public static String formatDateStringforRemark(String inputDate) {
		if (inputDate == null || inputDate.trim().isEmpty()) {
			return null;
		}

		LocalDateTime dateTime = null;
		Exception lastException = null;

		for (DateTimeFormatter formatter : INPUT_FORMATTERS) {
			try {
				dateTime = LocalDateTime.parse(inputDate.trim().toUpperCase(), formatter);
				break;
			} catch (Exception e) {
				lastException = e;
				continue;
			}
		}

		if (dateTime == null) {
			log.error("Failed to parse date: {} with error: {}", inputDate, lastException.getMessage());
			throw new IllegalArgumentException("Unable to parse date: " + inputDate);
		}

		return dateTime.format(outputFormatter);
	}

	public static LocalDate parseDate(String dateStr) {
		try {
			if (dateStr == null || dateStr.trim().isEmpty()) {
				throw new IllegalArgumentException("Date string cannot be null or empty");
			}

			// Handle "Today" case
			if (dateStr.contains("Today")) {
				return LocalDate.now();
			}

			dateStr = dateStr.trim();

			// Handle the case where the date includes time
			if (dateStr.contains(",")) {
				// Split by comma and take the first part (date part)
				dateStr = dateStr.split(",")[0].trim();
			}

			try {
				// Try "dd MMMM yyyy" format
				return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
			} catch (Exception e1) {
				try {
					// Try "dd MMMM" format and add current year
					LocalDate date = LocalDate.parse(dateStr + " " + LocalDate.now().getYear(),
							DateTimeFormatter.ofPattern("dd MMMM yyyy"));
					return date;
				} catch (Exception e2) {
					try {
						// Try "dd MMM ''yy" format
						return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMM ''yy"));
					} catch (Exception e3) {
						// Try "dd/MM/yyyy" format as last resort
						return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					}
				}
			}
		} catch (Exception e) {
			log.error("Failed to parse date: {} with error: {}", dateStr, e.getMessage());
			throw new IllegalArgumentException("Unable to parse date: " + dateStr, e);
		}
	}

	public static String formatEtaTime(String etaTime) {
		if (etaTime == null || etaTime.trim().isEmpty()) {
			return null;
		}

		try {
			LocalDateTime dateTime = LocalDateTime.parse(etaTime.trim(),
					DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
			return dateTime.format(etaOutputFormatters);
		} catch (Exception e) {
			log.error("Failed to parse ETA time: {} with error: {}", etaTime, e.getMessage());
			return etaTime; // Return original value if parsing fails
		}
	}

//----------working --------

	public static String prefixDate(String date) {
		DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		try {
			LocalDate dt = LocalDate.parse(date, displayFormatter);
			if (dt.equals(LocalDate.now())) {
				return "Today | " + formatGroupDate(dt);
			} else if (dt.equals(LocalDate.now().minusDays(1))) {
				return "Yesterday | " + formatGroupDate(dt);
			} else {
				return formatGroupDate(dt);
			}
		} catch (Exception e) {
			log.error("Error while parsing date: ", e);
			return date;
		}
	}

	public static String formatGroupDate(LocalDate date) {
		return date.format(dateGroupFormatter);
	}
	
	
	
	public static LocalDate getLocalDate(String date) {
//		DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		if (date.indexOf("|") > 0) {
			date = date.substring(date.indexOf("|") + 1).trim();
		}
		return LocalDate.parse(date, dateGroupFormatter);
	}

	public static String formatDateStringfor(String dateStr) {
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

	public static String formatDateStringforNewResponsefor(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return dateStr;
		}
		try {
			LocalDateTime dateTime;
			try {
				// First try SP format
				dateTime = LocalDateTime.parse(dateStr, SP_FORMATTER);
			} catch (DateTimeParseException e1) {
				try {
					// Then try Synergy format
					dateTime = LocalDateTime.parse(dateStr, SYNERGY_FORMATTER);
				} catch (DateTimeParseException e2) {
					// Finally try standard format
					if (dateStr.matches("\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}")) {
						dateTime = LocalDateTime.parse(dateStr, inputFormatter);
					} else {
						log.error("Failed to parse date string: {}", dateStr, e2);
						throw e2;
					}
				}
			}
			return dateTime.format(outputFormatterfordate);
		} catch (Exception e) {
			log.error("Failed to parse date string: {}", dateStr, e);
			return dateStr;
		}
	}

	public static String formatEtaTimeforcm(String etaTimeStr) {
		if (etaTimeStr == null || etaTimeStr.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDateTime dateTime;
			try {
				// First try standard format (dd/MM/yyyy HH:mm)
				dateTime = LocalDateTime.parse(etaTimeStr, inputFormatter);
			} catch (DateTimeParseException e1) {
				try {
					// Then try SP format
					dateTime = LocalDateTime.parse(etaTimeStr, SP_FORMATTER);
				} catch (DateTimeParseException e2) {
					try {
						// Finally try Synergy format
						dateTime = LocalDateTime.parse(etaTimeStr, SYNERGY_FORMATTER);
					} catch (DateTimeParseException e3) {
						log.error("Failed to parse eta time: {}", etaTimeStr, e3);
						return etaTimeStr;
					}
				}
			}
			return dateTime.format(etaOutputFormatter);
		} catch (Exception e) {
			log.error("Failed to parse eta time: {}", etaTimeStr, e);
			return etaTimeStr;
		}
	}

	public static String formatPreviousDayTimestamp() {
		try {
			LocalDateTime previousDay = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(0)
					.withSecond(59).withNano(0); // This line removes the nanoseconds
			String formattedDate = previousDay.format(outputFormatter);
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
	
	public static String convertDateFormat(String inputDate) {
	    try {
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a", Locale.ENGLISH);

	        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
	        return dateTime.format(outputFormatter).toLowerCase(); // to match "am"/"pm" style
	    } catch (DateTimeParseException e) {
	        throw new IllegalArgumentException("Invalid date format: " + inputDate, e);
	    }
	}
	
	
	@Loggable
	public static String getDownTimeInHrs(String createDate) {
		log.info("getDownTimeInHrs()|createdDate:{}", createDate);
		DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
		LocalDateTime parsedCreatedDate = LocalDateTime.parse(createDate, formatterInput);
		Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());

		return String.format("%d Hrs", duration.toHours());
	}
	
	@Loggable
	public static String getDownTimeInHrsHims(String createDate) {
	    log.info("getDownTimeInHrs() | createdDate: {}", createDate);

	    DateTimeFormatter formatterInput = new DateTimeFormatterBuilder()
	        .appendPattern("yyyy-MM-dd HH:mm:ss")
	        .optionalStart()
	        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
	        .optionalEnd()
	        .toFormatter(Locale.US);

	    LocalDateTime parsedCreatedDate = LocalDateTime.parse(createDate, formatterInput);
	    Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());

	    return String.format("%d hrs", duration.toHours());
	}

	
	public static String formatCustomDate(String inputDateStr) {
	    if (inputDateStr == null || inputDateStr.trim().isEmpty()) {
	        return "";
	    }
	    try {
	        // Define input format matching: yyyy-MM-dd HH:mm:ss.S
	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	        // Define desired output format: 24 July 2025, 01:46 PM
	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.ENGLISH);

	        Date date = inputFormat.parse(inputDateStr);
	        return outputFormat.format(date);
	    } catch (Exception e) {
	        System.err.println("Failed to format date: " + inputDateStr);
	        return "";
	    }
	}
	
	public static String formatToDateTimeShort(String inputDateStr) {
	    if (inputDateStr == null || inputDateStr.trim().isEmpty()) {
	        return "";
	    }
	    try {
	        // Input format: yyyy-MM-dd HH:mm:ss.S
	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	        // Desired output: 24/07/2025 13:45
	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	        Date parsedDate = inputFormat.parse(inputDateStr);
	        return outputFormat.format(parsedDate);
	    } catch (Exception e) {
	        System.err.println("Date format failed for input: " + inputDateStr);
	        return "";
	    }
	}

}