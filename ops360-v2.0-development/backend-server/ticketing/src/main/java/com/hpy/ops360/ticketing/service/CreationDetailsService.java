package com.hpy.ops360.ticketing.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.CreationDetailsDTO;
import com.hpy.ops360.ticketing.cm.entity.CreationdetailsEntity;
import com.hpy.ops360.ticketing.cm.repo.CreationDetailsRepo;
import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.entity.CheckAtmSource;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.CreationDetailsRepositoryHims;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreationDetailsService {

	@Autowired
	private CreationDetailsRepo creationDetailsRepo;

	@Autowired
	private SynergyService synergyService;
	
	@Autowired
	private CheckAtmSourceRepository checkAtmSourceRepository;
	
	@Autowired
	private CreationDetailsRepositoryHims creationDetailsRepositoryHims;
	
	public CreationDetailsDTO getCreationdetails(TicketDetailsReqWithoutReqId ticketDetailsRequest) {
        log.info("******* Inside getCreationdetails Service *********");
        log.debug("Request Received: ATM ID = {}, Ticket Number = {}", ticketDetailsRequest.getAtmid(), ticketDetailsRequest.getTicketno());

        if (ticketDetailsRequest.getTicketno() != null && ticketDetailsRequest.getTicketno().startsWith("OPS")) {
            log.info("Handling manual ticket details for Ticket Number: {}", ticketDetailsRequest.getTicketno());

            try {
                CreationdetailsEntity rawResults = creationDetailsRepo.getcreationdetails(ticketDetailsRequest.getAtmid(), ticketDetailsRequest.getTicketno());
                if (rawResults == null) {
                    log.warn("No data found in repository for ATM ID: {} and Ticket Number: {}", ticketDetailsRequest.getAtmid(), ticketDetailsRequest.getTicketno());
                    throw new NoSuchElementException("No details found for the provided ticket number.");
                }

                log.info("Data retrieved successfully for Ticket Number: {}", ticketDetailsRequest.getTicketno());
                CreationDetailsDTO response = new CreationDetailsDTO();
                response.setSrno(rawResults.getSrno());
                response.setTicketNumber(rawResults.getTicketNumber());
                response.setCreatedBy(rawResults.getCreatedBy());
                response.setCreatedTime(convertToCustomFormat(rawResults.getCreatedTime()));

                log.debug("Response generated: {}", response);
                return response;
            } catch (Exception e) {
                log.error("Error while fetching manual ticket details for Ticket Number: {}", ticketDetailsRequest.getTicketno(), e);
                throw e;
            }

        } else {
            log.info("Handling non-manual ticket details for Ticket Number: {}", ticketDetailsRequest.getTicketno());

            try {
            	List<CheckAtmSource> atm = checkAtmSourceRepository.checkAtmSource(ticketDetailsRequest.getAtmid());
            	if (atm.isEmpty()) {
    				log.error("Atm list with source is empty insert valid atm id");
    				throw new IllegalArgumentException("Atm list with source is empty");
    			}
    			log.info("Atm list with source response: {}", atm);
    			int source = atm.get(0).getSourceCode();
    			if (source == 0) // synergy
    			{
            	
                TicketDetailsDto ticket = synergyService.getTicketDetails(ticketDetailsRequest);
                if (ticket == null) {
                    log.warn("No ticket details found from Synergy Service for Ticket Number: {}", ticketDetailsRequest.getTicketno());
                    throw new NoSuchElementException("No ticket details found.");
                }

                log.info("Data retrieved successfully from Synergy Service for Ticket Number: {}", ticketDetailsRequest.getTicketno());
                CreationDetailsDTO response = new CreationDetailsDTO();
                response.setSrno("1");
                response.setTicketNumber(ticket.getSrno());
                response.setCreatedBy("API");
                response.setCreatedTime(convertToCustomFormat(ticket.getCreateddate()));

                log.debug("Response generated: {}", response);
                return response;
                
    			}else {

    				CreationdetailsEntity getcreationdetailsHims = creationDetailsRepositoryHims.getcreationdetailsHims(ticketDetailsRequest.getAtmid(),ticketDetailsRequest.getTicketno());
    				
                    if (getcreationdetailsHims == null) {
                        log.warn("No ticket details found from Hims for Ticket Number: {}", ticketDetailsRequest.getTicketno());
                        throw new NoSuchElementException("No ticket details found.");
                    }

                    log.info("Data retrieved successfully from Hims for Ticket Number: {}", ticketDetailsRequest.getTicketno());
                    CreationDetailsDTO response = new CreationDetailsDTO();
                    response.setSrno("1");
                    response.setTicketNumber(getcreationdetailsHims.getTicketNumber());
                    response.setCreatedBy("HIMS");
                    response.setCreatedTime(convertToCustomFormat(getcreationdetailsHims.getCreatedTime()));

                    log.debug("Response generated: {}", response);
                    return response;
    			}
    				
            } catch (Exception e) {
                log.error("Error while fetching ticket details from Hims for Ticket Number: {}", ticketDetailsRequest.getTicketno(), e);
                throw e;
            }
        }
    }

	public String convertToCustomFormat(String inputDateTime) {
	    log.debug("Converting input date-time: {}", inputDateTime);

	    try {
	        // Define potential input formats
	        DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	        DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
	        DateTimeFormatter inputFormatter3 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	        // Attempt parsing with the provided formats
	        LocalDateTime dateTime;
	        try {
	            dateTime = LocalDateTime.parse(inputDateTime, inputFormatter1);
	            log.debug("Parsed input date-time successfully using format: yyyy-MM-dd HH:mm:ss.SSS");
	        } catch (Exception e1) {
	            log.debug("Failed to parse using format yyyy-MM-dd HH:mm:ss.SSS, trying format yyyy-MM-dd HH:mm:ss.SS");
	            try {
	                dateTime = LocalDateTime.parse(inputDateTime, inputFormatter2);
	                log.debug("Parsed input date-time successfully using format: yyyy-MM-dd HH:mm:ss.SS");
	            } catch (Exception e2) {
	                log.debug("Failed to parse using format yyyy-MM-dd HH:mm:ss.SS, trying format dd/MM/yyyy HH:mm");
	                dateTime = LocalDateTime.parse(inputDateTime, inputFormatter3);
	                log.debug("Parsed input date-time successfully using format: dd/MM/yyyy HH:mm");
	            }
	        }

	        // Define the output format
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a");

	        // Format the date-time and return
	        String formattedDate = dateTime.format(outputFormatter);
	        log.debug("Formatted date-time: {}", formattedDate);
	        return formattedDate;
	    } catch (Exception e) {
	        log.error("Invalid date-time format encountered: {}", inputDateTime, e);
	        throw new IllegalArgumentException("Invalid date-time format: " + inputDateTime, e);
	    }
	}


}
