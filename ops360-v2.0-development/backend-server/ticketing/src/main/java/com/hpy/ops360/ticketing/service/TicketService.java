package com.hpy.ops360.ticketing.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.generic.Exception.EntityNotFoundException;
import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.TicketsRaisedCountresponseDto;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.LoginRequestDto;
import com.hpy.ops360.ticketing.dto.TicketAtmDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketClosureDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketDto;
import com.hpy.ops360.ticketing.dto.TicketReasonDto;
import com.hpy.ops360.ticketing.dto.TicketStatusDto;
import com.hpy.ops360.ticketing.dto.VendorDetailsDto;
import com.hpy.ops360.ticketing.entity.ATMBankName;
import com.hpy.ops360.ticketing.entity.AtmTicketEvent;
import com.hpy.ops360.ticketing.entity.CheckAtmSource;
import com.hpy.ops360.ticketing.entity.MTDUptimeDetails;
import com.hpy.ops360.ticketing.entity.MTDUptimeHims;
import com.hpy.ops360.ticketing.entity.Ticket;
import com.hpy.ops360.ticketing.entity.TicketClosure;
import com.hpy.ops360.ticketing.entity.TicketDetailsView;
import com.hpy.ops360.ticketing.entity.TicketsRaisedCount;
import com.hpy.ops360.ticketing.enums.TicketCategory;
import com.hpy.ops360.ticketing.enums.TicketStatus;
import com.hpy.ops360.ticketing.feignclient.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.ticketing.fiegnclient.OAuthFeignClient;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.mapper.TicketMapper;
import com.hpy.ops360.ticketing.repository.ATMBankNameRepository;
import com.hpy.ops360.ticketing.repository.AtmTicketEventRepository;
import com.hpy.ops360.ticketing.repository.BroadCategoryRepository;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.MTDUptimeDetailsRepository;
import com.hpy.ops360.ticketing.repository.MTDUptimeHimsRepository;
import com.hpy.ops360.ticketing.repository.ManualTicketReasonRepository;
import com.hpy.ops360.ticketing.repository.OwnerRepository;
import com.hpy.ops360.ticketing.repository.TaskRepository;
import com.hpy.ops360.ticketing.repository.TicketClosureRepository;
import com.hpy.ops360.ticketing.repository.TicketDetailsViewHimsRepository;
import com.hpy.ops360.ticketing.repository.TicketRaisedCountRepository;
import com.hpy.ops360.ticketing.repository.TicketRepository;
import com.hpy.ops360.ticketing.response.AtmUptimeDetailsResp;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.response.SynergyResponse;
import com.hpy.ops360.ticketing.ticket.dto.SelectedCategoryDto;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor.FormatStyle;
import com.hpy.ops360.ticketing.utils.DateUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TicketService extends GenericService<TicketDto, Ticket> {

	private final TicketMapper ticketMapper;

	private final TicketRepository ticketRepository;
	@Autowired
	private TicketRaisedCountRepository ticketRaisedCountRepository;

	private final SynergyService synergyService;

	private final LoginService loginService;

	private final ManualTicketReasonRepository manualTicketReasonRepository;

	private final int defaultTat;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private OAuthFeignClient oauthfiegnclient;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private BroadCategoryRepository broadCategoryRepository;

	@Autowired
	private AtmTicketEventRepository atmTicketEventRepository;

	@Autowired
	private DateUtil dateUtil;
	
	@Autowired
	private OwnerService ownerService;

	@Autowired
	private ATMBankNameRepository atmBankNameRepository;
	
	@Autowired
	private TicketClosureRepository ticketClosureRepository;
	
	private CheckAtmSourceRepository checkAtmSourceRepository;
	
	private TicketDetailsViewHimsRepository ticketDetailsViewHimsRepository;
	
	private MTDUptimeHimsRepository mtdUptimeHimsRepository;
	
	private MTDUptimeDetailsRepository mtdUptimeDetailsRepository;
	
	public TicketService(TicketMapper ticketMapper, TicketRepository ticketRepository, SynergyService synergyService,
			LoginService loginService, ManualTicketReasonRepository manualTicketReasonRepository,
			@Value("${ops360.ticketing.default.tat}") Integer defaultTat, OwnerRepository ownerRepository,
			BroadCategoryRepository broadCategoryRepository, AtmTicketEventRepository atmTicketEventRepository,CheckAtmSourceRepository checkAtmSourceRepository,TicketDetailsViewHimsRepository ticketDetailsViewHimsRepository,MTDUptimeHimsRepository mtdUptimeHimsRepository,MTDUptimeDetailsRepository mtdUptimeDetailsRepository) {
		this.ticketMapper = ticketMapper;
		this.ticketRepository = ticketRepository;
		this.synergyService = synergyService;
		this.loginService = loginService;
		this.manualTicketReasonRepository = manualTicketReasonRepository;
		this.defaultTat = defaultTat;
		this.ownerRepository = ownerRepository;
		this.broadCategoryRepository = broadCategoryRepository;
		this.atmTicketEventRepository = atmTicketEventRepository;
		this.checkAtmSourceRepository = checkAtmSourceRepository;
		this.ticketDetailsViewHimsRepository = ticketDetailsViewHimsRepository;
		this.mtdUptimeHimsRepository = mtdUptimeHimsRepository;
		this.mtdUptimeDetailsRepository = mtdUptimeDetailsRepository;
	}

	public TicketDto createNewTicket(TicketDto ticketDto) {

		// FIXME hardcoded TicketCategory for now.

		String retrivedTicketNumber = getGenereatedTicketNumber(TicketCategory.INFRA);

		LocalDateTime currentDateTime = LocalDateTime.now();

		log.info("new TicketNumber:{} ", retrivedTicketNumber);
		ticketDto.setTicketNumber(retrivedTicketNumber);
		ticketDto.setStatus(TicketStatus.OPEN.toString());
		log.info("newTicket{} ", ticketDto);
		return save(ticketDto);

	}

	public TicketDto updateTicketStatus(TicketStatusDto ticketStatusDto) throws EntityNotFoundException {
		TicketDto existingTicket = findById(ticketStatusDto.getTicketId());
		if (ticketStatusDto.getStatus().equalsIgnoreCase("CLOSED")) {
			existingTicket.setStatus(TicketStatus.CLOSED.toString());
		} else if (ticketStatusDto.getStatus().equalsIgnoreCase("INPROGRESS")) {
			existingTicket.setStatus(TicketStatus.INPROGRESS.toString());
		}

		return update(existingTicket);
	}

	protected String getGenereatedTicketNumber(TicketCategory ticketCategory) {
		// FIXME get max by TicketCategory wise
		Integer retrivedTicketNumber = ticketRepository.findMaxTicketNumber();

		if (retrivedTicketNumber == null) {
			retrivedTicketNumber = 0;
		}

		log.info("retrivedTicketNumber:{} ", retrivedTicketNumber);

		return ticketCategory.name() + ++retrivedTicketNumber;
	}

	public List<TicketDto> getTicketsByStatus(String status) {
		List<Ticket> tickets = ticketRepository.findByStatusIgnoreCase(status);
		log.info("tickets: {}", tickets);
		return ticketMapper.toDto(tickets);
	}


	public List<TicketDto> getAllTickets() {
		List<Ticket> tickets = ticketRepository.findAll();
		return ticketMapper.toDto(tickets);
	}


	public GenericResponseDto closeTicket(TicketClosureDto ticketClosureDto) {
		log.info("Ticket Closure Request: {}",ticketClosureDto);
		LoginRequestDto request = new LoginRequestDto();
		request.setUsername(ticketClosureDto.getUsername());
		request.setPassword(ticketClosureDto.getPassword());
		String closureDate = ticketClosureDto.getClosureDate();
		try {
			oauthfiegnclient.login(request);
			if (!isValidClosureDateFormat(closureDate)) {
				return new GenericResponseDto("Failed", "Invalid date format! Expected format: DD/MM/YYYY HH:MM");
			}
			taskRepository.closeTicket(ticketClosureDto.getTicketNo(), ticketClosureDto.getAtmId(),
					ticketClosureDto.getComments(), ticketClosureDto.getClosureDate());
			return new GenericResponseDto("success", "ticket closed");
		} catch (Exception e) {
			return new GenericResponseDto("Failed", "Invalid credential!!!");
		}
	}
	
	private boolean isValidClosureDateFormat(String closureDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		sdf.setLenient(false);
		try {
			 // Parse the date
	        Date date = sdf.parse(closureDate);
	        // Format the date back to string
	        String formattedDate = sdf.format(date);
	        // Compare the formatted date with the original input
	        return closureDate.equals(formattedDate);
		}
		catch(ParseException e){
			log.error("ParseException: Invalid date format: {}", closureDate);
			return false;
		}
		
	}

	public List<TicketReasonDto> getTicketReasonList() {

		return manualTicketReasonRepository.getManualTicketReason().stream()
				.map(e -> new TicketReasonDto(e.getReason())).toList().isEmpty() ? Collections.emptyList()
						: manualTicketReasonRepository.getManualTicketReason().stream()
								.map(e -> new TicketReasonDto(e.getReason())).toList();

	}

	 // ----------------modified ---------------------
	// -------------------CM portal-------------------

//	public TicketAtmDetailsDto (TicketDetailsReqWithoutReqId ticketDetailsRequest) {
//        if (ticketDetailsRequest == null) {
//            log.error("TicketDetailsReqWithoutReqId is null");
//            throw new IllegalArgumentException("TicketDetailsReqWithoutReqId cannot be null");
//        }
//        // Fetch ticket details
//        TicketDetailsDto ticket = synergyService.getTicketDetails(ticketDetailsRequest);
//        log.info("Fetching ticket details for request: {}", ticketDetailsRequest);
//        if (ticket == null) {
//            log.error("Fetched ticket details are null for request: {}", ticketDetailsRequest);
//            throw new IllegalStateException("Fetched ticket details cannot be null");
//        }
//        // Create the unique code for the single ticket
//        String uniqueCode = String.format("%s|%s|%s|%s|%s", 
//            ticket.getEquipmentid(),
//            ticket.getSrno(), 
//            ticket.getEventcode(), 
//            ticket.getNextfollowup(),
//            ticket.getCalldate());
//        // Fetch AtmTicketEvent for the single ticket
//        List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
//            .getAtmTicketEvent(loginService.getLoggedInUser(), uniqueCode);
//        log.info("event atmTicketEventCodeList for single ticket ===================: {}", uniqueCode);
//        if (atmTicketEventList.isEmpty()) {
//            log.error("No AtmTicketEvent found for ticket: {}", ticket.getSrno());
//            throw new IllegalStateException("No AtmTicketEvent found for the given ticket"+ticket);
//        }
//        AtmTicketEvent atmTicketEvent = atmTicketEventList.get(0);
//        // Create AtmShortDetailsDto using the provided logic
//        AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
//            .owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
//            .subcall(ticket.getSubcalltype())	  
//            .etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" : atmTicketEvent.getEtaDateTime())
//            .etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
//            .travelEta(atmTicketEvent.getTravelEta())
//            .build();
//        // Fetch ATM Bank Name
//        ATMBankName atmBankName = atmBankNameRepository.getATMBankName(ticketDetailsRequest.getAtmid(),
//                ticketDetailsRequest.getTicketno());
//        log.info("Fetching ATM uptime details for ATM ID: {}", ticketDetailsRequest.getAtmid());
//        AtmUptimeDetailsResp atmUptimeDetailsResp = synergyService.getAtmUptime(ticketDetailsRequest.getAtmid());
//        // Use the subcall from AtmShortDetailsDto
//        String subcallType = atmShortDetailsDto.getSubcall();
//        // If subcallType is still null or empty, fetch it from Synergy
//        if (subcallType == null || subcallType.isEmpty()) {
//            log.info("SubcallType is empty, fetching from Synergy: {}", ticketDetailsRequest.getTicketno());
//            
//            UpdateSubcallTypeDto updateSubcallTypeDto = UpdateSubcallTypeDto.builder()
//                    .ticketNo(ticketDetailsRequest.getTicketno())
//                    .atmId(ticketDetailsRequest.getAtmid())
//                    .subcallType(subcallType)
//                    .comments("SubcallType fetched from Synergy")
//                    .build();
//            SynergyResponse synergyResponse = synergyService.updateSubcallType(updateSubcallTypeDto);
//            subcallType = synergyResponse.getRequestid();
//            log.info("SubcallType fetched from Synergy: {}", subcallType);
//        }
//     // Fetch the broad category and owner using the provided method
//        SelectedCategoryDto selectedCategoryDto = ownerService.getBroadCategory(subcallType,ticket.getSrno(),ticket.getEquipmentid());
//        String owner = selectedCategoryDto.getSelectedCategory();
//        
//        // Vendor details logic
//        List<VendorDetailsDto> vendorDetails = new ArrayList<>();
//        if (ticket.getServicecode() != null && ticket.getVendor() != null) {
//            vendorDetails.add(new VendorDetailsDto(ticket.getServicecode(), ticket.getVendor()));
//        }
//        // Format dates
//        String formattedCreatedDate = DateUtil.formatDateString(ticket.getCreateddate());
//        String formattedCompletionDate = DateUtil.formatDateString(ticket.getCompletiondatewithtime());
//        String formattedCreatedDateForCEApp = DateUtil.formatDateStringCeCreatedDate(ticket.getCreateddate());
//        
//        // Construct the final TicketAtmDetailsDto
//        return new TicketAtmDetailsDto(
//	            null, ticket.getSrno(), ticket.getEventcode(),
//	            formattedCreatedDate, formattedCompletionDate, defaultTat,
//	            ticket.getEquipmentid(), ticket.getAddress(),
//	            atmUptimeDetailsResp != null ? atmUptimeDetailsResp.getMonthtotilldateuptime() : 0.0,
//	            ticket.getVendor(), vendorDetails, atmBankName.getAccount(),
//	            owner,  // Owner field
//	            subcallType , // SubcallType field
//	            atmTicketEvent.getEtaDateTime(), // etaDateTime field
//	            atmTicketEvent.getEtaTimeout(), // etaTimeout field
//	            atmTicketEvent.getTravelEta(), //
//	            formattedCreatedDateForCEApp
//	            
//	    );
//    }
	
	//Actual  method use in case of revert
	public TicketAtmDetailsDto getTicketDetailsByNumber(TicketDetailsReqWithoutReqId ticketDetailsRequest) {
	    if (ticketDetailsRequest == null) {
	        log.error("TicketDetailsReqWithoutReqId is null");
	        throw new IllegalArgumentException("TicketDetailsReqWithoutReqId cannot be null");
	    }

	    try {
	        TicketDetailsDto ticket = synergyService.getTicketDetails(ticketDetailsRequest);
	        log.info("Fetching ticket details for request: {}", ticketDetailsRequest);

	        if (ticket == null) {
	            log.error("Fetched ticket details are null for request: {}", ticketDetailsRequest);
	            throw new IllegalStateException("Fetched ticket details cannot be null");
	        }

	        String uniqueCode = String.format("%s|%s|%s|%s|%s",
	                ticket.getEquipmentid(), ticket.getSrno(), ticket.getEventcode(),
	                ticket.getNextfollowup(), ticket.getCalldate());

	        List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
	                .getAtmTicketEvent(loginService.getLoggedInUser(), uniqueCode);
	        log.info("event atmTicketEventCodeList for single ticket: {}", uniqueCode);

	        if (atmTicketEventList == null || atmTicketEventList.isEmpty()) {
	            log.error("No AtmTicketEvent found for ticket: {}", ticket.getSrno());
	            throw new IllegalStateException("No AtmTicketEvent found for the given ticket: " + ticket);
	        }

	        AtmTicketEvent atmTicketEvent = atmTicketEventList.get(0);

	        AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
	                .owner(Optional.ofNullable(atmTicketEvent.getOwner()).orElse(""))
	                .subcall(ticket.getSubcalltype())
	                .etaDateTime(Optional.ofNullable(atmTicketEvent.getEtaDateTime()).orElse(""))
	                .etaTimeout(Optional.ofNullable(atmTicketEvent.getEtaTimeout()).orElse(""))
	                .travelEta(atmTicketEvent.getTravelEta())
	                .build();
	        
	        //not needed get it from TicketDetailsDto ticket
	        ATMBankName atmBankName = atmBankNameRepository.getATMBankName(
	                ticketDetailsRequest.getAtmid(), ticketDetailsRequest.getTicketno());

	        AtmUptimeDetailsResp atmUptimeDetailsResp = synergyService.getAtmUptime(ticketDetailsRequest.getAtmid());

	        String subcallType = atmShortDetailsDto.getSubcall();
	        
	        //check this logic - not needed get subcalltype from synergy
	        if (subcallType == null || subcallType.isEmpty()) {
	            log.info("SubcallType is empty, fetching from Synergy: {}", ticketDetailsRequest.getTicketno());
	            UpdateSubcallTypeDto updateSubcallTypeDto = UpdateSubcallTypeDto.builder()
	                    .ticketNo(ticketDetailsRequest.getTicketno())
	                    .atmId(ticketDetailsRequest.getAtmid())
	                    .subcallType(subcallType)
	                    .comments("SubcallType fetched from Synergy")
	                    .build();

	            SynergyResponse synergyResponse = synergyService.updateSubcallType(updateSubcallTypeDto);
	            subcallType = synergyResponse != null ? synergyResponse.getRequestid() : "";
	            log.info("SubcallType fetched from Synergy: {}", subcallType);
	        }

	        SelectedCategoryDto selectedCategoryDto = ownerService.getBroadCategory(
	                subcallType, ticket.getSrno(), ticket.getEquipmentid());
	        String owner = selectedCategoryDto != null ? selectedCategoryDto.getSelectedCategory() : "";

	        List<VendorDetailsDto> vendorDetails = new ArrayList<>();
	        if (ticket.getServicecode() != null && ticket.getVendor() != null) {
	            vendorDetails.add(new VendorDetailsDto(ticket.getServicecode(), ticket.getVendor()));
	        }

	        String formattedCreatedDate = DateUtil.formatDateString(ticket.getCreateddate());
	        String formattedCompletionDate = DateUtil.formatDateString(ticket.getCompletiondatewithtime());
	        String formattedCreatedDateForCEApp = DateUtil.formatDateStringCeCreatedDate(ticket.getCreateddate());

	        String formattedETAforAPP = "";
	        try {
	            formattedETAforAPP = DateUtil.convertDateFormat(atmTicketEvent.getEtaDateTime());
	        } catch (Exception e) {
	            log.warn("Failed to format ETA date: {}", atmTicketEvent.getEtaDateTime(), e);
	        }

	        return new TicketAtmDetailsDto(
	                null,
	                ticket.getSrno(),
	                ticket.getEventcode(),
	                formattedCreatedDate,
	                formattedCompletionDate,
	                defaultTat,
	                ticket.getEquipmentid(),
	                ticket.getAddress(),
	                atmUptimeDetailsResp != null ? atmUptimeDetailsResp.getMonthtotilldateuptime() : 0.0,
	                ticket.getVendor(),
	                vendorDetails,
	                atmBankName != null ? atmBankName.getAccount() : "",
	                owner,
	                subcallType,
	                formattedETAforAPP,
	                atmTicketEvent.getEtaTimeout(),
	                atmTicketEvent.getTravelEta(),
	                formattedCreatedDateForCEApp
	        );

	    } catch (Exception e) {
	        log.error("Unexpected error in ", e);
	        throw new RuntimeException("Internal processing error", e);
	    }
	}
	
	public TicketAtmDetailsDto getTicketDetailsByNumberHims(TicketDetailsReqWithoutReqId ticketDetailsRequest) {
	    if (ticketDetailsRequest == null) {
	        log.error("TicketDetailsReqWithoutReqId is null");
	        throw new IllegalArgumentException("TicketDetailsReqWithoutReqId cannot be null");
	    }
	    if (ticketDetailsRequest.getAtmid().isEmpty() || ticketDetailsRequest.getTicketno().isEmpty()) {
	    	return TicketAtmDetailsDto.builder()
		    		.id(null)
		    	    .ticketNumber("")
		    	    .problem("")
		    	    .startTime("")
		    	    .endTime("")
		    	    .expectedTat(0)
		    	    .atmNo("")
		    	    .address("")
		    	    .mtdUptime(0.0)
		    	    .cra("")
		    	    .vendorDetails(new ArrayList<>())
		    	    .bankAccount("")
		    	    .Owner("")
		    	    .subcallType("")
		    	    .etaDateTime("")
		    	    .etaTimeout("")
		    	    .travelEta(0)
		    	    .createDateTime("")
		    	    .build();
		}
	    TicketDetailsView ticketHims= new TicketDetailsView();
	    TicketDetailsDto ticket=new TicketDetailsDto();
	    try {
	    	
	    	List<CheckAtmSource> atms=checkAtmSourceRepository.checkAtmSource(ticketDetailsRequest.getAtmid());
	    	if (atms.isEmpty()) {
	    		log.error("Atm list with source is empty insert valid atm id");
		        throw new IllegalArgumentException("Atm list with source is empty");
			}
	    	log.info("Atm list with source response: {}",atms);
	    	int source = atms.get(0).getSourceCode();
	    	if(source==0) //synergy
	    	{
		        ticket = synergyService.getTicketDetails(ticketDetailsRequest);
		        log.info("Fetching ticket details for request: {}", ticketDetailsRequest);
	
		        if (ticket == null) {
		            log.error("Fetched ticket details are null for request: {}", ticketDetailsRequest);
		            throw new IllegalStateException("Fetched ticket details cannot be null");
		        }
		        
		        log.info("syn ticket res: {}",ticket);
		    	log.info("hims ticket res: {}",ticketHims);
		        return buildTicketDetailsResponseBasedOnSource(ticketDetailsRequest, ticket,ticketHims);
	    	}
	    	else
	    	{
	    		ticketHims=ticketDetailsViewHimsRepository.getTicketDetailsFromHims(ticketDetailsRequest.getTicketno(), ticketDetailsRequest.getAtmid());
	    		log.info("hims ticket res: {}",ticketHims);
		        return buildTicketDetailsResponseBasedOnSource(ticketDetailsRequest, ticket,ticketHims);
	    	}
	    	

	    } catch (Exception e) {
	        log.error("Unexpected error in ", e);
	        throw new RuntimeException("Internal processing error", e);
	    }
	}

	private TicketAtmDetailsDto buildTicketDetailsResponseBasedOnSource(
			TicketDetailsReqWithoutReqId ticketDetailsRequest, TicketDetailsDto ticket, TicketDetailsView ticketHims) {
		log.info("ticket from syn: {}",ticket);
		log.info("ticket from hims: {}",ticketHims);
		if ((ticket == null || ticket.getSrno() == null) && (ticketHims == null || ticketHims.getSrno() == null)) {
		    log.warn("Both Synergy and HIMS ticket details are missing. Returning empty response.");
		    return TicketAtmDetailsDto.builder()
		    		.id(null)
		    	    .ticketNumber("")
		    	    .problem("")
		    	    .startTime("")
		    	    .endTime("")
		    	    .expectedTat(0)
		    	    .atmNo("")
		    	    .address("")
		    	    .mtdUptime(0.0)
		    	    .cra("")
		    	    .vendorDetails(new ArrayList<>())
		    	    .bankAccount("")
		    	    .Owner("")
		    	    .subcallType("")
		    	    .etaDateTime("")
		    	    .etaTimeout("")
		    	    .travelEta(0)
		    	    .createDateTime("")
		    	    .build();
		}

		if (ticket.getSrno()!=null) {
			log.info("Ticket Details from synergy");
			String uniqueCode = String.format("%s|%s|%s|%s|%s",
			        ticket.getEquipmentid(), ticket.getSrno(), ticket.getEventcode(),
			        ticket.getNextfollowup(), ticket.getCalldate());

			List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
			        .getAtmTicketEvent(loginService.getLoggedInUser(), uniqueCode);
			log.info("event atmTicketEventCodeList for single ticket: {}", uniqueCode);

			if (atmTicketEventList == null || atmTicketEventList.isEmpty()) {
			    log.error("No AtmTicketEvent found for ticket: {}", ticket.getSrno());
			    throw new IllegalStateException("No AtmTicketEvent found for the given ticket: " + ticket);
			}

			AtmTicketEvent atmTicketEvent = atmTicketEventList.get(0);

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
			        .owner(Optional.ofNullable(atmTicketEvent.getOwner()).orElse(""))
			        .subcall(ticket.getSubcalltype())
			        .etaDateTime(Optional.ofNullable(atmTicketEvent.getEtaDateTime()).orElse(""))
			        .etaTimeout(Optional.ofNullable(atmTicketEvent.getEtaTimeout()).orElse(""))
			        .travelEta(atmTicketEvent.getTravelEta())
			        .build();

			ATMBankName atmBankName = atmBankNameRepository.getATMBankName(
			        ticketDetailsRequest.getAtmid(), ticketDetailsRequest.getTicketno());

			//AtmUptimeDetailsResp atmUptimeDetailsResp = synergyService.getAtmUptime(ticketDetailsRequest.getAtmid());
			MTDUptimeDetails mtdUptimeDetails=mtdUptimeDetailsRepository.getAtmUptime(ticketDetailsRequest.getAtmid());
			String subcallType = atmShortDetailsDto.getSubcall();
			if (subcallType == null || subcallType.isEmpty()) {
			    log.info("SubcallType is empty, fetching from Synergy: {}", ticketDetailsRequest.getTicketno());
			    UpdateSubcallTypeDto updateSubcallTypeDto = UpdateSubcallTypeDto.builder()
			            .ticketNo(ticketDetailsRequest.getTicketno())
			            .atmId(ticketDetailsRequest.getAtmid())
			            .subcallType(subcallType)
			            .comments("SubcallType fetched from Synergy")
			            .build();

			    SynergyResponse synergyResponse = synergyService.updateSubcallType(updateSubcallTypeDto);
			    subcallType = synergyResponse != null ? synergyResponse.getRequestid() : "";
			    log.info("SubcallType fetched from Synergy: {}", subcallType);
			}

			SelectedCategoryDto selectedCategoryDto = ownerService.getBroadCategory(
			        subcallType, ticket.getSrno(), ticket.getEquipmentid());
			String owner = selectedCategoryDto != null ? selectedCategoryDto.getSelectedCategory() : "";

			List<VendorDetailsDto> vendorDetails = new ArrayList<>();
			if (ticket.getServicecode() != null && ticket.getVendor() != null) {
			    vendorDetails.add(new VendorDetailsDto(ticket.getServicecode(), ticket.getVendor()));
			}

			String formattedCreatedDate = DateUtil.formatDateString(ticket.getCreateddate());
			String formattedCompletionDate = DateUtil.formatDateString(ticket.getCompletiondatewithtime());
			String formattedCreatedDateForCEApp = DateUtil.formatDateStringCeCreatedDate(ticket.getCreateddate());

			String formattedETAforAPP = "";
			try {
			    formattedETAforAPP = DateUtil.convertDateFormat(atmTicketEvent.getEtaDateTime());
			} catch (Exception e) {
			    log.warn("Failed to format ETA date: {}", atmTicketEvent.getEtaDateTime(), e);
			}

			return new TicketAtmDetailsDto(
			        null,
			        ticket.getSrno(),
			        ticket.getEventcode(),
			        formattedCreatedDate,
			        formattedCompletionDate,
			        defaultTat,
			        ticket.getEquipmentid(),
			        ticket.getAddress(),
			        mtdUptimeDetails != null ? mtdUptimeDetails.getMtd() : 0.0,
			        ticket.getVendor(),
			        vendorDetails,
			        atmBankName != null ? atmBankName.getAccount() : "",
			        owner,
			        subcallType,
			        formattedETAforAPP,
			        atmTicketEvent.getEtaTimeout(),
			        atmTicketEvent.getTravelEta(),
			        formattedCreatedDateForCEApp
			);
		}
		
		//For HIMS
		
		String uniqueCode = String.format("%s|%s|%s|%s|%s",
		        ticketHims.getEquipmentid(), ticketHims.getSrno(), ticketHims.getEventcode(),
		        ticketHims.getNextfollowup(), ticketHims.getCalldate());
		
		log.info("uniqueCode for atmticketevent sp: {}", uniqueCode);

		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
		        .getAtmTicketEvent(loginService.getLoggedInUser(), uniqueCode);
		log.info("event atmTicketEventCodeList for single ticket: {}", uniqueCode);

		if (atmTicketEventList == null || atmTicketEventList.isEmpty()) {
		    log.error("No AtmTicketEvent found for ticket: {}", ticketHims.getSrno());
		    throw new IllegalStateException("No AtmTicketEvent found for the given ticket: " + ticketHims);
		}

		AtmTicketEvent atmTicketEvent = atmTicketEventList.get(0);

		AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
		        .owner(Optional.ofNullable(atmTicketEvent.getOwner()).orElse(""))
		        .subcall(ticketHims.getSubcalltype())
		        //.etaDateTime(Optional.ofNullable(atmTicketEvent.getEtaDateTime()).orElse(""))
		        .etaDateTime(ticketHims.getEtadatetime())
		        .etaTimeout(Optional.ofNullable(atmTicketEvent.getEtaTimeout()).orElse(""))
		        .travelEta(atmTicketEvent.getTravelEta())
		        .build();

//		ATMBankName atmBankName = atmBankNameRepository.getATMBankName(
//		        ticketDetailsRequest.getAtmid(), ticketDetailsRequest.getTicketno());
		String atmBankName = ticketHims.getCustomer();
		
		
		//AtmUptimeDetailsResp atmUptimeDetailsResp = synergyService.getAtmUptime(ticketDetailsRequest.getAtmid());
		//MTDUptimeHims mtdUptimeHims=mtdUptimeHimsRepository.getAtmUptimeHims(ticketDetailsRequest.getAtmid());
		MTDUptimeDetails mtdUptimeDetails=mtdUptimeDetailsRepository.getAtmUptime(atmBankName);
		String subcallType = atmShortDetailsDto.getSubcall();
//		if (subcallType == null || subcallType.isEmpty()) {
//		    log.info("SubcallType is empty, fetching from Synergy: {}", ticketDetailsRequest.getTicketno());
//		    UpdateSubcallTypeDto updateSubcallTypeDto = UpdateSubcallTypeDto.builder()
//		            .ticketNo(ticketDetailsRequest.getTicketno())
//		            .atmId(ticketDetailsRequest.getAtmid())
//		            .subcallType(subcallType)
//		            .comments("SubcallType fetched from Synergy")
//		            .build();
//
//		    SynergyResponse synergyResponse = synergyService.updateSubcallType(updateSubcallTypeDto);
//		    subcallType = synergyResponse != null ? synergyResponse.getRequestid() : "";
//		    log.info("SubcallType fetched from Synergy: {}", subcallType);
//		}

		SelectedCategoryDto selectedCategoryDto = ownerService.getBroadCategory(
		        subcallType, ticketHims.getSrno(), ticketHims.getEquipmentid());
		String owner = selectedCategoryDto != null ? selectedCategoryDto.getSelectedCategory() : "";

		List<VendorDetailsDto> vendorDetails = new ArrayList<>();
		if (ticketHims.getServicecode() != null && ticketHims.getVendor() != null) {
		    vendorDetails.add(new VendorDetailsDto(ticketHims.getServicecode(), ticketHims.getVendor()));
		}

		String formattedCreatedDate = CustomDateFormattor.convert(ticketHims.getCreateddate(), FormatStyle.DATABASE);
				//DateUtil.formatDateString(ticketHims.getCreateddate());
		String formattedCompletionDate =CustomDateFormattor.convert(ticketHims.getCompletiondatewithtime(), FormatStyle.DATABASE); 
				//DateUtil.formatDateString(ticketHims.getCompletiondatewithtime());
		String formattedCreatedDateForCEApp = CustomDateFormattor.convert(ticketHims.getCreateddate(), FormatStyle.COMPACT);
				//DateUtil.formatDateStringCeCreatedDate(ticketHims.getCreateddate());

		String formattedETAforAPP = "";
		try {
		    formattedETAforAPP = CustomDateFormattor.convert(atmTicketEvent.getEtaDateTime(), FormatStyle.COMPACT);
		    		//DateUtil.convertDateFormat(atmTicketEvent.getEtaDateTime());
		} catch (Exception e) {
		    log.warn("Failed to format ETA date: {}", atmTicketEvent.getEtaDateTime(), e);
		}

		return new TicketAtmDetailsDto(
		        null,
		        ticketHims.getSrno(),
		        ticketHims.getEventcode(),
		        formattedCreatedDate,
		        formattedCompletionDate,
		        defaultTat,
		        ticketHims.getEquipmentid(),
		        ticketHims.getAddress(),
		        mtdUptimeDetails != null ? mtdUptimeDetails.getMtd() : 0.0,
		        ticketHims.getVendor(),
		        vendorDetails,
		        atmBankName != null ? atmBankName: "",
		        owner,
		        ticketHims.getSubcalltype(),
		        formattedETAforAPP,
		        atmTicketEvent.getEtaTimeout(),
		        atmTicketEvent.getTravelEta(),
		        formattedCreatedDateForCEApp
		);
		
	}



	@Loggable
	public TicketsRaisedCountresponseDto getTicketsRaisedCount() {
		String userId = loginService.getLoggedInUser();
		TicketsRaisedCount count = ticketRaisedCountRepository.getTicketsRaisedCount(userId);
		return getTicketsRaisedCountResponseMapper(count);
	}

	private TicketsRaisedCountresponseDto getTicketsRaisedCountResponseMapper(TicketsRaisedCount count) {
		TicketsRaisedCountresponseDto countresponseDto = new TicketsRaisedCountresponseDto();
		countresponseDto.setSr_no(count.getSr_no());
		countresponseDto.setBreakdown(count.getBreakdown());
		countresponseDto.setService(count.getService());
		countresponseDto.setTotal(count.getTotal());
		countresponseDto.setUpdated(count.getUpdated());
		return countresponseDto;

	}

	public ResponseMessage checkTicketClosure(String ticketNumber, String atmId) {
		
		TicketClosure ticket =ticketClosureRepository.checkTicketClosure(ticketNumber, atmId);
		if (ticket.getStatus() == 1) {
			return new ResponseMessage("Ticket Closed", 1);
		}
		return new ResponseMessage("Ticket Not Found",0);
		
	}

}
