//package com.hpy.ops360.ticketing.controller.custrespcontroller;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.apache.camel.ProducerTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.hpy.ops360.ticketing.cm.dto.ATMTicketsRaisedCountDto;
//import com.hpy.ops360.ticketing.cm.dto.ATMTicketsRaisedRequestDTO;
//import com.hpy.ops360.ticketing.cm.dto.AddCommentDto;
//import com.hpy.ops360.ticketing.cm.dto.ApproveRejectStatusDto;
//import com.hpy.ops360.ticketing.cm.dto.ApproveRejectStatusresponseDto;
//import com.hpy.ops360.ticketing.cm.dto.AtmMtdUptimeDto;
//import com.hpy.ops360.ticketing.cm.dto.CeMachineUpDownCountDto;
//import com.hpy.ops360.ticketing.cm.dto.CmTasksWithDocumentsDto;
//import com.hpy.ops360.ticketing.cm.dto.CommentCMTicketDetailsRequestDTO;
//import com.hpy.ops360.ticketing.cm.dto.FileDownloadDto;
//import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
//import com.hpy.ops360.ticketing.cm.dto.TicketAtmIdDtoReq;
//import com.hpy.ops360.ticketing.cm.dto.TicketEtaUpdateDocumentsReq;
//import com.hpy.ops360.ticketing.cm.dto.TicketListCountDTO;
//import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsDTO;
//import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsRequestDTO;
//import com.hpy.ops360.ticketing.cm.dto.UserDto;
//import com.hpy.ops360.ticketing.cm.service.ATMTicketsRaisedService;
//import com.hpy.ops360.ticketing.cm.service.CommentCMTicketDetailsService;
//import com.hpy.ops360.ticketing.cm.service.TravelEtaDetailsService;
//import com.hpy.ops360.ticketing.dto.AtmIdDto;
//import com.hpy.ops360.ticketing.dto.DocumentDto;
//import com.hpy.ops360.ticketing.dto.GenericResponseDto;
//import com.hpy.ops360.ticketing.dto.RejectionReasonListDto;
//import com.hpy.ops360.ticketing.dto.TaskApprovedTicketsDto;
//import com.hpy.ops360.ticketing.dto.TicketFlagStatusRequestDTO;
//import com.hpy.ops360.ticketing.dto.TicketFlagStatusResponseDTO;
//import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
//import com.hpy.ops360.ticketing.dto.TicketHistoryResponse;
//import com.hpy.ops360.ticketing.dto.TicketManagementResponse;
//import com.hpy.ops360.ticketing.dto.TicketsWithCategoryCountDto;
//import com.hpy.ops360.ticketing.service.CmTaskService;
//import com.hpy.ops360.ticketing.service.SynergyService;
//import com.hpy.ops360.ticketing.service.TaskService;
//import com.hpy.ops360.ticketing.service.TicketFlagStatusService;
//import com.hpy.ops360.ticketing.service.UserAtmDetailsService;
//import com.hpy.ops360.ticketing.ticket.dto.CMTaskDto;
//import com.hpy.ops360.ticketing.ticket.dto.DownloadFileDto;
//import com.hpy.rest.dto.IResponseDto;
//import com.hpy.rest.util.RestUtils;
//
//import lombok.extern.log4j.Log4j2;
//
//@Log4j2
//@RestController
//@RequestMapping("/v2/task")
//@CrossOrigin("${app.cross-origin.allow}")
//public class TaskControllerV2 {
//
//	private static final int SUCCESS = 0;
//
//	@Autowired
//	private TicketFlagStatusService TicketFlagStatusService;
//
//	@Autowired
//	private TaskService taskServicetickets;
//
//	@Autowired
//	private TravelEtaDetailsService travelEtaDetailsService;
//
//	@Autowired
//	private CommentCMTicketDetailsService commentCMTicketDetailsService;
//
//	@Autowired
//	private CmTaskService cmTaskService;
//
//	@Autowired
//	private ATMTicketsRaisedService atmTicketsRaisedService;
//
//	@Autowired
//	private ProducerTemplate producerTemplate;
//
//	@Autowired
//	private CmTaskService taskService;
//
//	@Autowired
//	private SynergyService synergyService;
//	
//	@Autowired
//	private RestUtils restUtils;
//
//	@Autowired
//	private UserAtmDetailsService userAtmDetailsService;
//
//	@GetMapping("/list/ce")
//	public ResponseEntity<IResponseDto> getTicketsNumberOfCEAllList() {
//
//		log.info("***** Inside getTicketsNumberOfCEAllList Controller *****");	
//		List<CmTasksWithDocumentsDto> response = taskService.getTicketsNumberOfCEList();
//		log.info("Response Returned successfully from getTicketsNumberOfCEAllList:- "+ response);
////		return ResponseEntity.ok(restUtils.wrapResponse(response, "getTicketsNumberOfCEAllList details fetched successfully"));
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getTicketsNumberOfCEAllList details fetched successfully"));
//   
//	}
//
//	// ----------23/05/2024------------------
//
//	@GetMapping("/counting")
//	public ResponseEntity<IResponseDto> getTicketsNumberOfCEListCount() {
//		
//		
//		log.info("***** Inside getTicketsNumberOfCEAllList Controller *****");	
//		TicketListCountDTO ticketCount = taskService.getTicketsNumberOfCEListCount();
//		log.info("Response Returned successfully from getTicketsNumberOfCEAllList:- "+ ticketCount);
//		return ResponseEntity.ok(restUtils.wrapResponse(ticketCount, "getTicketsNumberOfCEAllList details fetched successfully"));
//   
//	}
//
//	@PostMapping("/getTaskDetailsByTicketNumber")
//	public ResponseEntity<IResponseDto> getTaskByTicketNumber(@RequestBody TicketAtmIdDtoReq ticketAtmIdDtoReq) {
//
//		
//		log.info("***** Inside getTaskByTicketNumber Controller *****");	
//		CMTaskDto response = taskService.getTaskByTicketNumber(ticketAtmIdDtoReq.getCreatedBy(),
//				ticketAtmIdDtoReq.getTicketNumber(), ticketAtmIdDtoReq.getAtmId());
//		log.info("Response Returned successfully from getTaskByTicketNumber:- "+ response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getTaskByTicketNumber details fetched successfully"));
//   
//	}
//
//	
//	@GetMapping("/list/rejectection-reason")
//	public ResponseEntity<IResponseDto> getRejectionReasons() {
//		
//		log.info("***** Inside getRejectionReasons Controller *****");	
//		List<RejectionReasonListDto> response = taskService.getRejectionReasons();
//		log.info("Response Returned successfully from getRejectionReasons:- "+ response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getRejectionReasons details fetched successfully"));
//	}
//
//
//	@PostMapping("/downloadFile")
//	public ResponseEntity<IResponseDto> downloadTicketId(@RequestBody DownloadFileDto downloadDto) {
//		log.info("***** Inside downloadTicketId Controller *****");	
//		
//		FileDownloadDto response = taskService.download(downloadDto.getCreatedBy(),
//				downloadDto.getTicketNumber(), downloadDto.getAtmId(), downloadDto.getIndex());
//		log.info("Response Returned successfully from downloadTicketId:- "+ response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "downloadTicketId details fetched successfully"));
//	}
//
//
//	@PostMapping("/action/{status}")
//	public ResponseEntity<IResponseDto> processTicket(@PathVariable("status") String status,
//			@RequestBody TaskApprovedTicketsDto taskDTO) {
//		
//		log.info("***** Inside processTicket Controller *****");
//		ApproveRejectStatusDto approveRejectStatusDto = taskService.updateTicketsNumberData(status, taskDTO);
//		
//		String message;
//	    boolean success;
//
//	    if (approveRejectStatusDto.getStatus().equals(SUCCESS)) {
//	        message = String.format("%s %s Successfully", status, approveRejectStatusDto.getSynergyTicketNo());
//	        success = true;
//	    } else {
//	        message = "Update failed";
//	        success = false;
//	    }
//	    ApproveRejectStatusresponseDto responseDto = new ApproveRejectStatusresponseDto(success, message);
//	    log.info("***** Process Ticket Result *****: " + responseDto);
//	    return ResponseEntity.ok(restUtils.wrapResponse(responseDto, message));
//	}
//
//
//	@GetMapping("/getCeOpenTicketDetails/{ceId}/{status}")
//	public ResponseEntity<IResponseDto> getCeOpenTicketDetails_show(@PathVariable String ceId,
//			@PathVariable String status) {
//		synergyService.clearCache();
//		
//		log.info("***** Inside getCeOpenTicketDetails_show Controller *****");
//		OpenTicketsWithCategoryDto response=taskService.getTicketDetailsByCEAndStatus(ceId, status);
//		log.info("getTicketDetailsByCEAndStatus Ticket Result: " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getCeOpenTicketDetails_show fetched Successfully"));
//	}
//
//
//	@GetMapping("/atm/{atmId}/{ceId}")
//	public ResponseEntity<IResponseDto> getTicketsByAtmId(@PathVariable String atmId,
//			@PathVariable String ceId) {
//		synergyService.clearCache();
//		
//		log.info("***** Inside getTicketsByAtmId Controller *****");
//		OpenTicketsWithCategoryDto response=taskService.getTicketDetailsByCEAgainstAtm(atmId, ceId);
//		log.info("getTicketDetailsByCEAgainstAtm Ticket Result: " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(taskService.transformToTicketDataResponse(taskService.getTicketDetailsByCEAgainstAtm(atmId, ceId)), "getTicketDetailsByCEAgainstAtm fetched Successfully"));
//	}
//
//	@PostMapping("/addcomments")
//	public ResponseEntity<IResponseDto> addComments(@RequestBody AddCommentDto addCommentDto) {
//		log.info("***** Inside addComments Controller *****");
//		GenericResponseDto response=synergyService.addComments(addCommentDto);
//		log.info("addComments Result: " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "Comment added Successfully"));
//	}
//
//	@GetMapping("/all-tickets-count")
//	public ResponseEntity<IResponseDto> getAllTicketDetailsCount() {
//		log.info("***** Inside getAllTicketDetailsCount Controller *****");
//		TicketsWithCategoryCountDto response = taskService.getCeAllTicketDetailsCount();
//		log.info("getAllTicketDetailsCount Result: " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getCeAllTicketDetailsCount Successfully"));
//	}
//
//	@PostMapping("/get_n_tickets_history")
//	public ResponseEntity<IResponseDto> getNTickets(@RequestBody TicketHistoryDto ticketHistoryReqDto) {
//		log.info("***** Inside getNTickets Controller *****");
//		OpenTicketsWithCategoryDto response = taskService.getCe_nTicketDetailsAgainstAtm(ticketHistoryReqDto);
//		log.info("getCe_nTicketDetailsAgainstAtm Result: " + response);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getCe_nTicketDetailsAgainstAtm Successfully"));
//	}
//
//	@PostMapping("/travel-details")
//	public ResponseEntity<IResponseDto> getTravelDetails(
//			@RequestBody TravelEtaDetailsRequestDTO travelEtaDetailsRequestDTO) {
//		log.info("***** Inside getTravelDetails Controller *****");
//		Optional<TravelEtaDetailsDTO> response = travelEtaDetailsService
//				.getTravelDetails(travelEtaDetailsRequestDTO.getAtmId(), travelEtaDetailsRequestDTO.getTicketNumber());
//		log.info("getTravelDetails Result: " + response);
//		log.info("getTravelDetails Result: " + response);
//
//	    if (response.isPresent()) {
//	        TravelEtaDetailsDTO responsedata = response.get();
//	        return ResponseEntity.ok(restUtils.wrapResponse(responsedata, "Travel details fetched successfully."));
//	    } else {
//	        return ResponseEntity.ok(restUtils.wrapNullResponse("No travel details found for the given ATM ID and ticket number.", HttpStatus.NO_CONTENT));
//	    }
//	
//	}
//
//	@PostMapping("/CommentCMTicketDetails")
//	public ResponseEntity<IResponseDto> insertUpdateCMTicketDetails(@RequestBody CommentCMTicketDetailsRequestDTO requestDTO) {
//		log.info("***** Inside insertUpdateCMTicketDetails Controller *****");
//		try {
//		commentCMTicketDetailsService.insertUpdateCMTicketDetails(requestDTO);
//		String successMessage = "Comment CM ticket details updated successfully.";
//	    log.info(successMessage);
//	    
//	    return ResponseEntity.ok(restUtils.wrapNullResponse(successMessage, HttpStatus.CREATED));
//		} catch (Exception e) {
//			return ResponseEntity.ok(restUtils.wrapNullResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
//		}
//	}
//
//	@PostMapping("/atmUptime")
//	public ResponseEntity<IResponseDto> getAtmUptime(@RequestBody AtmIdDto atmIdDto) {
//	    log.info("***** Inside getAtmUptime Controller *****");
//
//	    AtmMtdUptimeDto uptimeData = cmTaskService.getAtmUptime(atmIdDto.getAtmId());
//	    log.info("Retrieved ATM uptime data: " + uptimeData);
//
//	    return ResponseEntity.ok(restUtils.wrapResponse(uptimeData, "ATM uptime fetched successfully."));
//	}
//
//
//	@PostMapping("/ticketEtaUpdateDocuments")
//	public ResponseEntity<IResponseDto> getTicketUpdateDocuments(
//	        @RequestBody TicketEtaUpdateDocumentsReq ticketEtaUpdateDocumentsReq) {
//	    log.info("***** Inside getTicketUpdateDocuments Controller *****");
//	    
//	    List<DocumentDto> documents = cmTaskService.getTicketUpdateDocuments(
//	            ticketEtaUpdateDocumentsReq.getUsername(),
//	            ticketEtaUpdateDocumentsReq.getAtmId(),
//	            ticketEtaUpdateDocumentsReq.getTicketNumber()
//	    );
//	    
//	    log.info("Retrieved documents: " + documents);
//	    return ResponseEntity.ok(restUtils.wrapResponse(documents, "Ticket update documents fetched successfully."));
//	}
//
//	@PostMapping("/ce-machine-up-down-count")
//	public ResponseEntity<IResponseDto> getCeMachineUpDownCount(@RequestBody UserDto userDto) {
//	    log.info("***** Inside getCeMachineUpDownCount Controller *****");
//	    
//	    CeMachineUpDownCountDto countDto = taskService.getCeMachineUpDownCount(userDto.getUsername());
//	    
//	    log.info("Retrieved CE machine up/down count: " + countDto);
//	    return ResponseEntity.ok(restUtils.wrapResponse(countDto, "CE machine up/down count fetched successfully."));
//	}
//
//
//	@PostMapping("/ATMTicketsRaisedCount")
//	public ResponseEntity<IResponseDto> getATMTicketsRaisedCount(
//	        @RequestBody ATMTicketsRaisedRequestDTO requestDTO) {
//	    log.info("***** Inside getATMTicketsRaisedCount Controller *****");
//	    
//	    ATMTicketsRaisedCountDto atmTicketsCount = atmTicketsRaisedService.getATMTicketsRaisedCount(requestDTO);
//	    
//	    log.info("Retrieved ATM tickets raised count: " + atmTicketsCount);
//	    return ResponseEntity.ok(restUtils.wrapResponse(atmTicketsCount, "ATM tickets raised count fetched successfully."));
//	}
//
//
//	@PutMapping("/update_pin_tickets")
//	public ResponseEntity<IResponseDto> insertOrUpdateFlagStatus(@RequestBody TicketFlagStatusRequestDTO dto) {
//	    log.info("***** Inside insertOrUpdateFlagStatus Controller *****");
//	    
//	    TicketFlagStatusResponseDTO result = TicketFlagStatusService.insertOrUpdateFlagStatus(dto);
//	    log.info("Flag status updated successfully: " + result);
//	    
//	    return ResponseEntity.ok(restUtils.wrapResponse(result, "Ticket flag status updated successfully."));
//	}
//	
//	@PostMapping("get-all-tickets-on-index/{cmId}/{index}")
//	public ResponseEntity<IResponseDto> getAllTicketDetailsBasedOnIndex(@PathVariable String cmId,@PathVariable int index) {
//		synergyService.clearCache();
//		TicketManagementResponse response=taskService.getAllTicketDetailsBasedOnIndex(cmId,index);
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getAllTicketDetailsBasedOnIndex fetched successfully."));
//	}
//	
//	
//	@PostMapping("/n-tickets-history")
//	public ResponseEntity<IResponseDto> getNTicketsHistory(@RequestBody TicketHistoryDto request) {
//		TicketHistoryResponse response=taskService.processTickets_forNticketHistory(request);
//		
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "getNTicketsHistory fetched successfully."));
//	}
//
//}
