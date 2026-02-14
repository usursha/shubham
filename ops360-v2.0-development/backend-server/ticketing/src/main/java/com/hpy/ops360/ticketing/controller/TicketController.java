package com.hpy.ops360.ticketing.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.generic.Exception.EntityNotFoundException;
import com.hpy.ops360.ticketing.cm.dto.ACTransactionDetailsRequest;
import com.hpy.ops360.ticketing.cm.dto.AllocationDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.AllocationDetailsRequestDto;
import com.hpy.ops360.ticketing.cm.dto.AllocationETADetailsDto;
import com.hpy.ops360.ticketing.cm.dto.AllocationEtaDetailsRequestDto;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.CreationDetailsDTO;
import com.hpy.ops360.ticketing.cm.dto.Documentrequest;
import com.hpy.ops360.ticketing.cm.dto.FilterDTO;
import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketResponse;
import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketsRequest;
import com.hpy.ops360.ticketing.cm.dto.GetTicketDocumentsRequest;
import com.hpy.ops360.ticketing.cm.dto.GetTicketDocumentsResponse;
import com.hpy.ops360.ticketing.cm.dto.ImageFilterDocument;
import com.hpy.ops360.ticketing.cm.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.TicketWithDocumentsDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketsRaisedCountresponseDto;
import com.hpy.ops360.ticketing.cm.dto.TransTargetdetailsDto;
import com.hpy.ops360.ticketing.cm.dto.TransactionDetailsRequest;
import com.hpy.ops360.ticketing.cm.service.AllocationDetailsService;
import com.hpy.ops360.ticketing.cm.service.AllocationEtaDetailsService;
import com.hpy.ops360.ticketing.cm.service.CMTicketService;
import com.hpy.ops360.ticketing.cm.service.FlagTicketHistoryService;
import com.hpy.ops360.ticketing.cm.service.TicketFilterService;
import com.hpy.ops360.ticketing.cm.service.TicketWithDocumentsService;
import com.hpy.ops360.ticketing.cm.service.TransactionDetailsService;
import com.hpy.ops360.ticketing.dto.AllFormattedETADatesReqDTO;
import com.hpy.ops360.ticketing.dto.AllFormattedETADatesResponseDTO;
import com.hpy.ops360.ticketing.dto.AssignedAtmFilterResponseDto;
import com.hpy.ops360.ticketing.dto.AssignedAtmforCeFilterResponseDto;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.BroadCategoryForAtmHistoryFilterDto;
import com.hpy.ops360.ticketing.dto.EtaDto2;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.GroupTicketListResponse;
import com.hpy.ops360.ticketing.dto.GroupedTicketResponse;
import com.hpy.ops360.ticketing.dto.IsTravellingDto;
import com.hpy.ops360.ticketing.dto.RemarkHistoryResponseDto;
import com.hpy.ops360.ticketing.dto.RemarksDto;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryRequestDto;
import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryResponseDto;
import com.hpy.ops360.ticketing.dto.TaskDto;
import com.hpy.ops360.ticketing.dto.TaskSummaryOfCeResponseFilterDTO;
import com.hpy.ops360.ticketing.dto.TicketAtmDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketClosureDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketDto;
import com.hpy.ops360.ticketing.dto.TicketReasonDto;
import com.hpy.ops360.ticketing.dto.TicketStatusDto;
import com.hpy.ops360.ticketing.dto.TravelDto;
import com.hpy.ops360.ticketing.dto.TravelModeDto;
import com.hpy.ops360.ticketing.dto.TravelModeMessageDto;
import com.hpy.ops360.ticketing.dto.WorkModeDto;
import com.hpy.ops360.ticketing.entity.ManuallyTickets;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.ManuallyTicketRepository;
import com.hpy.ops360.ticketing.request.TicketClosureRequestDto;
import com.hpy.ops360.ticketing.request.UpdateWorkModeReq;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.service.AddEtaFromCmHimsAndSynergy;
import com.hpy.ops360.ticketing.service.AllFormattedETADatesService;
import com.hpy.ops360.ticketing.service.AssignedAtmforCeFilterService;
import com.hpy.ops360.ticketing.service.AtmTicketHistoryFilter;
import com.hpy.ops360.ticketing.service.CreationDetailsService;
import com.hpy.ops360.ticketing.service.EtaService;
import com.hpy.ops360.ticketing.service.ManuallyTicketService;
import com.hpy.ops360.ticketing.service.RemarksHistoryService;
import com.hpy.ops360.ticketing.service.TaskService;
import com.hpy.ops360.ticketing.service.TaskSummaryFilterOfCeService;
import com.hpy.ops360.ticketing.service.TicketService;
import com.hpy.ops360.ticketing.service.TravelModeService;
import com.hpy.ops360.ticketing.service.TravelService;
import com.hpy.ops360.ticketing.service.UpdateEtaByCeService;
import com.hpy.ops360.ticketing.service.WorkModeService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.exception.ErrorCode;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ticket")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class TicketController {

	private final TicketService ticketService;

	@Autowired
	private TicketFilterService ticketFilterService;

	private final TravelService travelService;

	private final TravelModeService travelModeService;

	private final WorkModeService workModeService;

	private final TaskService taskService;

	private final EtaService etaService;
	
	@Autowired
	private UpdateEtaByCeService  updateEtaByCeService;

	@Autowired
	private AllFormattedETADatesService allFormattedETADatesService;

	@Autowired
	private CMTicketService docticketService;

	@Autowired
	private final FlagTicketHistoryService flagservice;

	@Autowired
	private AllocationEtaDetailsService service;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private ManuallyTicketRepository repository;

	@Autowired
	private LoginService loginService;

	@Autowired
	private TaskSummaryFilterOfCeService taskSummaryFilterOfCeService;

	@Autowired
	private ManuallyTicketService manuallyTicketService;

	@Autowired
	private AssignedAtmforCeFilterService assignedAtmforCeFilterService;

	@Autowired
	private AtmTicketHistoryFilter atmTicketHistoryFilter;

	@Autowired
	private CreationDetailsService creationDetailsService;

	@Autowired
	private TransactionDetailsService transactionDetailsService;

	@Autowired
	private TicketWithDocumentsService documentsService;

	@Autowired
	private AllocationDetailsService allocationDetailsService;
	
	@Autowired
	private RemarksHistoryService remarkhistoryService ;
	
	@Autowired
	private AddEtaFromCmHimsAndSynergy addEtaFromCmHimsAndSynergy ;

	@PostMapping("/createTicket")
	@Loggable
	public ResponseEntity<GenericResponseDto> createTicket(@RequestBody TaskDto ticket,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		log.info("TicketController:createTicket:{}", ticket);
		return ResponseEntity.ok(taskService.createTask(ticket, token));
	}

	@PostMapping("/addCommentWithEta")
	@Loggable
	public ResponseEntity<GenericResponseDto> addEta(@RequestBody EtaDto2 etaDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		//return ResponseEntity.ok(etaService.addEta(etaDto, token));
		return ResponseEntity.ok(updateEtaByCeService.addEta(etaDto, token));
	}
	
	@PostMapping("/addCommentWithEta-hims")
	@Loggable
	public ResponseEntity<GenericResponseDto> addEtahims(@RequestBody EtaDto2 etaDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		//return ResponseEntity.ok(etaService.addEta(etaDto, token));
		return ResponseEntity.ok(updateEtaByCeService.addEtahims(etaDto, token));
	}

	@PostMapping("/Comment-EtaByCm")
	@Loggable
	public ResponseEntity<GenericResponseDto> addEtaFromCm(@RequestBody EtaDto2 etaDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		log.info("TicketController:addEtaFromCm:{}", etaDto);
		return ResponseEntity.ok(addEtaFromCmHimsAndSynergy.addEtaFromCmHimsAndSynergy(etaDto, token));
	}
	
	@PostMapping("/Comment-EtaByCm-hims")
	@Loggable
	public ResponseEntity<GenericResponseDto> addEtaFromCmhims(@RequestBody EtaDto2 etaDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return ResponseEntity.ok(addEtaFromCmHimsAndSynergy.addEtaFromCmHimsAndSynergyimage(etaDto, token));
	}

//	/**
//	 * @owner Shubham Shinde,Sachin Sonawane
//	 * @RequestBody ticketDetailsRequest
//	 * @return
//	 */
//	@PostMapping("/")
//	@Loggable
//	public ResponseEntity<TicketAtmDetailsDto> (
//			@RequestBody TicketDetailsReqWithoutReqId ticketDetailsRequest) throws EntityNotFoundException {
//		return ResponseEntity.ok(ticketService.(ticketDetailsRequest));
//
//	}
	
	/**
	 * @owner Shubham Shinde,Sachin Sonawane
	 * @RequestBody ticketDetailsRequest
	 * @return
	 */
	@PostMapping("/getTicketDetailsByNumber")
	@Loggable
	public ResponseEntity<TicketAtmDetailsDto> getTicketDetailsByNumber(
			@RequestBody TicketDetailsReqWithoutReqId ticketDetailsRequest) throws EntityNotFoundException {
		log.info("******* Inside getTicketDetailsByNumber controller *********");
		return ResponseEntity.ok(ticketService.getTicketDetailsByNumberHims(ticketDetailsRequest));

	}

	@PostMapping("/ticketAllocationDetails")
	@Loggable
	public ResponseEntity<IResponseDto> getAllocationEtaDetails(@RequestBody AllocationEtaDetailsRequestDto request) {
		log.info("******* Inside getAllocationEtaDetails controller *********");
		log.info("Request Recieved:- " + request);
		try {
			List<AllocationETADetailsDto> response = service.getAllocationEtaDetails(request);
			log.info("Response Recieved from Service:- " + response);
			return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
		} catch (Exception e) {
			log.info("Exception occured in getAllocationEtaDetails:- " + e);
			return ResponseEntity.ok(restUtils.wrapNullResponse("Error Occured in getAllocationEtaDetails",
					ErrorCode.UNEXPECTED_ERROR.getHttpStatus()));
		}

	}

	@PostMapping("/create")
	@Loggable
	public ResponseEntity<TicketDto> createNewTicket(@RequestBody TicketDto ticketDto) {
		log.info("TicketController:createNewTicket:{}", ticketDto);
		return ResponseEntity.ok(ticketService.createNewTicket(ticketDto));
	}

	@GetMapping("/{status}")
	@Loggable
	public ResponseEntity<List<TicketDto>> getTicketsByStatus(@PathVariable String status) {
		try {
			log.info("getTicketsByStatus:{}" + status);
			return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("/update/status")
	@Loggable
	public ResponseEntity<TicketDto> updateTicketStatus(@RequestBody TicketStatusDto ticketStatusDto) {
		try {

			return ResponseEntity.ok(ticketService.updateTicketStatus(ticketStatusDto));
		} catch (EntityNotFoundException e) {
			log.error("Error while retrieving Value of Ticket ID '{}'", ticketStatusDto.getTicketId(), e);
			return ResponseEntity.notFound().build();
		}
	}

	// FIXME
	@PostMapping("/close")
	@Loggable
	public ResponseEntity<GenericResponseDto> closeTicket(@RequestBody TicketClosureDto ticketClosureDto) {
		log.info("TicketController:closeTicket:{}", ticketClosureDto);
		return ResponseEntity.ok(ticketService.closeTicket(ticketClosureDto));

	}

	@GetMapping("/getAll")
	@Loggable
	public ResponseEntity<List<TicketDto>> getAllTickets() {
		try {
			List<TicketDto> ticketDto = ticketService.getAllTickets();
			return ResponseEntity.ok(ticketDto);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/opentickets/{max}")
	@Loggable
	public ResponseEntity<List<AtmShortDetailsDto>> getOpenTicketList(@RequestBody List<AtmDetailsDto> atmids,
			@PathVariable int max) {
		return ResponseEntity.ok(taskService.getTicketList(atmids, max));
	}

	@GetMapping("/reason")
	@Loggable
	public ResponseEntity<List<TicketReasonDto>> getTicketReasonList() {
		return ResponseEntity.ok(ticketService.getTicketReasonList());
	}

	@PostMapping("/travel/create")
	@Loggable
	public ResponseEntity<GenericResponseDto> createTravelPlan(@RequestBody TravelDto travelDto) {

		return ResponseEntity.ok(travelService.createTravelPlan(travelDto));

	}

	@GetMapping("/travel/status")
	@Loggable
	public ResponseEntity<IsTravellingDto> getIsTravellingStatus() {
		return ResponseEntity.ok(travelService.getIsTravellingStatus());
	}

	@GetMapping("/travel/message")
	@Loggable
	public ResponseEntity<TravelModeMessageDto> getTravellingModeMessage() {
		return ResponseEntity.ok(travelService.getTravellingModeMessage());
	}

	@GetMapping("/travel/travel-mode")
	@Loggable
	public ResponseEntity<List<TravelModeDto>> getTravelModes() {
		return ResponseEntity.ok(travelModeService.getTravelModes());
	}

	@GetMapping("/travel/work-mode")
	@Loggable
	public ResponseEntity<List<WorkModeDto>> getWorkModes() {
		return ResponseEntity.ok(workModeService.getWorkModes());
	}

	@PostMapping("/travel/add-travel-mode")
	@Loggable
	public ResponseEntity<TravelModeDto> addTravelMode(@RequestBody TravelModeDto travelModeDto) {
		return ResponseEntity.ok(travelModeService.addTravelMode(travelModeDto));
	}

	@PostMapping("/travel/add-work-mode")
	@Loggable
	public ResponseEntity<WorkModeDto> addTravelMode(@RequestBody WorkModeDto workModeDto) {
		return ResponseEntity.ok(workModeService.addWorkMode(workModeDto));
	}

	// _CM Portal__________________________________________

	@GetMapping("/counts")
	@Loggable
	public ResponseEntity<TicketsRaisedCountresponseDto> getTicketsRaisedCount() {
		log.info("Ticket COntroller Executed");
		TicketsRaisedCountresponseDto ticketCounts = ticketService.getTicketsRaisedCount();
		return ResponseEntity.ok(ticketCounts);
	}

	@PostMapping("/getRemarkHistory")
	@Loggable
	public ResponseEntity<List<RemarksDto>> getRemarksHistory(@RequestBody RemarksrequestDto requestsdto) {

		//return ResponseEntity.ok(etaService.getMergedRemarksHistory(requestsdto));
		return ResponseEntity.ok(remarkhistoryService.getMergedRemarksHistory(requestsdto));
	}
	
	
	/**
	 * @owner Shubham Shinde
	 * @RequestBody RemarksrequestDto (for synergy or hims )
	 * @return RemarksDto
	 */
	
	@PostMapping("/getRemarkHistory-hims-Or-synergy")
	@Loggable
	public ResponseEntity<List<RemarksDto>> getRemarksHistoryForHims(@RequestBody RemarksrequestDto requestsdto) {
		
		return ResponseEntity.ok(remarkhistoryService.getMergedRemarksHistory(requestsdto));
		
	}

	@PostMapping("/getRemarkHistory-cm")
	@Loggable
	public ResponseEntity<List<RemarkHistoryResponseDto>> getRemarksHistoryForCm_old(
			@RequestBody RemarksrequestDto requestsdto) throws ParseException {

		//return ResponseEntity.ok(etaService.getMergedRemarksHistoryForCm(requestsdto));
		return ResponseEntity.ok(remarkhistoryService.getRemarksHistoryFromHimsPortal(requestsdto));

	}

	@PostMapping("/getcreation_details")
	@Loggable
	public ResponseEntity<CreationDetailsDTO> getcreationdetails(@RequestBody TicketDetailsReqWithoutReqId request) {
		log.info("*** Inside getcreationdetails Controller ****");
		CreationDetailsDTO response = creationDetailsService.getCreationdetails(request);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/get_trans_target")
	@Loggable
	public ResponseEntity<List<TransTargetdetailsDto>> gettranstargetdetails(
			@RequestBody TransactionDetailsRequest requestsdto) {
		log.info("***** Inside gettranstargetdetails Controller *****");
		ACTransactionDetailsRequest request = new ACTransactionDetailsRequest();

		// Format current date as yyyy-MM-dd
		LocalDate currentDate = LocalDate.now();
		String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// Set the formatted date in the request object

		request.setUserId(requestsdto.getUserId());
		request.setDate(formattedDate);

		log.info("Updated Request with Current Date: " + request);
		List<TransTargetdetailsDto> response = transactionDetailsService.gettranstargetdetails(request);
		return ResponseEntity.ok(response);

	}

	/**
	 * @owner :- Shubham Shinde
	 * @filter:- get-Remark-History-cm
	 * @return:-Success
	 */

	@PostMapping("/get-Remark-History-cm")
	@Loggable
	public ResponseEntity<List<RemarkHistoryResponseDto>> getRemarksHistoryForCm(
			@RequestBody RemarksrequestDto requestsdto) {
		return ResponseEntity.ok(etaService.getMergedRemarksHistoryForCm_newResponse(requestsdto));
	}

	@GetMapping("/manualTicketDetails")
	@Loggable
	public ResponseEntity<IResponseDto> getGroupedManuallyTickets() {
		log.info("***** Inside getGroupedManuallyTickets");
		List<GroupedTicketResponse> response = manuallyTicketService.getGroupedManuallyTickets();
		String username = loginService.getLoggedInUser();
		List<ManuallyTickets> tickets = repository.getManuallyTickets(username);
		int total_records = tickets.size();
		GroupTicketListResponse responsedata = new GroupTicketListResponse();
		responsedata.setTotal_records(total_records);
		responsedata.setTicket_data(response);

		return ResponseEntity.ok(restUtils.wrapResponse(responsedata, "OK"));

	}

	// ----------------All filtering of Ce Task summary----------------------

	@GetMapping("/filters")
	@Loggable
	public ResponseEntity<TaskSummaryOfCeResponseFilterDTO> getFilters() {
		return ResponseEntity.ok(taskSummaryFilterOfCeService.getFilters());
	}

	@GetMapping("/getAssignAtmFilters")
	@Loggable
	public ResponseEntity<AssignedAtmforCeFilterResponseDto> getAssignAtmFilters() {
		return ResponseEntity.ok(assignedAtmforCeFilterService.getAssignAtmFilters());
	}

	/**
	 * @owner :- Shubham Shinde
	 * @filter:- ATM History filter API for CE
	 * @return:-Success
	 */

	@GetMapping("/getAssignAtmFiltersFor_CE")
	@Loggable
	public ResponseEntity<AssignedAtmFilterResponseDto> getAssignAtmFiltersCe() {
		return ResponseEntity.ok(assignedAtmforCeFilterService.getAssignAtmFiltersforCe());
	}

	@GetMapping("/Owner_atmTicketHistoryFilter")
	@Loggable
	public ResponseEntity<List<BroadCategoryForAtmHistoryFilterDto>> getAtmTicketHistoryFilter() {
		List<BroadCategoryForAtmHistoryFilterDto> broadCategories = atmTicketHistoryFilter.getBroadCategories();
		return new ResponseEntity<>(broadCategories, HttpStatus.OK);
	}

	@PostMapping("/sub-category_atmTicketHistoryFilter")
	@Loggable
	public ResponseEntity<List<SubcallTypeForAtmTicketHistoryResponseDto>> getOwnerList(
			@RequestBody SubcallTypeForAtmTicketHistoryRequestDto request) {
		List<SubcallTypeForAtmTicketHistoryResponseDto> subcategoty = atmTicketHistoryFilter.getOwnerList(request);
		return ResponseEntity.ok(subcategoty);
	}

	/**
	 * @owner :- Shubham Shinde
	 * @filter:- Assign ATM filter for CE APP
	 * @return:-Success
	 */

	@PostMapping("/update-work-mode")
	@Loggable
	public ResponseEntity<IResponseDto> updateWorkMode(@RequestBody UpdateWorkModeReq request) {
		ResponseMessage response = travelService.updateWorkMode(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Work Mode Updated Successfully"));
	}

	@PostMapping("/api/flagged-tickets")
	@Loggable
	public ResponseEntity<IResponseDto> getFlaggedTickets(@RequestBody FlaggedTicketsRequest request) {
		log.info("*** Inside FlagTicketHistoryController ***");
		log.info("Request received: userId={}, userType={}", request.getUserId(), request.getUserType());

		List<FlaggedTicketResponse> response = flagservice.getFlaggedTickets(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
	}

	@PostMapping("/check-closure")
	@Loggable
	public ResponseEntity<IResponseDto> checkTicketClosure(@RequestBody TicketClosureRequestDto request) {

		return ResponseEntity.ok(
				restUtils.wrapResponse(ticketService.checkTicketClosure(request.getTicketNumber(), request.getAtmId()),
						"check ticket closure api"));
	}

	// Endpoint to get ticket details by ATM ID
	@PostMapping("/getatm_photo_details")
	@Loggable
	public ResponseEntity<IResponseDto> getTicketDetailsByAtmId(@RequestBody Documentrequest request) {
		log.info("*** Inside getTicketDetailsByAtmId ***");
		List<TicketDetailsDto> response = docticketService.getTicketDetailsByAtmId(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));

	}

	@PostMapping("/getlist_ticketimage")
	@Loggable
	public ResponseEntity<IResponseDto> getTicketDetailsimageListByAtmId(@RequestBody Documentrequest request) {
		log.info("*** Inside getTicketDetailsimageListByAtmId ***");
		List<TicketWithDocumentsDTO> response = documentsService.fetchTicketsWithDocuments(request.getAtm_id(),
				request.getStatus(), request.getTicketType(), request.getDateStr(), request.getStartDate(),
				request.getEndDate());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));

	}

//	@PostMapping("/getimageFilter")
//	@Loggable
//	public ResponseEntity<IResponseDto> getimageFilter(@RequestBody Documentrequest request) {
//		log.info("*** Inside getTicketDetailsimageListByAtmId ***");
//		List<TicketWithDocumentsDTO> response = documentsService.fetchTicketsWithDocuments(request.getAtm_id(),
//				request.getStatus(), request.getIsDown(), request.getEtaExpired(), request.getStartDate(), request.getEndDate());
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//
//	}

	@PostMapping("/getlistatm_photo_details")
	@Loggable
	public ResponseEntity<IResponseDto> getTicketDetailsListByAtmId(@RequestBody GetTicketDocumentsRequest request) {
		log.info("*** Inside getTicketDetailsListByAtmId ***");
		log.info("Request received in controller: {}", request);	
		List<GetTicketDocumentsResponse> response = docticketService.getTicketDetailsListByAtmId(request.getAtm_id(),
				request.getStatus(), request.getTicketType(),  request.getDateStr(),
				request.getStartDate(), request.getEndDate(), request.getTicketNumber(),
				request.getLastModifiedDateStr());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));

	}

	// Endpoint to get Dropdown API for remarks Updated On, after clicking on any
	// ticket
	@PostMapping("/get_formatted_eta_dates")
	@Loggable
	public ResponseEntity<IResponseDto> getFormattedETADatesList(@RequestBody AllFormattedETADatesReqDTO request) {
		List<AllFormattedETADatesResponseDTO> response = allFormattedETADatesService.getAllFormattedETADates(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));

	}

	@PostMapping("/getAllocationDetails")
	@Loggable
	public ResponseEntity<IResponseDto> getAllocationDetails(@RequestBody AllocationDetailsRequestDto request) {
		log.info("******* Inside getAllocationDetails controller *********");
		log.info("Request Recieved:- " + request);
		try {
			List<AllocationDetailsDto> response = allocationDetailsService.getAllocationDetails(request);
			log.info("Response Recieved from Service:- " + response);
			return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
		} catch (Exception e) {
			log.info("Exception occured in getAllocationDetails:- " + e);
			return ResponseEntity.ok(restUtils.wrapNullResponse("Error Occured in getAllocationDetails",
					ErrorCode.UNEXPECTED_ERROR.getHttpStatus()));
		}

	}

	@PostMapping("/image/filters")
	public ResponseEntity<IResponseDto> getTicketFilters(@RequestBody ImageFilterDocument request) {
		String atmId=request.getAtmId();
		if (atmId == null || atmId.trim().isEmpty()) {
			return ResponseEntity.ok(restUtils.wrapNullResponse(null, HttpStatus.BAD_REQUEST));
		}

		FilterDTO filters = ticketFilterService.getTicketFilters(atmId);

		return ResponseEntity.ok(restUtils.wrapResponse(filters, "Filter Data Fetched Successfully"));
	}

}
