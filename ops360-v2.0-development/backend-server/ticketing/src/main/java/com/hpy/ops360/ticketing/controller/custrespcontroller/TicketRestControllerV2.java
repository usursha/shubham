//package com.hpy.ops360.ticketing.controller.custrespcontroller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.hpy.generic.Exception.EntityNotFoundException;
//import com.hpy.ops360.ticketing.cm.dto.AllocationETADetailsDto;
//import com.hpy.ops360.ticketing.cm.dto.AllocationEtaDetailsRequestDto;
//import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
//import com.hpy.ops360.ticketing.cm.dto.Documentrequest;
//import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketResponse;
//import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketsRequest;
//import com.hpy.ops360.ticketing.cm.dto.TicketDetailsDto;
//import com.hpy.ops360.ticketing.cm.dto.TicketsRaisedCountresponseDto;
//import com.hpy.ops360.ticketing.cm.service.AllocationEtaDetailsService;
//import com.hpy.ops360.ticketing.cm.service.CMTicketService;
//import com.hpy.ops360.ticketing.cm.service.FlagTicketHistoryService;
//import com.hpy.ops360.ticketing.dto.AssignedAtmFilterResponseDto;
//import com.hpy.ops360.ticketing.dto.AssignedAtmforCeFilterResponseDto;
//import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
//import com.hpy.ops360.ticketing.dto.BroadCategoryForAtmHistoryFilterDto;
//import com.hpy.ops360.ticketing.dto.EtaDto2;
//import com.hpy.ops360.ticketing.dto.EtaRequestDtoForCm;
//import com.hpy.ops360.ticketing.dto.GenericResponseDto;
//import com.hpy.ops360.ticketing.dto.GroupTicketListResponse;
//import com.hpy.ops360.ticketing.dto.GroupedTicketResponse;
//import com.hpy.ops360.ticketing.dto.IsTravellingDto;
//import com.hpy.ops360.ticketing.dto.RemarksDto;
//import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
//import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryRequestDto;
//import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryResponseDto;
//import com.hpy.ops360.ticketing.dto.TaskDto;
//import com.hpy.ops360.ticketing.dto.TaskSummaryOfCeResponseFilterDTO;
//import com.hpy.ops360.ticketing.dto.TicketAtmDetailsDto;
//import com.hpy.ops360.ticketing.dto.TicketClosureDto;
//import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
//import com.hpy.ops360.ticketing.dto.TicketDto;
//import com.hpy.ops360.ticketing.dto.TicketReasonDto;
//import com.hpy.ops360.ticketing.dto.TicketStatusDto;
//import com.hpy.ops360.ticketing.dto.TravelDto;
//import com.hpy.ops360.ticketing.dto.TravelModeDto;
//import com.hpy.ops360.ticketing.dto.TravelModeMessageDto;
//import com.hpy.ops360.ticketing.dto.WorkModeDto;
//import com.hpy.ops360.ticketing.entity.ManuallyTickets;
//import com.hpy.ops360.ticketing.login.service.LoginService;
//import com.hpy.ops360.ticketing.repository.ManuallyTicketRepository;
//import com.hpy.ops360.ticketing.request.UpdateWorkModeReq;
//import com.hpy.ops360.ticketing.response.ResponseMessage;
//import com.hpy.ops360.ticketing.service.AssignedAtmforCeFilterService;
//import com.hpy.ops360.ticketing.service.AtmTicketHistoryFilter;
//import com.hpy.ops360.ticketing.service.EtaService;
//import com.hpy.ops360.ticketing.service.ManuallyTicketService;
//import com.hpy.ops360.ticketing.service.TaskService;
//import com.hpy.ops360.ticketing.service.TaskSummaryFilterOfCeService;
//import com.hpy.ops360.ticketing.service.TicketService;
//import com.hpy.ops360.ticketing.service.TravelModeService;
//import com.hpy.ops360.ticketing.service.TravelService;
//import com.hpy.ops360.ticketing.service.WorkModeService;
//import com.hpy.rest.dto.IResponseDto;
//import com.hpy.rest.exception.ErrorCode;
//import com.hpy.rest.util.RestUtils;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RestController
//@RequestMapping("/v2/ticket")
//@AllArgsConstructor
//@CrossOrigin("${app.cross-origin.allow}")
//public class TicketRestControllerV2 {
//
//	@Autowired
//	private CMTicketService docticketService;
//
//	@Autowired
//	private final FlagTicketHistoryService flagservice;
//
//	private final TicketService ticketService;
//
//	private final TravelService travelService;
//
//	private final TravelModeService travelModeService;
//
//	private final WorkModeService workModeService;
//
//	private final TaskService taskService;
//
//	private final EtaService etaService;
//
//	@Autowired
//	private AllocationEtaDetailsService service;
//
//	@Autowired
//	private final RestUtils restUtils;
//
//	@Autowired
//	private ManuallyTicketRepository repository;
//
//	@Autowired
//	private LoginService loginService;
//
//	@Autowired
//	private TaskSummaryFilterOfCeService taskSummaryFilterOfCeService;
//
//	@Autowired
//	private ManuallyTicketService manuallyTicketService;
//
//	@Autowired
//	private AssignedAtmforCeFilterService assignedAtmforCeFilterService;
//
//	@Autowired
//	private AtmTicketHistoryFilter atmTicketHistoryFilter;
//
//	@PostMapping("/createTicket")
//	public ResponseEntity<IResponseDto> createTicket(@RequestBody TaskDto ticket,
//			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//		log.info("*** Inside createTicket controller ***");
//		log.info("TicketController:createTicket:{}", ticket);
//		GenericResponseDto response = taskService.createTask(ticket,token);
//		log.info("Response Given from createTicket:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket Created Successfully"));
//	}
//
//	@PostMapping("/addCommentWithEta")
//	public ResponseEntity<IResponseDto> addEta(@RequestBody EtaDto2 etaDto,
//			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//		log.info("*** Inside addEta controller ***");
//		log.info("TicketController:addEta:{}", etaDto);
//		GenericResponseDto response = etaService.addEta(etaDto, token);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "ETA Added Successfully"));
//	}
//
//	@PostMapping("/addCommentWithEtaByCm")
//	public ResponseEntity<IResponseDto> addEtaByCm(@RequestBody EtaRequestDtoForCm etaDto,
//			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//		log.info("*** Inside addEtaByCm controller ***");
//		log.info("TicketController:addEtaByCm:{}", etaDto);
//		GenericResponseDto response = etaService.addEtaByCm(etaDto, token);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "ETA By CM Added Successfully"));
//	}
//
//	/**
//	 * @owner Shubham Shinde,Sachin Sonawane
//	 * @RequestBody ticketDetailsRequest
//	 * @return
//	 */
//	@PostMapping("/")
//	public ResponseEntity<IResponseDto> (
//			@RequestBody TicketDetailsReqWithoutReqId ticketDetailsRequest) throws EntityNotFoundException {
//		log.info("*** Inside  controller ***");
//		log.info("TicketController::{}", ticketDetailsRequest);
//		TicketAtmDetailsDto response = ticketService.(ticketDetailsRequest);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket Deatails Fetched Successfully"));
//
//	}
//
//	@PostMapping("/ticketAllocationDetails")
//	public ResponseEntity<IResponseDto> getAllocationEtaDetails(@RequestBody AllocationEtaDetailsRequestDto request) {
//		log.info("******* Inside getAllocationEtaDetails controller *********");
//		log.info("Request Recieved:- " + request);
//		try {
//			List<AllocationETADetailsDto> response = service.getAllocationEtaDetails(request);
//			log.info("Response Recieved from Service:- " + response);
//			return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//		} catch (Exception e) {
//			log.info("Exception occured in getAllocationEtaDetails:- " + e);
//			return ResponseEntity.ok(restUtils.wrapNullResponse("Error Occured in getAllocationEtaDetails",
//					ErrorCode.UNEXPECTED_ERROR.getHttpStatus()));
//		}
//
//	}
//
//	@PostMapping("/create")
//	public ResponseEntity<IResponseDto> createNewTicket(@RequestBody TicketDto ticketDto) {
//
//		log.info("******* Inside createNewTicket controller *********");
//		log.info("Request Recieved:- " + ticketDto);
//		TicketDto response = ticketService.createNewTicket(ticketDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket Created Successfully"));
//	}
//
//	@GetMapping("/{status}")
//	public ResponseEntity<IResponseDto> getTicketsByStatus(@PathVariable String status) {
//		try {
//			log.info("******* Inside getTicketsByStatus controller *********");
//			log.info("getTicketsByStatus:{}" + status);
//			List<TicketDto> response = ticketService.getTicketsByStatus(status);
//			return ResponseEntity.ok(restUtils.wrapResponse(response, "Status fetched Successfully"));
//		} catch (Exception e) {
//			return ResponseEntity.notFound().build();
//		}
//
//	}
//
//	@PutMapping("/update/status")
//	public ResponseEntity<IResponseDto> updateTicketStatus(@RequestBody TicketStatusDto ticketStatusDto) {
//		try {
//			log.info("******* Inside updateTicketStatus controller *********");
//			log.info("Request Recieved:- " + ticketStatusDto);
//
//			TicketDto response = ticketService.updateTicketStatus(ticketStatusDto);
//			return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket Successfully Updated"));
//		} catch (EntityNotFoundException e) {
//			log.error("Error while retrieving Value of Ticket ID '{}'", ticketStatusDto.getTicketId(), e);
//			return ResponseEntity.notFound().build();
//		}
//	}
//
//	// FIXME
//	@PostMapping("/close")
//	public ResponseEntity<IResponseDto> closeTicket(@RequestBody TicketClosureDto ticketClosureDto) {
//		log.info("******* Inside closeTicket controller *********");
//		log.info("Request Recieved:- " + ticketClosureDto);
//		GenericResponseDto response = ticketService.closeTicket(ticketClosureDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket Closed Successfully"));
//
//	}
//
//	@GetMapping("/getAll")
//	public ResponseEntity<IResponseDto> getAllTickets() {
//		try {
//			log.info("******* Inside getAllTickets controller *********");
//			List<TicketDto> ticketDto = ticketService.getAllTickets();
//			return ResponseEntity.ok(restUtils.wrapResponse(ticketDto, "All Tickets Fetched Successfully"));
//		} catch (Exception e) {
//			return ResponseEntity.notFound().build();
//		}
//	}
//
//	@PostMapping("/opentickets/{max}")
//	public ResponseEntity<IResponseDto> getOpenTicketList(@RequestBody List<AtmDetailsDto> atmids,
//			@PathVariable int max) {
//		log.info("******* Inside getOpenTicketList controller *********");
//		log.info("Request Recieved:- " + atmids);
//		List<AtmShortDetailsDto> response = taskService.getTicketList(atmids, max);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Fetched Open Tickets Successfully"));
//	}
//
//	@GetMapping("/reason")
//	public ResponseEntity<IResponseDto> getTicketReasonList() {
//		log.info("******* Inside getOpenTicketList controller *********");
//		List<TicketReasonDto> response = ticketService.getTicketReasonList();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Get getTicketReasonList Successfully"));
//	}
//
//	@PostMapping("/travel/create")
//	public ResponseEntity<IResponseDto> createTravelPlan(@RequestBody TravelDto travelDto) {
//
//		log.info("******* Inside createTravelPlan controller *********");
//		log.info("Request Recieved:- " + travelDto);
//		GenericResponseDto response = travelService.createTravelPlan(travelDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Plan Created Successfully"));
//
//	}
//
//	@GetMapping("/travel/status")
//	public ResponseEntity<IResponseDto> getIsTravellingStatus() {
//		log.info("******* Inside getIsTravellingStatus controller *********");
//		IsTravellingDto response = travelService.getIsTravellingStatus();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Status Fetched Successfully"));
//	}
//
//	@GetMapping("/travel/message")
//	public ResponseEntity<IResponseDto> getTravellingModeMessage() {
//		log.info("******* Inside getIsTravellingStatus controller *********");
//
//		TravelModeMessageDto response = travelService.getTravellingModeMessage();
//		log.info("Response Recieved From travelService:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Mode Message Fetched Successfully"));
//	}
//
//	@GetMapping("/travel/travel-mode")
//	public ResponseEntity<IResponseDto> getTravelModes() {
//		log.info("******* Inside getIsTravellingStatus controller *********");
//		List<TravelModeDto> response = travelModeService.getTravelModes();
//		log.info("Response Recieved From travelModeService:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travek Modes Fetched Successfully"));
//	}
//
//	@GetMapping("/travel/work-mode")
//	public ResponseEntity<IResponseDto> getWorkModes() {
//		log.info("******* Inside getWorkModes controller *********");
//		List<WorkModeDto> response = workModeService.getWorkModes();
//		log.info("Response Recieved From workModeService:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Work Mode Fetched Successfully"));
//	}
//
//	@PostMapping("/travel/add-travel-mode")
//	public ResponseEntity<IResponseDto> addTravelMode(@RequestBody TravelModeDto travelModeDto) {
//		log.info("******* Inside addTravelMode controller *********");
//		TravelModeDto response = travelModeService.addTravelMode(travelModeDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Mode added Successfully"));
//	}
//
//	@PostMapping("/travel/add-work-mode")
//	public ResponseEntity<IResponseDto> addWorkMode(@RequestBody WorkModeDto workModeDto) {
//		log.info("******* Inside addWorkMode controller *********");
//		WorkModeDto response = workModeService.addWorkMode(workModeDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Work Mode Added Successfully"));
//	}
//
//	// _CM Portal__________________________________________
//
//	@GetMapping("/counts")
//	public ResponseEntity<IResponseDto> getTicketsRaisedCount() {
//		log.info("*** Ticket COntroller Executed ***");
//		TicketsRaisedCountresponseDto ticketCounts = ticketService.getTicketsRaisedCount();
//		log.info("Response Recieved From getTicketsRaisedCount:- " + ticketCounts);
//		return ResponseEntity.ok(restUtils.wrapResponse(ticketCounts, "Raise Count Ticket Fetched Successfully"));
//	}
//
//	@PostMapping("/getRemarkHistory")
//	public ResponseEntity<IResponseDto> getRemarksHistory(@RequestBody RemarksrequestDto requestsdto) {
//		log.info("*** Inside getRemarksHistory Controller Executed ***");
//		List<RemarksDto> response = etaService.getMergedRemarksHistory(requestsdto);
//		log.info("Response Recieved From getMergedRemarksHistory:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Remarks History Fetched Successfully"));
//
//	}
//
//	@GetMapping("/manualTicketDetails")
//	public ResponseEntity<IResponseDto> getGroupedManuallyTickets() {
//		log.info("***** Inside getGroupedManuallyTickets *****");
//		List<GroupedTicketResponse> response = manuallyTicketService.getGroupedManuallyTickets();
//		String username = loginService.getLoggedInUser();
//		List<ManuallyTickets> tickets = repository.getManuallyTickets(username);
//		int total_records = tickets.size();
//		GroupTicketListResponse responsedata = new GroupTicketListResponse();
//		responsedata.setTotal_records(total_records);
//		responsedata.setTicket_data(response);
//
//		return ResponseEntity.ok(restUtils.wrapResponse(responsedata, "OK"));
//
//	}
//
//	// ----------------All filtering of Ce Task summary----------------------
//
//	@GetMapping("/filters")
//	public ResponseEntity<IResponseDto> getFilters() {
//		log.info("*** Inside getFilters Controller Executed ***");
//		TaskSummaryOfCeResponseFilterDTO response = taskSummaryFilterOfCeService.getFilters();
//		log.info("Response Recieved From getFilters:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Filter data Fetched Successfully"));
//	}
//
//	@GetMapping("/getAssignAtmFilters")
//	public ResponseEntity<IResponseDto> getAssignAtmFilters() {
//		log.info("*** Inside getAssignAtmFilters Controller Executed ***");
//		AssignedAtmforCeFilterResponseDto response = assignedAtmforCeFilterService.getAssignAtmFilters();
//		log.info("Response Recieved From getFilters:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Assigned ATM filters successfully"));
//	}
//
//	/**
//	 * @owner :- Shubham Shinde
//	 * @filter:- ATM History filter API for CE
//	 * @return:-Success
//	 */
//
//	@GetMapping("/getAssignAtmFiltersFor_CE")
//	public ResponseEntity<IResponseDto> getAssignAtmFiltersCe() {
//		log.info("*** Inside getAssignAtmFiltersCe Controller Executed ***");
//		AssignedAtmFilterResponseDto response = assignedAtmforCeFilterService.getAssignAtmFiltersforCe();
//		log.info("Response Recieved From getAssignAtmFiltersCe:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Assigned ATM filters for ce fetched successfully"));
//	}
//
//	@GetMapping("/Owner_atmTicketHistoryFilter")
//	public ResponseEntity<IResponseDto> getAtmTicketHistoryFilter() {
//		log.info("*** Inside getAtmTicketHistoryFilter Controller Executed ***");
//		List<BroadCategoryForAtmHistoryFilterDto> broadCategories = atmTicketHistoryFilter.getBroadCategories();
//		log.info("Response Recieved From atmTicketHistoryFilter:- " + broadCategories);
//
//		return ResponseEntity.ok(restUtils.wrapResponse(broadCategories, "broadCategories fetched successfully"));
//	}
//
//	@PostMapping("/sub-category_atmTicketHistoryFilter")
//	public ResponseEntity<IResponseDto> getOwnerList(@RequestBody SubcallTypeForAtmTicketHistoryRequestDto request) {
//
//		log.info("*** Inside getAtmTicketHistoryFilter Controller Executed ***");
//		List<SubcallTypeForAtmTicketHistoryResponseDto> subcategoty = atmTicketHistoryFilter.getOwnerList(request);
//		log.info("Response Recieved From atmTicketHistoryFilter:- " + subcategoty);
//
//		return ResponseEntity.ok(restUtils.wrapResponse(subcategoty, "subcategoty fetched successfully"));
//	}
//
//	/**
//	 * @owner :- Shubham Shinde
//	 * @filter:- Assign ATM filter for CE APP
//	 * @return:-Success
//	 */
//
//	@PostMapping("/update-work-mode")
//	public ResponseEntity<IResponseDto> updateWorkMode(@RequestBody UpdateWorkModeReq request) {
//		log.info("*** Inside getAtmTicketHistoryFilter Controller Executed ***");
//		ResponseMessage response = travelService.updateWorkMode(request);
//		log.info("Response Recieved From atmTicketHistoryFilter:- " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "subcategoty fetched successfully"));
//	}
//
//	@PostMapping("/api/flagged-tickets")
//	public ResponseEntity<IResponseDto> getFlaggedTickets(@RequestBody FlaggedTicketsRequest request) {
//		log.info("*** Inside FlagTicketHistoryController ***");
//		log.info("Request received: userId={}, userType={}", request.getUserId(), request.getUserType());
//
//		List<FlaggedTicketResponse> response = flagservice.getFlaggedTickets(request);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//	}
//
//	// Endpoint to get ticket details by ATM ID
//	@PostMapping("/getTicketDetailsByAtmId")
//	public ResponseEntity<IResponseDto> getTicketDetailsByAtmId(@RequestBody Documentrequest request) {
//		log.info("*** Inside getTicketDetailsByAtmId ***");
//		List<TicketDetailsDto> response = docticketService.getTicketDetailsByAtmId(request);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//	}
//}
