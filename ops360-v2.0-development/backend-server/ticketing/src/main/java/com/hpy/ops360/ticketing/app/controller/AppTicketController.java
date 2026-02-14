package com.hpy.ops360.ticketing.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.generic.Exception.EntityNotFoundException;
import com.hpy.ops360.ticketing.dto.AssignedAtmFilterResponseDto;
import com.hpy.ops360.ticketing.dto.AssignedAtmforCeFilterResponseDto;
import com.hpy.ops360.ticketing.dto.BroadCategoryForAtmHistoryFilterDto;
import com.hpy.ops360.ticketing.dto.EtaDto2;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.GroupTicketListResponse;
import com.hpy.ops360.ticketing.dto.GroupedTicketResponse;
import com.hpy.ops360.ticketing.dto.IsTravellingDto;
import com.hpy.ops360.ticketing.dto.RemarksDto;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryRequestDto;
import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryResponseDto;
import com.hpy.ops360.ticketing.dto.TaskDto;
import com.hpy.ops360.ticketing.dto.TaskSummaryOfCeResponseFilterDTO;
import com.hpy.ops360.ticketing.dto.TicketAtmDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketReasonDto;
import com.hpy.ops360.ticketing.dto.TravelDto;
import com.hpy.ops360.ticketing.dto.TravelModeMessageDto;
import com.hpy.ops360.ticketing.entity.ManuallyTickets;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.ManuallyTicketRepository;
import com.hpy.ops360.ticketing.request.TicketClosureRequestDto;
import com.hpy.ops360.ticketing.request.UpdateWorkModeReq;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.service.AssignedAtmforCeFilterService;
import com.hpy.ops360.ticketing.service.AtmTicketHistoryFilter;
import com.hpy.ops360.ticketing.service.EtaService;
import com.hpy.ops360.ticketing.service.ManuallyTicketService;
import com.hpy.ops360.ticketing.service.TaskService;
import com.hpy.ops360.ticketing.service.TaskSummaryFilterOfCeService;
import com.hpy.ops360.ticketing.service.TicketService;
import com.hpy.ops360.ticketing.service.TravelService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/app/ticket")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class AppTicketController {

	@Autowired
	private TravelService travelService;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private TaskSummaryFilterOfCeService taskSummaryFilterOfCeService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private AssignedAtmforCeFilterService assignedAtmforCeFilterService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ManuallyTicketRepository repository;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ManuallyTicketService manuallyTicketService;
	
	@Autowired
	private EtaService etaService;
	
	@Autowired
	private AtmTicketHistoryFilter atmTicketHistoryFilter;

	@GetMapping("/travel/message")
	public ResponseEntity<IResponseDto> getTravellingModeMessage() {
		log.info("******* Inside getIsTravellingStatus controller *********");

		TravelModeMessageDto response = travelService.getTravellingModeMessage();
		log.info("Response Recieved From travelService:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Mode Message Fetched Successfully"));
	}

	@PostMapping("/update-work-mode")
	public ResponseEntity<IResponseDto> updateWorkMode(@RequestBody UpdateWorkModeReq request) {
		log.info("*** Inside getAtmTicketHistoryFilter Controller Executed ***");
		ResponseMessage response = travelService.updateWorkMode(request);
		log.info("Response Recieved From atmTicketHistoryFilter:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "subcategoty fetched successfully"));
	}

	@GetMapping("/filters")
	public ResponseEntity<IResponseDto> getFilters() {
		log.info("*** Inside getFilters Controller Executed ***");
		TaskSummaryOfCeResponseFilterDTO response = taskSummaryFilterOfCeService.getFilters();
		log.info("Response Recieved From getFilters:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Filter data Fetched Successfully"));
	}

	@PostMapping("/check-closure")
	@Loggable
	public ResponseEntity<IResponseDto> checkTicketClosure(@RequestBody TicketClosureRequestDto request) {
		log.info("*** Inside checkTicketClosure Controller Executed ***");
		ResponseMessage response = ticketService.checkTicketClosure(request.getTicketNumber(), request.getAtmId());
		log.info("Response Recieved From checkTicketClosure:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "check ticket closure api"));
	}

	@GetMapping("/travel/status")
	@Loggable
	public ResponseEntity<IResponseDto> getIsTravellingStatus() {
		log.info("*** Inside getIsTravellingStatus Controller Executed ***");
		IsTravellingDto response = travelService.getIsTravellingStatus();
		log.info("Response Recieved From getIsTravellingStatus:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Status Fetched Succesfully"));
	}

	@PostMapping("/get-ticket-details-by-number")
	@Loggable
	public ResponseEntity<IResponseDto> getTicketDetailsByNumber(
			@RequestBody TicketDetailsReqWithoutReqId ticketDetailsRequest) throws EntityNotFoundException {
		log.info("*** Inside getTicketDetailsByNumber Controller Executed ***");
		TicketAtmDetailsDto response = ticketService.getTicketDetailsByNumber(ticketDetailsRequest);
		log.info("Response Recieved From getTicketDetailsByNumber:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Get Tickets Details fetched successfully"));
	}

	@PostMapping("/travel/create")
	@Loggable
	public ResponseEntity<IResponseDto> createTravelPlan(@RequestBody TravelDto travelDto) {
		log.info("*** Inside createTravelPlan Controller Executed ***");
		GenericResponseDto response = travelService.createTravelPlan(travelDto);
		log.info("Response Recieved From createTravelPlan:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel Plan Created Successfully"));
	}

	@GetMapping("/get-assign-atm-filters-for-ce")
	@Loggable
	public ResponseEntity<IResponseDto> getAssignAtmFiltersCe() {
		log.info("*** Inside getAssignAtmFiltersCe Controller Executed ***");
		AssignedAtmFilterResponseDto response = assignedAtmforCeFilterService.getAssignAtmFiltersforCe();
		log.info("Response Recieved From getAssignAtmFiltersCe:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "getAssignAtmFiltersCe fetched Successfully"));
	}

	@GetMapping("/reason")
	@Loggable
	public ResponseEntity<IResponseDto> getTicketReasonList() {
		log.info("*** Inside getTicketReasonList Controller Executed ***");
		List<TicketReasonDto> response = ticketService.getTicketReasonList();
		log.info("Response Recieved From getTicketReasonList:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Reason List fetched Successfully"));
	}

	@PostMapping("/create-ticket")
	@Loggable
	public ResponseEntity<IResponseDto> createTicket(@RequestBody TaskDto ticket,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		log.info("TicketController:createTicket:{}", ticket);
		GenericResponseDto response = taskService.createTask(ticket, token);
		log.info("Response Recieved From createTicket:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket Created Succesfully"));
	}

	@GetMapping("/manual-ticket-details")
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

		return ResponseEntity.ok(restUtils.wrapResponse(responsedata, "Manual Tickets fetched Successfully"));

	}
	
	@PostMapping("/add-comment-with-eta")
	@Loggable
	public ResponseEntity<IResponseDto> addEta(@RequestBody EtaDto2 etaDto,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		log.info("***** Inside addEta");
		GenericResponseDto response=etaService.addEta(etaDto, token);
		log.info("Response Recieved From addEta:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "ETA Added Successfully"));
	}
	
	@PostMapping("/get-remark-history")
	@Loggable
	public ResponseEntity<IResponseDto> getRemarksHistory(@RequestBody RemarksrequestDto requestsdto) {
		log.info("***** Inside getRemarksHistory");
		List<RemarksDto> response=etaService.getMergedRemarksHistory(requestsdto);
		log.info("Response Recieved From getRemarksHistory:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Remark History fetched Successfully"));

	}
	
	@GetMapping("/owner-atm-ticket-history-filter")
	@Loggable
	public ResponseEntity<IResponseDto> getAtmTicketHistoryFilter() {
		log.info("***** Inside getAtmTicketHistoryFilter");
		List<BroadCategoryForAtmHistoryFilterDto> response = atmTicketHistoryFilter.getBroadCategories();
		log.info("Response Recieved From getAtmTicketHistoryFilter:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "getAtmTicketHistoryFilter fetched Successfully"));
	}
	
	@PostMapping("/sub-category-atm-ticket-history-filter")
	@Loggable
	public ResponseEntity<IResponseDto> getOwnerList(
			@RequestBody SubcallTypeForAtmTicketHistoryRequestDto request) {
		log.info("***** Inside getOwnerList");
		List<SubcallTypeForAtmTicketHistoryResponseDto> response = atmTicketHistoryFilter.getOwnerList(request);
		log.info("Response Recieved From getOwnerList:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Owner List Fetched Successfully"));
	}
	
	
}
