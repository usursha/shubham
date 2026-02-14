package com.hpy.ops360.ticketing.cm.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.config.DisableSslClass;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.AtmDetailsWithSourceDto;
import com.hpy.ops360.ticketing.dto.TicketCategoryForAllTickets;
import com.hpy.ops360.ticketing.dto.TicketData;
import com.hpy.ops360.ticketing.dto.TicketDateGroup;
import com.hpy.ops360.ticketing.dto.TicketManagementResponse;
import com.hpy.ops360.ticketing.dto.TicketTypeDataForAllTickets;
import com.hpy.ops360.ticketing.entity.UserAtmDetails;
import com.hpy.ops360.ticketing.service.TaskService;
import com.hpy.ops360.ticketing.service.TaskService2;
import com.hpy.ops360.ticketing.service.TaskService3;
import com.hpy.ops360.ticketing.service.UserAtmDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AllTicketByCEService {

	@Autowired
	private UserAtmDetailsService userAtmDetailsService;

//	@Autowired
//	private TaskService2 test;
	
	
	@Autowired
	private TaskService3 test;
	
	

	@Value("${ops360.ticketing.max-atm-list-count}")
	private int maxAtmListCount;

	@Value("${ops360.ticketing.atm-batch-size}")
	private int atmBatchSize;

	/**
	 * Main method to get ticket details based on CE Id and process them
	 * accordingly.
	 */
	public TicketManagementResponse getTicketDetailsByCEAndStatus_newResponse(String ceId) {
		log.info("Fetching user ATM details for CE ID: {}", ceId);
		DisableSslClass.disableSSLVerification();
		List<UserAtmDetails> atmWithSourceList = userAtmDetailsService.getUserAtmDetails(ceId);
		List<AtmDetailsWithSourceDto> atmIdDtoList = new ArrayList<>();
		
		//atmIdDtoList= atmWithSourceList.stream().map(atm -> new AtmDetailsWithSourceDto(atm.getAtm_code(),atm.getSource())).toList();

		List<AtmDetailsWithSourceDto> atmIds = atmWithSourceList.stream()
				.map(atmDetails -> new AtmDetailsWithSourceDto(atmDetails.getAtm_code(),atmDetails.getSource())).collect(Collectors.toList());

//		if (atmIdDtoList.isEmpty()) {
//			log.warn("No ATM details found for CE ID: {}", ceId);
//		}

		return processTickets_newResponse(atmIds,ceId);
	}

	/**
	 * Process the list of ATM IDs and retrieve and organize tickets accordingly.
	 */
	private TicketManagementResponse processTickets_newResponse(List<AtmDetailsWithSourceDto> atmIds,String ceId) {
		log.info("Processing tickets for {} ATM(s)", atmIds.size());

		if (atmIds.isEmpty()) {
			log.info("No ATM IDs found to process tickets.");
			return new TicketManagementResponse();
		}

		
		
		
		List<AtmShortDetailsDto> allTickets = test.getTicketList(atmIds, maxAtmListCount,ceId);
		//List<AtmShortDetailsDto> allTickets = test.getTicketListHims(atmIds, maxAtmListCount,ceId);
		
		log.info("Retrieved {} tickets for processing.", allTickets.size());

		return buildResponse(allTickets);
	}

	/**
	 * Builds the response by categorizing tickets into various groups such as open,
	 * timeout, and updated tickets.
	 */
	private TicketManagementResponse buildResponse(List<AtmShortDetailsDto> allTickets) {
		log.info("Building response with categorized tickets.");

		TicketManagementResponse response = new TicketManagementResponse();
		List<TicketCategoryForAllTickets> categories = new ArrayList<>();

		// Updated the color logic for tickets where travel and update status intersect

		// All Tickets Category
		categories.add(createTicketCategory("All Tickets", allTickets));

		// Open Tickets Category
		List<AtmShortDetailsDto> openTickets = allTickets.stream()
				.filter(t -> t.getIsUpdated() == 0 && t.getIsTimedOut() == 0).collect(Collectors.toList());
		categories.add(createTicketCategory("Open Tickets", openTickets));

		// Timeout Tickets Category
		List<AtmShortDetailsDto> timeoutTickets = allTickets.stream().filter(t -> t.getIsTimedOut() == 1)
				.collect(Collectors.toList());
		categories.add(createTicketCategory("Timeout Tickets", timeoutTickets));

		// Updated Tickets Category
		List<AtmShortDetailsDto> updatedTickets = allTickets.stream()
				.filter(t -> t.getIsUpdated() == 1 || t.getIsTravelling() == 1).collect(Collectors.toList());
		categories.add(createTicketCategory("Updated Tickets", updatedTickets));

		response.setData(categories);
		return response;
	}

	/**
	 * Creates a category for tickets based on the category name and list of
	 * tickets.
	 */
	private TicketCategoryForAllTickets createTicketCategory(String name, List<AtmShortDetailsDto> tickets) {
		log.debug("Creating ticket category: {}", name);

		TicketCategoryForAllTickets category = new TicketCategoryForAllTickets();
		category.setName(name);
		category.setCount(tickets.size());

		List<TicketTypeDataForAllTickets> typeDataList = Arrays.asList(createTicketType("Total Tickets", tickets),
				createTicketType("Cash", filterByEventGroup(tickets, "Cash")),
				createTicketType("Communication", filterByEventGroup(tickets, "Communication")),
				createTicketType("Hardware Fault", filterByEventGroup(tickets, "Hardware Fault")),
				createTicketType("No Transactions", filterByEventGroup(tickets, "No Transactions")),
				createTicketType("Supervisory", filterByEventGroup(tickets, "Supervisory")),
				createTicketType("Others", filterByEventGroup(tickets, "Others")));

		category.setData(typeDataList);
		return category;
	}

	/**
	 * Creates ticket type data for each event group in the tickets.
	 */
	private TicketTypeDataForAllTickets createTicketType(String name, List<AtmShortDetailsDto> tickets) {
		log.debug("Creating ticket type: {}", name);

		TicketTypeDataForAllTickets typeData = new TicketTypeDataForAllTickets();
		typeData.setName(name);
		typeData.setCount(tickets.size());

		List<AtmShortDetailsDto> flaggedTickets = tickets.stream().filter(t -> t.getFlagStatus() == 1)
				.filter(t -> !t.getSubcall().contains("Data;ATM Transacting")).collect(Collectors.toList());
		typeData.setFlagedTickets(groupTicketsByDate(flaggedTickets));

		List<AtmShortDetailsDto> otherTickets = tickets.stream()
				.filter(t -> t.getFlagStatus() == 0 || t.getSubcall().contains("Data;ATM Transacting"))
				.collect(Collectors.toList());
		typeData.setOtherTickets(groupTicketsByDate(otherTickets));

		return typeData;
	}

	/**
	 * Groups tickets by their creation date.
	 */
	private List<TicketDateGroup> groupTicketsByDate(List<AtmShortDetailsDto> tickets) {
		log.debug("Grouping tickets by date");

		if (tickets.isEmpty()) {
			log.info("No tickets to group by date.");
			return Collections.emptyList();
		}

		Map<String, List<AtmShortDetailsDto>> ticketsByDate = tickets.stream()
				.collect(Collectors.groupingBy(ticket -> formatDateGroup(ticket.getCreatedDate())));

		return ticketsByDate.entrySet().stream()
				.map(entry -> new TicketDateGroup(entry.getKey(), mapToTicketData(entry.getValue())))
				.sorted(compareDateGroups()).collect(Collectors.toList());
	}

	/**
	 * Maps the grouped tickets to TicketData objects.
	 */
	private List<TicketData> mapToTicketData(List<AtmShortDetailsDto> tickets) {
		log.debug("Mapping tickets to TicketData");

		return tickets.stream().map(this::convertToTicketData).collect(Collectors.toList());
	}

	/**
	 * Converts a single AtmShortDetailsDto to TicketData.
	 */
	private TicketData convertToTicketData(AtmShortDetailsDto dto) {
		log.debug("Converting ticket: {} to TicketData", dto.getRequestid());

		return new TicketData(dto.getRequestid(), dto.getAtmId(), dto.getTicketNumber(), dto.getBank(),
				dto.getSiteName(), dto.getOwner(), dto.getSubcall(), dto.getVendor(), dto.getError(), dto.getDownTime(),
				dto.getPriorityScore(), dto.getEventGroup(), dto.getIsBreakdown(), dto.getIsUpdated(),
				dto.getIsTimedOut(), dto.getIsTravelling(), dto.getTravelTime(), dto.getTravelEta(), dto.getDownCall(),
				dto.getEtaDateTime(), dto.getEtaTimeout(), dto.getCreatedDate(), dto.getCloseDate(),
				dto.getFlagStatus(), dto.getFlagStatusInsertTime(), dto.getColor(), dto.getCeName());
	}

	/**
	 * Filters tickets by the event group type.
	 */
	private List<AtmShortDetailsDto> filterByEventGroup(List<AtmShortDetailsDto> tickets, String eventGroup) {
		log.debug("Filtering tickets by event group: {}", eventGroup);

		return tickets.stream().filter(t -> eventGroup.equals(t.getEventGroup())).collect(Collectors.toList());
	}

	/**
	 * Formats the date into "Today | dd MMMM yyyy", "Yesterday | dd MMMM yyyy", or
	 * just "dd MMMM yyyy".
	 */
	private String formatDateGroup(String dateTime) {

		LocalDate ticketDate = parseCreatedDate(dateTime);
		LocalDate today = LocalDate.now();

		if (ticketDate.equals(today)) {
			return "Today | " + formatDate(today);
		}
		LocalDate yesterday = LocalDate.now().minusDays(1);

		if (ticketDate.equals(yesterday)) {
			return "Yesterday | " + formatDate(yesterday);
		}

		return formatDate(ticketDate);
	}

	/**
	 * Formats the date as "dd MMMM yyyy".
	 */
	private String formatDate(LocalDate date) {
		log.debug("Formatting date: {}", date);

		return date.format(DateTimeFormatter.ofPattern("dd MMM ''yy"));
	}

	/**
	 * Parses the created date in the format "dd MMM ''yy, hh:mm a".
	 */

//	public static LocalDate parseCreatedDate(String dateTimeString) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM ''yy");
//		try {
//			return LocalDate.parse(dateTimeString, formatter);
//		} catch (DateTimeParseException e) {
//			System.err.println("Error parsing date-time string: " + e.getMessage());
//			return LocalDate.now(); // or handle the error as needed
//		}
//	}
	
	public static LocalDate parseCreatedDate(String dateTimeString) {
		if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
			log.warn("Empty or null date string, returning current date");
			return LocalDate.now();
		}

		String trimmedDate = dateTimeString.trim();
		log.debug("Parsing date string: {}", trimmedDate);

		try {
			// Try different date formats in order of likelihood
			
			// Format 1: Database datetime format "2024-11-20 10:36:15" or "2024-11-20 10:36:15.123"
			if (trimmedDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d+)?")) {
				if (trimmedDate.contains(".")) {
					// Handle with milliseconds
					LocalDateTime dateTime = LocalDateTime.parse(trimmedDate.substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					return dateTime.toLocalDate();
				} else {
					// Handle without milliseconds
					LocalDateTime dateTime = LocalDateTime.parse(trimmedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					return dateTime.toLocalDate();
				}
			}
			
			// Format 2: ISO date format "2024-11-20"
			if (trimmedDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
			
			// Format 3: Already formatted date "dd MMM ''yy" (e.g., "20 Nov '24")
			if (trimmedDate.matches("\\d{1,2} \\w{3} '\\d{2}")) {
				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("dd MMM ''yy"));
			}
			
			// Format 4: Alternative format "dd/MM/yyyy HH:mm"
			if (trimmedDate.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}")) {
				LocalDateTime dateTime = LocalDateTime.parse(trimmedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
				return dateTime.toLocalDate();
			}
			
			// Format 5: Simple date "dd/MM/yyyy"
			if (trimmedDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			}
			
			log.warn("Unrecognized date format: {}, returning current date", trimmedDate);
			return LocalDate.now();
			
		} catch (DateTimeParseException e) {
			log.error("Error parsing date-time string: {} - {}", dateTimeString, e.getMessage());
			// Return current date as fallback instead of failing
			return LocalDate.now();
		}
	}

	/**
	 * Compares date groups to sort them in chronological order (Today first, then
	 * Yesterday, then others).
	 */
	private Comparator<TicketDateGroup> compareDateGroups() {
		log.debug("Comparing date groups");

		return (date1, date2) -> {
			String d1 = date1.getDate(), d2 = date2.getDate();
			if (date1.getDate().startsWith("Today | ")) {
				d1 = date1.getDate().replace("Today | ", "");

			}

			if (date1.getDate().startsWith("Yesterday | ")) {
				d1 = date1.getDate().replace("Yesterday | ", "");

			}

			if (date2.getDate().startsWith("Today | ")) {
				d2 = date2.getDate().replace("Today | ", "");

			}
			if (date2.getDate().startsWith("Yesterday | ")) {
				d2 = date2.getDate().replace("Yesterday | ", "");

			}

			System.out.println("---format---" + d1 + "----format" + d2);
			DateTimeFormatter dateGroupFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy");

			LocalDate dt1 = LocalDate.parse(d1, dateGroupFormatter);
			LocalDate dt2 = LocalDate.parse(d2, dateGroupFormatter);
			return dt2.compareTo(dt1);
		};
	}
}
