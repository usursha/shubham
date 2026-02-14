package com.hpy.ops360.ticketing.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.ATMTicketsRaisedCountDto;
import com.hpy.ops360.ticketing.cm.dto.ATMTicketsRaisedRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.AddCommentDto;
import com.hpy.ops360.ticketing.cm.dto.ApproveRejectStatusDto;
import com.hpy.ops360.ticketing.cm.dto.ApproveRejectStatusresponseDto;
import com.hpy.ops360.ticketing.cm.dto.AtmMtdUptimeDto;
import com.hpy.ops360.ticketing.cm.dto.CeMachineUpDownCountDto;
import com.hpy.ops360.ticketing.cm.dto.CmTasksWithDocumentsDto;
import com.hpy.ops360.ticketing.cm.dto.CommentCMTicketDetailsRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.FileDownloadDto;
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
import com.hpy.ops360.ticketing.cm.dto.RejectionReasonDto;
import com.hpy.ops360.ticketing.cm.dto.ResponseCmTasksWithDocumentsWrapper;
import com.hpy.ops360.ticketing.cm.dto.SearchTicketsDto;
import com.hpy.ops360.ticketing.cm.dto.TicketAtmIdDtoReq;
import com.hpy.ops360.ticketing.cm.dto.TicketEtaUpdateDocumentsReq;
import com.hpy.ops360.ticketing.cm.dto.TicketListCountDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketMainResponseDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSearchRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSearchResponseDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketUpdateDocumentswrapper;
import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsDTO;
import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.UserDto;
import com.hpy.ops360.ticketing.cm.service.ATMTicketsRaisedService;
import com.hpy.ops360.ticketing.cm.service.CommentCMTicketDetailsService;
import com.hpy.ops360.ticketing.cm.service.TicketSearchService;
import com.hpy.ops360.ticketing.cm.service.TravelEtaDetailsService;
import com.hpy.ops360.ticketing.dto.AtmIdDto;
import com.hpy.ops360.ticketing.dto.CommentCMTicketResponseDTO;
import com.hpy.ops360.ticketing.dto.DocumentDto;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.TaskApprovedTicketsDto;
import com.hpy.ops360.ticketing.dto.TicketFlagStatusRequestDTO;
import com.hpy.ops360.ticketing.dto.TicketFlagStatusResponseDTO;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.TicketsWithCategoryCountDto;
import com.hpy.ops360.ticketing.entity.RejectionReason;
import com.hpy.ops360.ticketing.service.CmTaskService;
import com.hpy.ops360.ticketing.service.SynergyService;
import com.hpy.ops360.ticketing.service.TicketFlagStatusService;
import com.hpy.ops360.ticketing.ticket.dto.CMTaskDto;
import com.hpy.ops360.ticketing.ticket.dto.DownloadFileDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseDto;
import com.hpy.rest.exception.DataNotFoundException;
import com.hpy.rest.exception.DatabaseException;
import com.hpy.rest.exception.TokenExpiredException;
import com.hpy.rest.util.RestUtils;

import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/task2")
@CrossOrigin("${app.cross-origin.allow}")
public class TaskRestController {

	private static final int SUCCESS = 0;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private CmTaskService taskService;
	@Autowired
	private SynergyService synergyService;

	@Autowired
	private TravelEtaDetailsService travelEtaDetailsService;

	@Autowired
	private CmTaskService cmTaskService;

	@Autowired
	private CommentCMTicketDetailsService commentCMTicketDetailsService;

	@Autowired
	private ATMTicketsRaisedService atmTicketsRaisedService;

	@Autowired
	private TicketFlagStatusService ticketFlagStatusService;

	@Autowired
	private TicketSearchService ticketService;

	@GetMapping("/list/ce")
	public ResponseEntity<IResponseDto> getTicketsNumberOfCEAllList() {
		List<CmTasksWithDocumentsDto> data = taskService.getTicketsNumberOfCEList();
		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));

	}

	@GetMapping("/counting")
	public ResponseEntity<IResponseDto> getTicketsNumberOfCEListCount() {
		TicketListCountDTO data = taskService.getTicketsNumberOfCEListCount();

		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/getTaskDetailsByTicketNumber")
	public ResponseEntity<IResponseDto> getTaskByTicketNumber(@RequestBody TicketAtmIdDtoReq ticketAtmIdDtoReq) {

		CMTaskDto data = taskService.getTaskByTicketNumber(ticketAtmIdDtoReq.getCreatedBy(),
				ticketAtmIdDtoReq.getTicketNumber(), ticketAtmIdDtoReq.getAtmId());

		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/downloadFile")
	public ResponseEntity<IResponseDto> downloadTicketId(@RequestBody DownloadFileDto downloadDto) {
		FileDownloadDto data = taskService.download(downloadDto.getCreatedBy(), downloadDto.getTicketNumber(),
				downloadDto.getAtmId(), downloadDto.getIndex());

		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/action/{status}")
	public ResponseEntity<IResponseDto> processTicket(@PathVariable("status") String status,
			@RequestBody TaskApprovedTicketsDto taskDTO) {

		ApproveRejectStatusDto approveRejectStatusDto = taskService.updateTicketsNumberData(status, taskDTO);

		if (approveRejectStatusDto.getStatus().equals(SUCCESS)) {
			String result = String.format("%s %s Successfully", status, approveRejectStatusDto.getSynergyTicketNo());
			ApproveRejectStatusresponseDto data = new ApproveRejectStatusresponseDto(true, result);

			IResponseDto responseDto = restUtils.wrapResponse(data, "Success");
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} else {
			ApproveRejectStatusresponseDto data = new ApproveRejectStatusresponseDto(false, "Update failed");

			IResponseDto responseDto = restUtils.wrapResponse(data, "Update failed");
			return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@GetMapping("/getCeOpenTicketDetails/{ceId}/{status}")
//	public ResponseEntity<IResponseDto> getCeOpenTicketDetails(@PathVariable String ceId, @PathVariable String status) {
//		synergyService.clearCache();
//
//		OpenTicketsWithCategoryDto data = taskService.getTicketDetailsByCEAndStatus(ceId, status);
//
//		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
//	}

	@GetMapping("/atm/{atmId}/{ceId}")
	public ResponseEntity<IResponseDto> getTicketsByAtmId(@PathVariable String atmId, @PathVariable String ceId) {
		synergyService.clearCache();

		OpenTicketsWithCategoryDto data = taskService.getTicketDetailsByCEAgainstAtm(atmId, ceId);

		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/addcomments")
	public ResponseEntity<IResponseDto> addComments(@RequestBody AddCommentDto addCommentDto) {

		GenericResponseDto data = synergyService.addComments(addCommentDto);
		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@GetMapping("/all-tickets-count")
	public ResponseEntity<IResponseDto> getAllTicketDetailsCount() {
		TicketsWithCategoryCountDto response = taskService.getCeAllTicketDetailsCount();
		// Use RestUtils to wrap the response
		IResponseDto formattedResponse = restUtils.wrapResponse(response, "Fetched all ticket details count");

		return new ResponseEntity<>(formattedResponse, HttpStatus.OK);
	}

	@PostMapping("/get_n_tickets_history")
	public ResponseEntity<IResponseDto> getNTickets(@RequestBody TicketHistoryDto ticketHistoryReqDto) {
		OpenTicketsWithCategoryDto data = taskService.getCe_nTicketDetailsAgainstAtm(ticketHistoryReqDto);

		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/travel-details")
	public ResponseEntity<IResponseDto> getTravelDetails(
			@RequestBody TravelEtaDetailsRequestDTO travelEtaDetailsRequestDTO) {
		Optional<TravelEtaDetailsDTO> response = travelEtaDetailsService
				.getTravelDetails(travelEtaDetailsRequestDTO.getAtmId(), travelEtaDetailsRequestDTO.getTicketNumber());

		if (response.isPresent()) {
			IResponseDto responseDto = restUtils.wrapResponse(response.get(), "Success");
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} else {
			IResponseDto responseDto = restUtils.wrapNullResponse("Travel details not found",
					HttpStatus.EXPECTATION_FAILED);
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		}
	}

	@PostMapping("/atmUptime")
	public ResponseEntity<IResponseDto> getAtmUptime(@RequestBody AtmIdDto atmIdDto) {
		AtmMtdUptimeDto data = cmTaskService.getAtmUptime(atmIdDto.getAtmId());
		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/ticketEtaUpdateDocuments")
	public ResponseEntity<IResponseDto> getTicketUpdateDocuments(
			@RequestBody TicketEtaUpdateDocumentsReq ticketEtaUpdateDocumentsReq) {
		List<DocumentDto> documentList = cmTaskService.getTicketUpdateDocuments(
				ticketEtaUpdateDocumentsReq.getUsername(), ticketEtaUpdateDocumentsReq.getAtmId(),
				ticketEtaUpdateDocumentsReq.getTicketNumber());

		TicketUpdateDocumentswrapper datawrapper = new TicketUpdateDocumentswrapper();
		datawrapper.setDocumentDtolist(documentList);

		IResponseDto response = restUtils.wrapResponse(datawrapper, "ETA Ticket Update Documents Fetched");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/ce-machine-up-down-count")
	public ResponseEntity<IResponseDto> getCeMachineUpDownCount(@RequestBody UserDto userDto) {

		CeMachineUpDownCountDto data = taskService.getCeMachineUpDownCount(userDto.getUsername());
		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));
	}

	@PostMapping("/ATMTicketsRaisedCount")
	public ResponseEntity<IResponseDto> getATMTicketsRaisedCount(@RequestBody ATMTicketsRaisedRequestDTO requestDTO) {
		ATMTicketsRaisedCountDto data = atmTicketsRaisedService.getATMTicketsRaisedCount(requestDTO);

		return ResponseEntity.ok(restUtils.wrapResponse(data, "OK"));

	}

//	@PostMapping("/Open-close-tickets")
//	public ResponseEntity<IResponseDto> getTicketsForCm(@RequestBody TicketRequestDTO request) {
//		//log.info("Received POST request for tickets with cmUserId: {}", request.getCmUserId());
//
//		try {
//			TicketMainResponseDTO tickets = ticketService.getTicketsGroupedByDate(request);
//			return ResponseEntity.ok(restUtils.wrapResponse(tickets, "OK"));
//		} catch (Exception e) {
//			log.error("Error processing ticket request: ", e);
//			return ResponseEntity.internalServerError().build();
//		}
//	}

	@PostMapping("/Open-close-tickets")
	public ResponseEntity<IResponseDto> getGroupedTickets(@RequestBody TicketRequestDTO request) {

		TicketMainResponseDTO response = ticketService.getTicketsGroupedByDate(request);

		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
	}
	
	
	@PostMapping("/search-Open-close-tickets")
	public ResponseEntity<IResponseDto> getGroupedTickets(@RequestBody TicketSearchRequestDTO request) {

		List<SearchTicketsDto> response = ticketService.getSearchTickets(request);

		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
	}
	

	/**
	 * @owner - Shubham Shinde
	 * @API - CM API changes for custom response
	 * @return- "responseCode": 200, "message": "OK", "data": []
	 */

	/*
	 * @PutMapping("/update_pin_tickets") public ResponseEntity<IResponseDto>
	 * insertOrUpdateFlagStatus(@RequestBody TicketFlagStatusRequestDTO dto) {
	 * TicketFlagStatusResponseDTO data =
	 * ticketFlagStatusService.insertOrUpdateFlagStatus(dto); return
	 * ResponseEntity.ok(restUtils.wrapResponse(data, "OK")); }
	 * 
	 * @GetMapping("/list/rejection-reason") public ResponseEntity<IResponseDto>
	 * getRejectionReasons() { List<RejectionReasonDto> data =
	 * taskService.getRejectionReasons();
	 * 
	 * if (data.isEmpty()) { return
	 * ResponseEntity.ok(restUtils.wrapNullResponse("No rejection reasons found",
	 * HttpStatus.NOT_FOUND)); }
	 * 
	 * return ResponseEntity.ok(restUtils.wrapResponse(data,
	 * "Rejection reasons fetched successfully")); }
	 * 
	 * @PostMapping("/CommentCMTicketDetails") public ResponseEntity<IResponseDto>
	 * insertUpdateCMTicketDetails(
	 * 
	 * @RequestBody CommentCMTicketDetailsRequestDTO requestDTO) {
	 * CommentCMTicketResponseDTO responseDTO =
	 * commentCMTicketDetailsService.insertUpdateCMTicketDetails(requestDTO);
	 * 
	 * if (responseDTO.isSuccess()) { return
	 * ResponseEntity.ok(restUtils.wrapResponse(responseDTO, "OK")); } else { return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body(restUtils.wrapResponse(responseDTO, responseDTO.getMessage())); } }
	 */
}
