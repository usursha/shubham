package com.hpy.ops360.ticketing.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

import com.google.common.net.HttpHeaders;
import com.hpy.generic.IGenericDto;
import com.hpy.generic.Exception.EntityNotFoundException;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.ManualTicketListWrapper;
import com.hpy.ops360.ticketing.cm.dto.RemarksrequestDtoWrapper;
import com.hpy.ops360.ticketing.cm.dto.TicketsRaisedCountresponseDto;
import com.hpy.ops360.ticketing.dto.AssignedAtmforCeFilterResponseDto;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.AtmShortDetailsListWrapper;
import com.hpy.ops360.ticketing.dto.EtaDto2;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.GetManuallyTicketResponseDTO;
import com.hpy.ops360.ticketing.dto.IsTravellingDto;
import com.hpy.ops360.ticketing.dto.RemarksDto;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.dto.TaskDto;
import com.hpy.ops360.ticketing.dto.TaskSummaryOfCeResponseFilterDTO;
import com.hpy.ops360.ticketing.dto.TicketAtmDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketClosureDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketDto;
import com.hpy.ops360.ticketing.dto.TicketReasonDto;
import com.hpy.ops360.ticketing.dto.TicketReasonDto;
import com.hpy.ops360.ticketing.dto.TicketStatusDto;
import com.hpy.ops360.ticketing.dto.TravelDto;
import com.hpy.ops360.ticketing.dto.TravelModeDto;
import com.hpy.ops360.ticketing.dto.WorkModeDto;
import com.hpy.ops360.ticketing.entity.TravelModeMessage;
import com.hpy.ops360.ticketing.request.UpdateWorkModeReq;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.service.AssignedAtmforCeFilterService;
import com.hpy.ops360.ticketing.service.EtaService;
import com.hpy.ops360.ticketing.service.ManuallyTicketService;
import com.hpy.ops360.ticketing.service.TaskService;
import com.hpy.ops360.ticketing.service.TaskSummaryFilterOfCeService;
import com.hpy.ops360.ticketing.service.TicketService;
import com.hpy.ops360.ticketing.service.TravelModeService;
import com.hpy.ops360.ticketing.service.TravelService;
import com.hpy.ops360.ticketing.service.WorkModeService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseDto;
import com.hpy.rest.util.RestUtils;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ticket2")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class TicketRestController {

	private final TicketService ticketService;

	private final EtaService etaService;

	@Autowired
	private ManuallyTicketService manuallyTicketService;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TravelService travelService;

	@Autowired
	private TravelModeService travelModeService;

	@Autowired
	private WorkModeService workModeService;

	@Autowired
	private TaskSummaryFilterOfCeService taskSummaryFilterOfCeService;

	@Autowired
	private AssignedAtmforCeFilterService assignedAtmforCeFilterServicel;

	@GetMapping("/counts")
	public ResponseEntity<IResponseDto> getTicketsRaisedCount() {
		TicketsRaisedCountresponseDto data = ticketService.getTicketsRaisedCount();
		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/getRemarkHistory")
	public ResponseEntity<IResponseDto> getTicketsRaisedCount(@RequestBody RemarksrequestDto requestsdto) {
		List<RemarksDto> dashboardData = etaService.getMergedRemarksHistory(requestsdto);
		RemarksrequestDtoWrapper datawrapper = new RemarksrequestDtoWrapper();
		datawrapper.setRemarksdtolist(dashboardData);
		IResponseDto response = restUtils.wrapResponse(datawrapper, "Fetched Remarks Data");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/manualTicketDetails")
	public ResponseEntity<IResponseDto> getManuallyTickets() {
		List<GetManuallyTicketResponseDTO> manualticketlist = manuallyTicketService.getManuallyTickets();
		ManualTicketListWrapper setmanualticket = new ManualTicketListWrapper();
		setmanualticket.setManualticketList(manualticketlist);
		IResponseDto response = restUtils.wrapResponse(setmanualticket, "OK");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getTicketDetailsByNumber")
	public ResponseEntity<IResponseDto> getTicketDetailsByNumber(
			@RequestBody TicketDetailsReqWithoutReqId ticketDetailsRequest) throws EntityNotFoundException {
		TicketAtmDetailsDto data = ticketService.getTicketDetailsByNumber(ticketDetailsRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	/**
	 * @owner - Shubham Shinde
	 * @API - CM API changes for custom response
	 * @return- "responseCode": 200, "message": "OK", "data": []
	 */

//	@PostMapping("/createTicket")
//	public ResponseEntity<IResponseDto> createTicket(@RequestBody TaskDto ticket) {
//		log.info("TicketController:createTicket:{}", ticket);
//		GenericResponseDto response = taskService.createTask(ticket);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket created successfully"));
//	}
//
//	@PostMapping("/addCommentWithEta")
//	public ResponseEntity<IResponseDto> addEta(@RequestBody EtaDto2 etaDto,
//			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//		GenericResponseDto response = etaService.addEta(etaDto, token);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "ETA added successfully"));
//	}
//
//	@PostMapping("/create")
//	public ResponseEntity<IResponseDto> createNewTicket(@RequestBody TicketDto ticketDto) {
//		TicketDto response = ticketService.createNewTicket(ticketDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "New ticket created successfully"));
//	}
//
//	@GetMapping("/{status}")
//	public ResponseEntity<IResponseDto> getTicketsByStatus(@PathVariable String status) {
//		try {
//			log.info("getTicketsByStatus:{}", status);
//			List<TicketDto> response = ticketService.getTicketsByStatus(status);
//			return ResponseEntity.ok(restUtils.wrapResponse(response, "Tickets fetched successfully"));
//		} catch (Exception e) {
//			return ResponseEntity.notFound().build();
//		}
//	}
//
//	@PutMapping("/update/status")
//	public ResponseEntity<IResponseDto> updateTicketStatus(@RequestBody TicketStatusDto ticketStatusDto) {
//		try {
//			TicketDto response = ticketService.updateTicketStatus(ticketStatusDto);
//			return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket status updated successfully"));
//		} catch (EntityNotFoundException e) {
//			log.error("Error while retrieving Value of Ticket ID '{}'", ticketStatusDto.getTicketId(), e);
//			return ResponseEntity.notFound().build();
//		}
//	}
//
//	/*
//	 * @PostMapping("/close") public ResponseEntity<IResponseDto>
//	 * closeTicket(@RequestBody TicketClosureDto ticketClosureDto) {
//	 * GenericResponseDto response = ticketService.closeTicket(ticketClosureDto);
//	 * return ResponseEntity.ok(restUtils.wrapResponse(response,
//	 * "Ticket closed successfully")); }
//	 */
//
//	@GetMapping("/getAll")
//	public ResponseEntity<IResponseDto> getAllTickets() {
//		try {
//			List<TicketDto> response = ticketService.getAllTickets();
//			return ResponseEntity.ok(restUtils.wrapResponse(response, "All tickets fetched successfully"));
//		} catch (Exception e) {
//			return ResponseEntity.notFound().build();
//		}
//	}
//
////	@PostMapping("/opentickets/{max}")
////	public ResponseEntity<IResponseDto> getOpenTicketList(@RequestBody List<AtmDetailsDto> atmids,
////			@PathVariable int max) {
////		List<AtmShortDetailsDto> response = taskService.getTicketList(atmids, max);
////		IResponseDto responseDto =restUtils.wrapResponse(response, "Open tickets fetched successfully");
////		return ResponseEntity.ok(responseDto);
////	}
//	
//	@PostMapping("/opentickets/{max}")
//	public ResponseEntity<IResponseDto> getOpenTicketList(@RequestBody List<AtmDetailsDto> atmids,
//			@PathVariable int max) {
//		List<AtmShortDetailsDto> response = taskService.getTicketList(atmids, max);
//
//		AtmShortDetailsListWrapper wrapper = new AtmShortDetailsListWrapper(response);
//		/*
//		 * // Convert List<AtmShortDetailsDto> to List<IGenericDto> List<IGenericDto>
//		 * genericDtoList = response.stream() .map(dto -> (IGenericDto) dto)
//		 * .collect(Collectors.toList());
//		 */
//		IResponseDto responseDto = restUtils.wrapResponse(wrapper, "Open tickets fetched successfully");
//		return ResponseEntity.ok(responseDto);
//	}
//
//	@GetMapping("/reason")
//	public ResponseEntity<IResponseDto> getTicketReasonList() {
//		List<TicketReasonDto> response = ticketService.getTicketReasonList();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Ticket reasons fetched successfully"));
//	}
//
//	@PostMapping("/travel/create")
//	public ResponseEntity<IResponseDto> createTravelPlan(@RequestBody TravelDto travelDto) {
//		GenericResponseDto response = travelService.createTravelPlan(travelDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel plan created successfully"));
//	}
//
//	@GetMapping("/travel/status")
//	public ResponseEntity<IResponseDto> getIsTravellingStatus() {
//		IsTravellingDto response = travelService.getIsTravellingStatus();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travelling status fetched successfully"));
//	}
//
//	@GetMapping("/travel/message")
//	public ResponseEntity<IResponseDto> getTravellingModeMessage() {
//		TravelModeMessage response = travelService.getTravellingModeMessage();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travelling mode message fetched successfully"));
//	}
//
//	@GetMapping("/travel/travel-mode")
//	public ResponseEntity<IResponseDto> getTravelModes() {
//		List<TravelModeDto> response = travelModeService.getTravelModes();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel modes fetched successfully"));
//	}
//
//	@GetMapping("/travel/work-mode")
//	public ResponseEntity<IResponseDto> getWorkModes() {
//		List<WorkModeDto> response = workModeService.getWorkModes();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Work modes fetched successfully"));
//	}
//
//	@PostMapping("/travel/add-travel-mode")
//	public ResponseEntity<IResponseDto> addTravelMode(@RequestBody TravelModeDto travelModeDto) {
//		TravelModeDto response = travelModeService.addTravelMode(travelModeDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Travel mode added successfully"));
//	}
//
//	@PostMapping("/travel/add-work-mode")
//	public ResponseEntity<IResponseDto> addWorkMode(@RequestBody WorkModeDto workModeDto) {
//		WorkModeDto response = workModeService.addWorkMode(workModeDto);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Work mode added successfully"));
//	}
//
//	  @GetMapping("/filters")
//	    public ResponseEntity<IResponseDto> getFilters() {
//	        TaskSummaryOfCeResponseFilterDTO response = taskSummaryFilterOfCeService.getFilters();
//	        return ResponseEntity.ok(restUtils.wrapResponse(response, "Filters fetched successfully"));
//	    }
//
//	@GetMapping("/getAssignAtmFilters")
//	public ResponseEntity<IResponseDto> getAssignAtmFilters() {
//		AssignedAtmforCeFilterResponseDto response = assignedAtmforCeFilterServicel.getAssignAtmFilters();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Assign ATM filters fetched successfully"));
//	}
//
//	@PostMapping("/update-work-mode")
//	public ResponseEntity<IResponseDto> updateWorkMode(@RequestBody UpdateWorkModeReq request) {
//		ResponseMessage response = travelService.updateWorkMode(request);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Work mode updated successfully"));
//	}
}
