package com.hpy.ops360.ticketing.v2.controller;

import java.util.List;
import java.util.Optional;

import org.apache.camel.ProducerTemplate;
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
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto_forAllTickets;
import com.hpy.ops360.ticketing.cm.dto.SearchTicketsDto;
import com.hpy.ops360.ticketing.cm.dto.TicketAtmIdDtoReq;
import com.hpy.ops360.ticketing.cm.dto.TicketEtaUpdateDocumentsReq;
import com.hpy.ops360.ticketing.cm.dto.TicketListCountDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketMainResponseDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSearchRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSuggestionReq;
import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsDTO;
import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.UserDto;
import com.hpy.ops360.ticketing.cm.service.ATMTicketsRaisedService;
import com.hpy.ops360.ticketing.cm.service.AllTicketByCEService;
import com.hpy.ops360.ticketing.cm.service.CommentCMTicketDetailsService;
import com.hpy.ops360.ticketing.cm.service.TicketSearchService;
import com.hpy.ops360.ticketing.cm.service.TravelEtaDetailsService;
import com.hpy.ops360.ticketing.dto.AtmIdDto;
import com.hpy.ops360.ticketing.dto.DocumentDto;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.RejectionReasonListDto;
import com.hpy.ops360.ticketing.dto.TaskApprovedTicketsDto;
import com.hpy.ops360.ticketing.dto.TicketDataResponse;
import com.hpy.ops360.ticketing.dto.TicketFlagStatusRequestDTO;
import com.hpy.ops360.ticketing.dto.TicketFlagStatusResponseDTO;
import com.hpy.ops360.ticketing.dto.TicketHistoryByDateRequest;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryResponse;
import com.hpy.ops360.ticketing.dto.TicketManagementResponse;
import com.hpy.ops360.ticketing.dto.TicketsWithCategoryCountDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.CmTaskService;
import com.hpy.ops360.ticketing.service.SynergyService;
import com.hpy.ops360.ticketing.service.TaskService;
import com.hpy.ops360.ticketing.service.TicketFlagStatusService;
import com.hpy.ops360.ticketing.service.UserAtmDetailsService;
import com.hpy.ops360.ticketing.ticket.dto.CMTaskDto;
import com.hpy.ops360.ticketing.ticket.dto.DownloadFileDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v2/task")
@CrossOrigin("${app.cross-origin.allow}")
public class TaskV2Controller {

	private static final int SUCCESS = 0;

	@Autowired
	private TicketFlagStatusService TicketFlagStatusService;

	@Autowired
	private TaskService taskServicetickets;

	@Autowired
	private TravelEtaDetailsService travelEtaDetailsService;

	@Autowired
	private CommentCMTicketDetailsService commentCMTicketDetailsService;

	@Autowired
	private CmTaskService cmTaskService;

	@Autowired
	private ATMTicketsRaisedService atmTicketsRaisedService;

	@Autowired
	private ProducerTemplate producerTemplate;

	@Autowired
	private CmTaskService taskService;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private SynergyService synergyService;

	@Autowired
	private UserAtmDetailsService userAtmDetailsService;

	@Autowired
	private AllTicketByCEService allTicketByCEService;

	@Autowired
	private TicketSearchService ticketService;

	@GetMapping("/list/ce")
	@Loggable
	public ResponseEntity<List<CmTasksWithDocumentsDto>> getTicketsNumberOfCEAllList() {

		return ResponseEntity.ok(taskService.getTicketsNumberOfCEList());
	}

	// ----------23/05/2024------------------

	@GetMapping("/counting")
	@Loggable
	public ResponseEntity<TicketListCountDTO> getTicketsNumberOfCEListCount() {
		TicketListCountDTO ticketCount = taskService.getTicketsNumberOfCEListCount();
		return ResponseEntity.ok(ticketCount);
	}

	@PostMapping("/getTaskDetailsByTicketNumber")
	@Loggable
	public ResponseEntity<CMTaskDto> getTaskByTicketNumber(@RequestBody TicketAtmIdDtoReq ticketAtmIdDtoReq) {

		return ResponseEntity.ok(taskService.getTaskByTicketNumber(ticketAtmIdDtoReq.getCreatedBy(),
				ticketAtmIdDtoReq.getTicketNumber(), ticketAtmIdDtoReq.getAtmId()));
	}

	@GetMapping("/list/rejectection-reason")
	@Loggable
	public ResponseEntity<List<RejectionReasonListDto>> getRejectionReasons() {
		return ResponseEntity.ok(taskService.getRejectionReasons());
	}

	@PostMapping("/downloadFile")
	@Loggable
	public ResponseEntity<FileDownloadDto> downloadTicketId(@RequestBody DownloadFileDto downloadDto) {
		FileDownloadDto fileDownloadDto1 = taskService.download(downloadDto.getCreatedBy(),
				downloadDto.getTicketNumber(), downloadDto.getAtmId(), downloadDto.getIndex());
		return ResponseEntity.ok(fileDownloadDto1);
	}

	@PostMapping("/action/{status}")
	@Loggable
	public ResponseEntity<ApproveRejectStatusresponseDto> processTicket(@PathVariable("status") String status,
			@RequestBody TaskApprovedTicketsDto taskDTO) {
		ApproveRejectStatusDto approveRejectStatusDto = taskService.updateTicketsNumberData(status, taskDTO);
		if (approveRejectStatusDto.getStatus().equals(SUCCESS)) {
			String result = String.format("%s %s Successfully", status, approveRejectStatusDto.getSynergyTicketNo());
			return new ResponseEntity<>(new ApproveRejectStatusresponseDto(true, result), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ApproveRejectStatusresponseDto(false, "Update failed"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/action-kafka/{status}")
	@Loggable
	public ResponseEntity<ApproveRejectStatusresponseDto> processTicket_viaKafka(@PathVariable("status") String status,
			@RequestBody TaskApprovedTicketsDto taskDTO) {
		ApproveRejectStatusDto approveRejectStatusDto = taskService.updateTicketsNumberData_ViaKafkaHims(status,
				taskDTO);
		if (approveRejectStatusDto.getStatus().equals(SUCCESS)) {
			String result = String.format("%s %s Successfully", status, approveRejectStatusDto.getSynergyTicketNo());
			return new ResponseEntity<>(new ApproveRejectStatusresponseDto(true, result), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ApproveRejectStatusresponseDto(false, "Update failed"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @owner :- Shubham Shinde
	 * @getCeOpenTicketDetails:- getCeOpenTicketDetails for per CE
	 * @return:-Success
	 */
	@GetMapping("/getCeOpenTicketDetails/{ceId}/{status}")
	@Loggable
	public ResponseEntity<OpenTicketsWithCategoryDto> getCeOpenTicketDetails_show(@PathVariable String ceId,
			@PathVariable String status) {
		synergyService.clearCache();
		return ResponseEntity.ok(taskService.getTicketDetailsByCEAndStatus(ceId, status));
	}
//	

	@GetMapping("/getCeOpenTicketDetails_forAllTickets/{ceId}/{status}")
	@Loggable
	public ResponseEntity<OpenTicketsWithCategoryDto_forAllTickets> getCeOpenTicketDetails_showforAlltickets(
			@PathVariable String ceId, @PathVariable String status) {
		synergyService.clearCache();

		return ResponseEntity.ok(taskService.getTicketDetailsByCEAndStatus_forAllTicketOriginal(ceId, status));
	}

	/**
	 * @owner :- Shubham Shinde
	 * @AllTickets:- getTicketDetails_newResponse
	 * @return:-Success
	 */
	@GetMapping("getCeOpenTicketDetails_newResponse/{ceId}")
	@Loggable
	public ResponseEntity<TicketManagementResponse> getTicketDetails_newResponse(@PathVariable String ceId) {
		synergyService.clearCache();
//		return ResponseEntity.ok(taskService.getTicketDetailsByCEAndStatus_newResponse(ceId));
		return ResponseEntity.ok(allTicketByCEService.getTicketDetailsByCEAndStatus_newResponse(ceId));
	}

	@PostMapping("get-all-tickets-on-index/{ceId}/{index}")
	@Loggable
	public ResponseEntity<TicketManagementResponse> getAllTicketDetailsBasedOnIndex(@PathVariable String ceId,
			@PathVariable int index) {
		synergyService.clearCache();
		return ResponseEntity.ok(taskService.getAllTicketDetailsBasedOnIndex(ceId, index));
	}

	@GetMapping("/atm-old/{atmId}/{ceId}")
	@Loggable
	public ResponseEntity<OpenTicketsWithCategoryDto> getTicketsByAtmIdold(@PathVariable String atmId,
			@PathVariable String ceId) {
		synergyService.clearCache();
		return ResponseEntity.ok(taskService.getTicketDetailsByCEAgainstAtm(atmId, ceId));
	}

	@GetMapping("/atm/{atmId}/{ceId}")
	@Loggable
	public ResponseEntity<TicketDataResponse> getTicketsByAtmId(@PathVariable String atmId, @PathVariable String ceId) {
		synergyService.clearCache();
		// OpenTicketsWithCategoryDto baseResponse = ;
		return ResponseEntity
				.ok(taskService.transformToTicketDataResponse(taskService.getTicketDetailsByCEAgainstAtm(atmId, ceId)));
	}

	@PostMapping("/addcomments")
	@Loggable
	public ResponseEntity<GenericResponseDto> addComments(@RequestBody AddCommentDto addCommentDto) {
		return ResponseEntity.ok(synergyService.addComments(addCommentDto));
	}

	@GetMapping("/all-tickets-count")
	@Loggable
	public ResponseEntity<TicketsWithCategoryCountDto> getAllTicketDetailsCount() {
		TicketsWithCategoryCountDto response = taskService.getCeAllTicketDetailsCount();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * @owner :- Shubham Shinde
	 * @get_n_tickets_history:- get_n_tickets_history (Only 10 tickets will show )
	 * @return:-Success
	 */
	@PostMapping("/get_n_tickets_history")
	// @Loggable
	public ResponseEntity<OpenTicketsWithCategoryDto> getNTickets(@RequestBody TicketHistoryDto ticketHistoryReqDto) {
		OpenTicketsWithCategoryDto response = taskService.getCe_nTicketDetailsAgainstAtm(ticketHistoryReqDto);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/n-tickets-history")
	@Loggable
	public ResponseEntity<IResponseDto> getNTicketsHistory(@RequestBody TicketHistoryDto request) {
		TicketHistoryResponse response = taskService.processTickets_forNticketHistory(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "getNTicketsHistory fetched successfully."));
	}

	@PostMapping("/ce-n-tickets-history")
	@Loggable
	public ResponseEntity<TicketHistoryResponse> getNTicketsHistoryCE(@RequestBody TicketHistoryDto request) {
		TicketHistoryResponse response = taskService.processTickets_forNticketHistory_Ce(request);
		return ResponseEntity.ok(response);
	}

	/**
	 * @owner :- Shubham Shinde
	 * @tickets_history_By_Date :- getTicketHistoryByDate For CE
	 * @return:-Success
	 */

	@PostMapping("/history-bydate")
	@Loggable
	public ResponseEntity<OpenTicketsWithCategoryDto> getTicketHistoryByDate(
			@RequestBody TicketHistoryByDateRequest request) {
//		log.info("Received request for ticket history with atmId: {} and date: {}", request.getAtmid(),
//				request.getFromdate());

		OpenTicketsWithCategoryDto response = taskService.getTicketHistoryBydateForCe(request);
		return ResponseEntity.ok(response);

	}

	/**
	 * @owner :- Shubham Shinde
	 * @tickets_history_By_Date :- getTicketHistoryByDate For CM
	 * @return:-Success
	 */

	@GetMapping("/Tickets-history/{atmId}")
	@Loggable
	public ResponseEntity<OpenTicketsWithCategoryDto> getTicketHistory(@PathVariable String atmId) {
		OpenTicketsWithCategoryDto response = taskService.getTicketHistoryBydateForCm(atmId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/travel-details")
	@Loggable
	public ResponseEntity<Optional<TravelEtaDetailsDTO>> getTravelDetails(
			@RequestBody TravelEtaDetailsRequestDTO travelEtaDetailsRequestDTO) {
		Optional<TravelEtaDetailsDTO> response = travelEtaDetailsService
				.getTravelDetails(travelEtaDetailsRequestDTO.getAtmId(), travelEtaDetailsRequestDTO.getTicketNumber());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/CommentCMTicketDetails")
	@Loggable
	public ResponseEntity<Void> insertUpdateCMTicketDetails(@RequestBody CommentCMTicketDetailsRequestDTO requestDTO) {
		commentCMTicketDetailsService.insertUpdateCMTicketDetails(requestDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/atmUptime")
	@Loggable
	public ResponseEntity<AtmMtdUptimeDto> getAtmUptime(@RequestBody AtmIdDto atmIdDto) {
		return ResponseEntity.ok(cmTaskService.getAtmUptime(atmIdDto.getAtmId()));
	}

	@PostMapping("/ticketEtaUpdateDocuments")
	@Loggable
	public ResponseEntity<List<DocumentDto>> getTicketUpdateDocuments(
			@RequestBody TicketEtaUpdateDocumentsReq ticketEtaUpdateDocumentsReq) {
		return ResponseEntity.ok(cmTaskService.getTicketUpdateDocuments(ticketEtaUpdateDocumentsReq.getUsername(),
				ticketEtaUpdateDocumentsReq.getAtmId(), ticketEtaUpdateDocumentsReq.getTicketNumber()));
	}

	@PostMapping("/ce-machine-up-down-count")
	@Loggable
	public ResponseEntity<IResponseDto> getCeMachineUpDownCount(@RequestBody UserDto userDto) {
		CeMachineUpDownCountDto response = taskService.getCeMachineUpDownCount(userDto.getUsername());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Fetched Updown Count"));
	}

	@PostMapping("/ATMTicketsRaisedCount")
	@Loggable
	public ResponseEntity<ATMTicketsRaisedCountDto> getATMTicketsRaisedCount(
			@RequestBody ATMTicketsRaisedRequestDTO requestDTO) {
		ATMTicketsRaisedCountDto atmTicketsCount = atmTicketsRaisedService.getATMTicketsRaisedCount(requestDTO);
		return ResponseEntity.ok(atmTicketsCount);

	}

	@PutMapping("/update_pin_tickets")
	@Loggable
	public ResponseEntity<TicketFlagStatusResponseDTO> insertOrUpdateFlagStatus(
			@RequestBody TicketFlagStatusRequestDTO dto) {
		TicketFlagStatusResponseDTO result = TicketFlagStatusService.insertOrUpdateFlagStatus(dto);
		return ResponseEntity.ok(result);
	}

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

	@PostMapping("/portal/index-of-ticket/open-closed-ticket-suggestion")
	public ResponseEntity<IResponseDto> getOpenClosedTicketSuggestion(@RequestBody TicketSuggestionReq request) {
		List<String> response = ticketService.getOpenClosedTicketSuggestion(request);
		return ResponseEntity.ok(restUtils.wrapResponseListOfString(response, "OK"));
	}

}
