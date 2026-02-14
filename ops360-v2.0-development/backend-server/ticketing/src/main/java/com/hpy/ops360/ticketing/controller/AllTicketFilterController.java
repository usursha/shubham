package com.hpy.ops360.ticketing.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
import com.hpy.ops360.ticketing.cm.dto.TicketsWithCategoryDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.CmTaskService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/task")
@CrossOrigin("${app.cross-origin.allow}")
public class AllTicketFilterController {

	
	@Autowired
	private CmTaskService taskService;
	
	
	@GetMapping("/getCeAllOpenTicketDetails/{ceId}/{status}")
	@Loggable
	public TicketsWithCategoryDto getAllTicketDetailsByCEAndStatus(String ceId, String status) {
		    // Fetch all tickets and category count from the service
		    OpenTicketsWithCategoryDto allData = taskService.getTicketDetailsByCEAndStatus(ceId, status);

		    // Group tickets by event group and date
		    List<AtmShortDetailsDto> allTickets = allData.getTicketShortDetails();

		    Map<String, Map<String, List<AtmShortDetailsDto>>> groupedTickets = new HashMap<>();

		    for (AtmShortDetailsDto ticket : allTickets) {
		        String eventGroup = ticket.getEventGroup();
		        String date;
		        if (ticket.getFlagStatus() == 1) {
		        date = formatFlagStatusDate(ticket.getFlagStatusInsertTime());
		        }else {
		        	date=formatCreatedDate(ticket.getCreatedDate());
		        }

		        // Group tickets by event group and date
		        groupedTickets
		            .computeIfAbsent(eventGroup, k -> new HashMap<>()) // Create event group if not exists
		            .computeIfAbsent(date, k -> new ArrayList<>())  // Create date group if not exists
		            .add(ticket);  // Add ticket to the respective date group
		    }

		    // Map grouped tickets into response format
		    List<TicketsWithCategoryDto.TicketGroupedByDateDto> ticketShortDetails = groupedTickets.entrySet().stream()
		        .map(entry -> {
		            String eventGroup = entry.getKey();
		            Map<String, List<AtmShortDetailsDto>> dateGroups = entry.getValue();

		            List<TicketsWithCategoryDto.TicketDataWithDateDto> flaggedTickets = new ArrayList<>();
		            List<TicketsWithCategoryDto.TicketDataWithDateDto> unflaggedTickets = new ArrayList<>();

		            for (Map.Entry<String, List<AtmShortDetailsDto>> dateEntry : dateGroups.entrySet()) {
		                String date = dateEntry.getKey();
		                List<AtmShortDetailsDto> tickets = dateEntry.getValue();

		                List<AtmShortDetailsDto> flagged = tickets.stream()
		                        .filter(ticket -> ticket.getFlagStatus() == 1)
		                        .collect(Collectors.toList());
		                List<AtmShortDetailsDto> unflagged = tickets.stream()
		                        .filter(ticket -> ticket.getFlagStatus() != 1)
		                        .collect(Collectors.toList());

		                if (!flagged.isEmpty()) {
		                    flaggedTickets.add(new TicketsWithCategoryDto.TicketDataWithDateDto(date, flagged));
		                }
		                if (!unflagged.isEmpty()) {
		                    unflaggedTickets.add(new TicketsWithCategoryDto.TicketDataWithDateDto(date, unflagged));
		                }
		            }

		            return new TicketsWithCategoryDto.TicketGroupedByDateDto(eventGroup, flaggedTickets, unflaggedTickets);
		        })
		        .collect(Collectors.toList());

		    return TicketsWithCategoryDto.builder()
		            .ticketShortDetails(ticketShortDetails)
		            .ticketCategoryCount(allData.getTicketCategoryCount())  // Directly using the count from the service
		            .build();
		}

			private String formatCreatedDate(String createdDateStr) {
			    if (createdDateStr == null || createdDateStr.trim().isEmpty()) {
			        return "Invalid Date";
			    }

			    List<String> dateFormats = Arrays.asList(
			        "dd/MM/yyyy HH:mm",  // e.g., "05/11/2024 16:21"
			        "dd/MM/yyyy",        // e.g., "05/11/2024"
			        "yyyy-MM-dd",        // e.g., "2024-11-05"
			        "MM/dd/yyyy",        // e.g., "11/05/2024"
			        "yyyy/MM/dd HH:mm",  // e.g., "2024/11/05 16:21"
			        "yyyy-MM-dd HH:mm"   // e.g., "2024-11-05 16:21"
			    );

			    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

			    for (String format : dateFormats) {
			        try {
			            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(format);
			            if (format.contains("HH:mm")) {
			                LocalDateTime dateTime = LocalDateTime.parse(createdDateStr.trim(), inputFormatter);
			                LocalDate date = dateTime.toLocalDate();
			                return formatLocalDate(date);
//			                return dateTime.format(outputFormatter);
			            } else {
			                // Parse as LocalDate if no time is specified
			                LocalDate date = LocalDate.parse(createdDateStr.trim(), inputFormatter);
			                return formatLocalDate(date);
//			                return date.format(outputFormatter);
			            }
			        } catch (DateTimeParseException ignored) {
			        }
			    }

			    return "Invalid Date";
			}

			private String formatFlagStatusDate(LocalDateTime dateTime) {
			    if (dateTime == null) {
			        return "Unknown Date";
			    }
			    LocalDate date = dateTime.toLocalDate();
			    return formatLocalDate(date);
			}
			private String formatLocalDate(LocalDate date) {
			    LocalDate today = LocalDate.now();
			    LocalDate yesterday = today.minusDays(1);

			    if (date.equals(today)) {
			        return "Today | " + date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
			    } else if (date.equals(yesterday)) {
			        return "Yesterday | " + date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
			    } else {
			        return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
			    }
			}

	
	
}
