package com.hpy.ops360.ticketing.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.ApproveRejectStatusDto;
import com.hpy.ops360.ticketing.dto.TaskApprovedTicketsDto;
import com.hpy.ops360.ticketing.dto.TicketUpdateEvent;
import com.hpy.ops360.ticketing.entity.CMTaskDetails;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.CeMachineUpDownCountRepository;
import com.hpy.ops360.ticketing.repository.CmAtmDetailsRepository;
import com.hpy.ops360.ticketing.repository.CmTaskDetailsApproveOrRejectRepository;
import com.hpy.ops360.ticketing.repository.CmTaskDetailsRepository;
import com.hpy.ops360.ticketing.repository.CmTaskRepository;
import com.hpy.ops360.ticketing.repository.RejectionReasonRepository;
import com.hpy.ops360.ticketing.repository.TicketCountRepository;
import com.hpy.ops360.ticketing.repository.TicketUpdateDocumentRepository;
import com.hpy.ops360.ticketing.request.HimsCreateTicketRequest;
import com.hpy.ops360.ticketing.response.CreateTicketResponse;
import com.hpy.ops360.ticketing.response.HimsCreateTicketResponse;
import com.hpy.ops360.ticketing.ticket.dto.CreateTicketDto;
import com.hpy.ops360.ticketing.util.TicketEventProducer;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ManualTicketApproveOrRejectServiceByKafka {

	
	@Autowired
	private LoginService loginService;

	@Autowired
	private CmTaskRepository cmTaskRepository;

	@Autowired
	private UserAtmDetailsService userAtmDetailsService;

	@Autowired
	private RejectionReasonRepository reasonRepository;

	@Autowired
	private CmAtmDetailsRepository cmAtmDetailsRepository;

	@Autowired
	private SynergyService synergyService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TicketCountRepository ticketCountRepository;

	@Autowired
	private CmTaskDetailsRepository cmTaskDetailsRepository;
	
	@Autowired
	private CmTaskDetailsApproveOrRejectRepository approveOrRejectRepository;

	@Autowired
	private TicketUpdateDocumentRepository ticketUpdateDocumentRepository;

	@Autowired
	private CeMachineUpDownCountRepository ceUpDownCountRepo;

	@Value("${ops360.ticketing.max-atm-list-count}")
	private int maxAtmListCount;

	@Value("${ops360.ticketing.atm-batch-size}")
	private int atmBatchSize;

	@Autowired
	private TicketEventProducer ticketEventProducer;
	
	@Autowired
	private HimsService himsService;
	

	public ApproveRejectStatusDto updateTicketsNumberData_ViaKafka(String status, TaskApprovedTicketsDto taskDTO) {
		ApproveRejectStatusDto result;
		try {
			if (status.equalsIgnoreCase("approved")) {
				result = handleApprovedTicket(taskDTO);
			} else {
				result = handleRejectedTicket(taskDTO);
			}
			// Send event to Kafka
			TicketUpdateEvent event = TicketUpdateEvent.builder().ticketNumber(taskDTO.getTicketNumber())
					.atmId(taskDTO.getAtmId()).status(status).userName(taskDTO.getUserName())
					.checkerName(loginService.getLoggedInUser()).checkerComment(taskDTO.getCheckerComment())
					.synergyTicketNo(result.getSynergyTicketNo()).timestamp(LocalDateTime.now()).build();

			ticketEventProducer.sendTicketUpdate(event);

			return result;
		} catch (Exception e) {
			log.error("Error updating ticket: {}", e.getMessage(), e);
			throw new RuntimeException("Error processing ticket update", e);
		}
	}
	

	private ApproveRejectStatusDto handleRejectedTicket(TaskApprovedTicketsDto taskDTO) {
		Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(), "",
				taskDTO.getStatus(), loginService.getLoggedInUser(), taskDTO.getCheckerRejectReason(),
				taskDTO.getCheckerComment(), "rejected", taskDTO.getTicketNumber());

		return new ApproveRejectStatusDto(ticketStatus, "");
	}

//	private ApproveRejectStatusDto handleApprovedTicket(TaskApprovedTicketsDto taskDTO) {
//		CMTaskDetails ceTaskDetails = cmTaskDetailsRepository.getTicketsNumberDetails(taskDTO.getUserName(),
//				taskDTO.getTicketNumber(), taskDTO.getAtmId());
//
//		CreateTicketDto createTicketDto = new CreateTicketDto(ceTaskDetails.getAtmId(), ceTaskDetails.getTicketNumber(),
//				"Start diagnosis", ceTaskDetails.getCreatedBy(), "", "", ceTaskDetails.getComment());
//
//		log.info("Creating Synergy ticket: {}", createTicketDto);
//		CreateTicketResponse createTicketResponse = synergyService.createTicket(createTicketDto);
//		log.info("Synergy ticket created: {}", createTicketResponse);
//
//		Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(),
//				createTicketResponse.getTicketno(), taskDTO.getStatus(), loginService.getLoggedInUser(),
//				taskDTO.getCheckerRejectReason(), taskDTO.getCheckerComment(), "approved", taskDTO.getTicketNumber());
//
//		return new ApproveRejectStatusDto(ticketStatus, createTicketResponse.getTicketno());
//	}
	
	
	// Updated Service Method - Simplified Logic
	private ApproveRejectStatusDto handleApprovedTicket(TaskApprovedTicketsDto taskDTO) {
	    log.info("Handling approved ticket for ATM: {}, Ticket: {}", taskDTO.getAtmId(), taskDTO.getTicketNumber());
	    
	    // Get ticket details with ATM source from SP
	    CMTaskDetailsApproveOrRejectTicketByKafka ceTaskDetails = approveOrRejectRepository.getTicketsNumberDetailsWithSource(
	        taskDTO.getUserName(), 
	        taskDTO.getTicketNumber(), 
	        taskDTO.getAtmId()
	    );
	    
	    if (ceTaskDetails == null) {
	        log.error("No ticket details found for ATM: {}, Ticket: {}", taskDTO.getAtmId(), taskDTO.getTicketNumber());
	        throw new RuntimeException("Ticket details not found");
	    }
	    
	    // Get ATM source directly from SP result
	    String atmSource = ceTaskDetails.getAtmSource();
	    log.info("ATM {} source from SP: {}", ceTaskDetails.getAtmId(), atmSource);
	    
	    // Validate ATM source
	    if (atmSource == null || atmSource.trim().isEmpty()) {
	        log.error("ATM source is null/empty for ATM: {}", ceTaskDetails.getAtmId());
	        throw new RuntimeException("ATM source not found for ATM: " + ceTaskDetails.getAtmId());
	    }
	    
	    CreateTicketResponse createTicketResponse = null;
	    
	    // Check ATM source and create ticket accordingly
	    if ("synergy".equalsIgnoreCase(atmSource)) {
	        log.info("ATM {} is from Synergy source, creating Synergy ticket", ceTaskDetails.getAtmId());
	        
	        createTicketResponse = createSynergyTicket(ceTaskDetails);
	        
	    } else if ("hims".equalsIgnoreCase(atmSource)) {
	        log.info("ATM {} is from HIMS source, creating HIMS ticket", ceTaskDetails.getAtmId());
	        
	        createTicketResponse = createHimsTicket(ceTaskDetails);
	        
	    } else {
	        log.error("Unknown ATM source: {} for ATM: {}", atmSource, ceTaskDetails.getAtmId());
	        throw new RuntimeException("Unknown ATM source: " + atmSource + " for ATM: " + ceTaskDetails.getAtmId());
	    }
	    
	    // Validate ticket creation response
	    if (createTicketResponse == null || createTicketResponse.getTicketno() == null) {
	        log.error("Failed to create ticket - null response for ATM: {}", ceTaskDetails.getAtmId());
	        throw new RuntimeException("Failed to create ticket - null response");
	    }
	    
	    log.info("Ticket created successfully with ticket number: {}", createTicketResponse.getTicketno());
	    
	    // Update ticket status in database
	    Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(
	        taskDTO.getUserName(),
	        taskDTO.getAtmId(),
	        createTicketResponse.getTicketno(),
	        taskDTO.getStatus(),
	        loginService.getLoggedInUser(),
	        taskDTO.getCheckerRejectReason(),
	        taskDTO.getCheckerComment(),
	        "approved",
	        taskDTO.getTicketNumber()
	    );
	    
	    log.info("Ticket status updated in database. Status: {}, Ticket: {}", 
	             ticketStatus, createTicketResponse.getTicketno());
	    
	    return new ApproveRejectStatusDto(ticketStatus, createTicketResponse.getTicketno());
	}

	// Helper method to create Synergy ticket
	private CreateTicketResponse createSynergyTicket(CMTaskDetailsApproveOrRejectTicketByKafka ceTaskDetails) {
	    log.info("Creating Synergy ticket for ATM: {}", ceTaskDetails.getAtmId());
	    
	    // Create Synergy ticket DTO
	    CreateTicketDto createTicketDto = new CreateTicketDto(
	        ceTaskDetails.getAtmId(),
	        ceTaskDetails.getTicketNumber(),
	        "Start diagnosis",
	        ceTaskDetails.getCreatedBy(),
	        "",  // Additional field 1
	        "",  // Additional field 2
	        ceTaskDetails.getComment()
	    );
	    
	    log.info("Creating Synergy ticket: {}", createTicketDto);
	    CreateTicketResponse response = synergyService.createTicket(createTicketDto);
	    log.info("Synergy ticket created: {}", response);
	    
	    return response;
	}

	// Helper method to create HIMS ticket
	private CreateTicketResponse createHimsTicket(CMTaskDetailsApproveOrRejectTicketByKafka ceTaskDetails) {
	    log.info("Creating HIMS ticket for ATM: {}", ceTaskDetails.getAtmId());
	    
	    // Create HIMS ticket DTO
	    HimsCreateTicketRequest himsRequest = new HimsCreateTicketRequest(
	        ceTaskDetails.getAtmId(),
	        ceTaskDetails.getTicketNumber(),  // refno
	        "Start diagnosis",                // diagnosis
	        ceTaskDetails.getCreatedBy(),     // createdby
	        ceTaskDetails.getComment()       // comment
	       // ceTaskDetails.getEventCode() != null ? ceTaskDetails.getEventCode() : "General Issue"  // eventcode
	    );
	    
	    log.info("Creating HIMS ticket: {}", himsRequest);
	    CreateTicketResponse himsResponse = null;
		try {
			//himsResponse = himsService.createTicketWithValidation(himsRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    log.info("HIMS ticket created: {}", himsResponse);
	    
	    // Convert HIMS response to common CreateTicketResponse format
	    CreateTicketResponse response = new CreateTicketResponse(
	        himsResponse.getTicketno(),
	        himsResponse.getStatus(),
	        himsResponse.getRequestid()
	    );
	    
	    return response;
	}
	
	
	


}
