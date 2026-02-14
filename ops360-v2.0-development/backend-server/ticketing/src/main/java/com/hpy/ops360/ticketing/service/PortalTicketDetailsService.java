package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketShortDetailsDto;
import com.hpy.ops360.ticketing.entity.AtmTicketEvent;
import com.hpy.ops360.ticketing.entity.TicketDetailsView;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.AtmTicketEventRepository;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.TicketDetailsViewHimsRepository;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.DateStringCleaner;
import com.hpy.ops360.ticketing.utils.DateUtil;
import com.hpy.ops360.ticketing.utils.SafeValueUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PortalTicketDetailsService {
	
	private SynergyService synergyService;
	
	private AtmTicketEventRepository atmTicketEventRepository;
	
	private LoginService loginService;
	
	private CheckAtmSourceRepository checkAtmSourceRepository;
	
	private TicketDetailsViewHimsRepository ticketDetailsViewHimsRepository;

	public PortalTicketDetailsService(SynergyService synergyService,
			AtmTicketEventRepository atmTicketEventRepository,LoginService loginService,CheckAtmSourceRepository checkAtmSourceRepository,TicketDetailsViewHimsRepository ticketDetailsViewHimsRepository) {
		super();
		this.synergyService = synergyService;
		this.atmTicketEventRepository = atmTicketEventRepository;
		this.loginService=loginService;
		this.checkAtmSourceRepository = checkAtmSourceRepository;
		this.ticketDetailsViewHimsRepository = ticketDetailsViewHimsRepository;
	}



	public TicketShortDetailsDto getTicketTabDetails(TicketDetailsReqWithoutReqId request)
	{
			
		  if (request == null) {
			  log.error("TicketDetailsReqWithoutReqId is null");
	          throw new IllegalArgumentException("TicketDetailsReqWithoutReqId cannot be null");
		  }
		  
		  if (request.getAtmid().isEmpty() && request.getTicketno().isEmpty() || request.getAtmid().isEmpty() || request.getTicketno().isEmpty()) {
				return TicketShortDetailsDto.builder()
						.ceName("")
						.ticketNumber("")
						.atmId("")
						.createdOn("")
						.status("")
						.ticketType("")
						.downHrs("")
						.owner("")
						.subcalltype("")
						.internalRemarks("")
						.latestEtaDateTime("")
						.vendor("")
						.build();
						
		  }
		  log.info("Fetching ticket details for request from hims: {}", request);
	      TicketDetailsView ticket = ticketDetailsViewHimsRepository.getTicketDetailsFromHims(request.getTicketno(), request.getAtmid());
	      if (ticket == null) {
	    	  return TicketShortDetailsDto.builder()
						.ceName("")
						.ticketNumber("")
						.atmId("")
						.createdOn("")
						.status("")
						.ticketType("")
						.downHrs("")
						.owner("")
						.subcalltype("")
						.internalRemarks("")
						.latestEtaDateTime("")
						.vendor("")
						.build();
		}
	      
	      log.info("Fetching ticket details for response: {}", ticket);
	      
	   // Create the unique code for the single ticket
		  String uniqueCode = String.format("%s|%s|%s|%s|%s", 
				  ticket.getEquipmentid(),
				  ticket.getSrno(), 
				  ticket.getEventcode(), 
				  ticket.getNextfollowup(),
				  ticket.getCalldate());
		  // Fetch AtmTicketEvent for the single ticket
		  List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
				  .getAtmTicketEvent(loginService.getLoggedInUser(), uniqueCode);
		  
		  log.info("event atmTicketEventCodeList for single ticket ===================: {}", uniqueCode);
		  if (atmTicketEventList.isEmpty()) {
			  log.error("No AtmTicketEvent found for ticket: {}", ticket.getSrno());
			  throw new IllegalStateException("No AtmTicketEvent found for the given ticket"+ticket);
		  }
		  
		  AtmTicketEvent atmTicketEvent = atmTicketEventList.get(0);
		  
		  TicketShortDetailsDto responseDetails = new TicketShortDetailsDto();
		  responseDetails.setCeName(atmTicketEvent.getCeFullName());
		  responseDetails.setAtmId(request.getAtmid());
		  responseDetails.setTicketNumber(request.getTicketno());
		  responseDetails.setCreatedOn(
			        ticket.getCreateddate() != null
			            ? CustomDateFormattor.convert(ticket.getCreateddate(), CustomDateFormattor.FormatStyle.SIMPLE_ENGLISH)
			            : ""
			    );	      
		  responseDetails.setStatus(SafeValueUtil.nullToEmpty(ticket.getStatus()));
		  responseDetails.setDownHrs(
			        ticket.getCreateddate() != null
			            ? DateUtil.getDownTimeInHrsHims(ticket.getCreateddate())
			            : ""
			    );	      
		  responseDetails.setTicketType(atmTicketEvent.getDownCall()==1?"Down":"");
		  responseDetails.setOwner(SafeValueUtil.nullToEmpty(atmTicketEvent.getOwner()));
		  responseDetails.setSubcalltype(SafeValueUtil.nullToEmpty(ticket.getSubcalltype()));
		  responseDetails.setInternalRemarks(SafeValueUtil.nullToEmpty(atmTicketEvent.getInternalRemark()));
//		  responseDetails.setLatestEtaDateTime(
//			        atmTicketEvent.getEtaDateTime() != null
//			            ? CustomDateFormattor.convert(atmTicketEvent.getEtaDateTime(), CustomDateFormattor.FormatStyle.SIMPLE_ENGLISH)
//			            : ""
//			    );	      
		  
		// UPDATED: Clean the ETA DateTime string
	        String rawEtaDateTime = atmTicketEvent.getEtaDateTime();
	        String cleanedEtaDateTime = DateStringCleaner.cleanDateTimeString(rawEtaDateTime);
	        
	        log.info("Raw ETA DateTime: '{}'", rawEtaDateTime);
	        log.info("Cleaned ETA DateTime: '{}'", cleanedEtaDateTime);
	        
	        // Validate the cleaned date
	        if (!DateStringCleaner.isValidDateTimeFormat(cleanedEtaDateTime)) {
	            log.warn("Cleaned ETA DateTime may not be valid: '{}'", cleanedEtaDateTime);
	            // You can choose to set empty string or keep the cleaned version
	            cleanedEtaDateTime = ""; // or keep cleanedEtaDateTime
	        }
	        
	      responseDetails.setLatestEtaDateTime(cleanedEtaDateTime);
		 // responseDetails.setLatestEtaDateTime(atmTicketEvent.getEtaDateTime());
		  responseDetails.setVendor(SafeValueUtil.nullToEmpty(ticket.getVendor()));
		  log.info("Ticket Details Response: {}",responseDetails);
		  return responseDetails;
	      
	      
		  
      
      
	}


//	private TicketShortDetailsDto buildTicketDetailsResponseBasedOnSource(TicketDetailsReqWithoutReqId request) {
//		log.info("Fetching ticket details for request: {}", request);
//		  TicketDetailsDto ticket = synergyService.getTicketDetails(request);
//		  log.info("Fetching ticket details response: {}", ticket);
//		  
//		  if (ticket == null) {
//		    log.error("Fetched ticket details are null for request: {}", request);
//		    throw new IllegalStateException("Fetched ticket details cannot be null");
//		  }
//		  
//		  // Create the unique code for the single ticket
//		  String uniqueCode = String.format("%s|%s|%s|%s|%s", 
//				  ticket.getEquipmentid(),
//				  ticket.getSrno(), 
//				  ticket.getEventcode(), 
//				  ticket.getNextfollowup(),
//				  ticket.getCalldate());
//		  // Fetch AtmTicketEvent for the single ticket
//		  List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
//				  .getAtmTicketEvent(loginService.getLoggedInUser(), uniqueCode);
//		  
//		  log.info("event atmTicketEventCodeList for single ticket ===================: {}", uniqueCode);
//		  if (atmTicketEventList.isEmpty()) {
//			  log.error("No AtmTicketEvent found for ticket: {}", ticket.getSrno());
//			  throw new IllegalStateException("No AtmTicketEvent found for the given ticket"+ticket);
//		  }
//		  
//		  AtmTicketEvent atmTicketEvent = atmTicketEventList.get(0);
//		  
//		  TicketShortDetailsDto responseDetails = new TicketShortDetailsDto();
//		  responseDetails.setCeName(atmTicketEvent.getCeName());
//		  responseDetails.setAtmId(request.getAtmid());
//		  responseDetails.setTicketNumber(request.getTicketno());
//		  responseDetails.setCreatedOn(
//			        ticket.getCreateddate() != null
//			            ? CustomDateFormattor.convert(ticket.getCreateddate(), CustomDateFormattor.FormatStyle.SIMPLE_ENGLISH)
//			            : ""
//			    );	      
//		  responseDetails.setStatus(SafeValueUtil.nullToEmpty(ticket.getStatus()));
//		  responseDetails.setDownHrs(
//			        ticket.getCreateddate() != null
//			            ? DateUtil.getDownTimeInHrsHims(ticket.getCreateddate())
//			            : ""
//			    );	      
//		  responseDetails.setTicketType(atmTicketEvent.getDownCall()==1?"Down":"");
//		  responseDetails.setOwner(SafeValueUtil.nullToEmpty(atmTicketEvent.getOwner()));
//		  responseDetails.setSubcalltype(SafeValueUtil.nullToEmpty(ticket.getSubcalltype()));
//		  responseDetails.setInternalRemarks(SafeValueUtil.nullToEmpty(atmTicketEvent.getInternalRemark()));
//		  responseDetails.setLatestEtaDateTime(
//			        atmTicketEvent.getEtaDateTime() != null
//			            ? CustomDateFormattor.convert(atmTicketEvent.getEtaDateTime(), CustomDateFormattor.FormatStyle.SIMPLE_ENGLISH)
//			            : ""
//			    );	      
//		  responseDetails.setVendor(SafeValueUtil.nullToEmpty(ticket.getVendor()));
//		  log.info("Ticket Details Response: {}",responseDetails);
//		  return responseDetails;
//	}

}
