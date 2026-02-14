package com.hpy.ops360.ticketing.cm.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.FlagStatusGroup;
import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketResponse;
import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketsRequest;
import com.hpy.ops360.ticketing.cm.dto.TicketDetail;
import com.hpy.ops360.ticketing.cm.entity.TicketdetailsEntity;
import com.hpy.ops360.ticketing.cm.repo.FlaggedTicketRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FlagTicketHistoryService {
    
	@Autowired
	private FlaggedTicketRepo repository;

    public List<FlaggedTicketResponse> getFlaggedTickets(FlaggedTicketsRequest request) {
    	
    	log.info("*** Inside getFlaggedTickets Service ***");
        log.info("Request received: userId={}, userType={}", request.getUserId(), request.getUserType());
        
        log.info("Fetching flagged ticket details from the repository");
    	List<TicketdetailsEntity> rawResults = repository.getflaggedticketdetails(request.getUserId(), request.getUserType());
        // Group and format response
    	int totalcounts=rawResults.size();
    	log.info("Fetched {} ticket details from the database", totalcounts);

        // Mapping raw results to TicketDetail DTO
        log.info("Mapping fetched data to TicketDetail DTO");
    	List<TicketDetail> data=rawResults.stream().map(result -> new TicketDetail(result.getSrNo(),result.getTicketNumber(),result.getCeUserName(),result.getAtmId(),result.getOwner(),result.getSubcallType(),formatLocalDateTime(result.getEtaDateTime()),result.getVendor(),result.getEventcode(),result.getTravelEtaDatetime(),result.getDownTime(),result.getTravelHours(),result.getFlagStatus(),result.getFlag_status_inserttime(),result.getCreatedTime(),result.getBorder(),result.getFill())).toList();
    	log.info("Grouping and formatting ticket data");
    	List<FlaggedTicketResponse> response=groupAndFormatTickets(data,totalcounts);
    	log.info("Exiting getFlaggedTickets service method");
    	return response;
    }
    
    public String extractTimeInAmPm(String dateTime) {
        // Define possible input formats
        DateTimeFormatter formatterWithOneFraction = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter formatterWithThreeFractions = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // Define the output format
        DateTimeFormatter amPmFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        LocalDateTime localDateTime;

        if (dateTime == null || dateTime.trim().isEmpty()) {
            return "";
        }
        
        try {
            // Attempt parsing with the single-digit fractional seconds format
            localDateTime = LocalDateTime.parse(dateTime, formatterWithOneFraction);
        } catch (DateTimeParseException e) {
            try {
                // Attempt parsing with the three-digit fractional seconds format
                localDateTime = LocalDateTime.parse(dateTime, formatterWithThreeFractions);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date-time format. Expected formats are yyyy-MM-dd HH:mm:ss.S or yyyy-MM-dd HH:mm:ss.SSS.");
            }
        }

        // Format the time part in AM/PM format and return it
        return localDateTime.format(amPmFormatter);
    }

    public String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a");
        String formattedDateTime = dateTime.format(formatter);
        // Convert AM/PM to uppercase
        formattedDateTime = formattedDateTime.replace("am", "AM").replace("pm", "PM");
        return formattedDateTime;
    }
    
    private List<FlaggedTicketResponse> groupAndFormatTickets(List<TicketDetail> tickets,int totalcounts) {

    	log.info("Entering groupAndFormatTickets method");

        log.debug("Grouping tickets by CE username and flag status insert time");
        // Group tickets by ceUserName and flagStatusInsertTime
        Map<String, Map<String, List<TicketDetail>>> groupedData = tickets.stream()
                .collect(Collectors.groupingBy(
                        TicketDetail::getCeUserName,
                        Collectors.groupingBy(
                                ticket -> formatFlagStatusDate(ticket.getFlag_status_inserttime()), // Format here
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));

        List<FlaggedTicketResponse> response = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<TicketDetail>>> ceEntry : groupedData.entrySet()) {
        	log.debug("Processing CE username: {}", ceEntry.getKey());
            FlaggedTicketResponse flaggedTicketResponse = new FlaggedTicketResponse();
            flaggedTicketResponse.setCeUserName(ceEntry.getKey());

            List<FlagStatusGroup> flagStatusGroups = new ArrayList<>();
            for (Map.Entry<String, List<TicketDetail>> flagEntry : ceEntry.getValue().entrySet()) {
            	log.debug("Processing flag status insert time: {}", flagEntry.getKey());
                FlagStatusGroup flagStatusGroup = new FlagStatusGroup();
                flagStatusGroup.setFlagStatusInsertTime(flagEntry.getKey()); // Already formatted
                flagStatusGroup.setTicketDetails(flagEntry.getValue());
                flagStatusGroups.add(flagStatusGroup);
            }

            flaggedTicketResponse.setTotalCounts(totalcounts);
            flaggedTicketResponse.setFlagStatusGroups(flagStatusGroups);
            response.add(flaggedTicketResponse);
        }

        log.info("Exiting groupAndFormatTickets method");
        return response;
    }

    
    
    private String formatFlagStatusDate(LocalDateTime dateTime) {
    	log.debug("Formatting flag status date for: {}", dateTime);
	    if (dateTime == null) {
	    	log.warn("Flag status date is null; returning 'Unknown Date'");
	        return "Unknown Date";
	    }
	    LocalDate date = dateTime.toLocalDate();
	    return formatLocalDate(date);
	}
	// Helper method for formatting LocalDate to the desired string format
	private String formatLocalDate(LocalDate date) {
		log.debug("Formatting local date: {}", date);
	    LocalDate today = LocalDate.now();
	    LocalDate yesterday = today.minusDays(1);

	    if (date.equals(today)) {
	    	 log.debug("Date is today; formatting as 'Today'");
	        return "Today | " + date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
	    } else if (date.equals(yesterday)) {
	    	log.debug("Date is yesterday; formatting as 'Yesterday'");
	        return "Yesterday | " + date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
	    } else {
	    	log.debug("Date is neither today nor yesterday; formatting as standard date");
	        return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
	    }
	} 
}