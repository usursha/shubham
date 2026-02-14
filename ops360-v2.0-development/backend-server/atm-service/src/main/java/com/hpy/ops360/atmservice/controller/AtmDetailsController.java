package com.hpy.ops360.atmservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmMtdDto;
import com.hpy.ops360.atmservice.dto.AtmIdDto;
import com.hpy.ops360.atmservice.dto.AtmListData_forSearchRequestDto;
import com.hpy.ops360.atmservice.dto.AtmListData_forSearchResponseDto;
import com.hpy.ops360.atmservice.dto.CMUnderCEAtmDetailsDTO;
import com.hpy.ops360.atmservice.dto.CeAssignedAtmDto;
import com.hpy.ops360.atmservice.dto.CeAtmMappingDto;
import com.hpy.ops360.atmservice.dto.InActCeAtmMappingDto;
import com.hpy.ops360.atmservice.dto.IndexOfAtmFilterResponseDto;
import com.hpy.ops360.atmservice.dto.IndexOfUpDawnAtmForCmResponse;
import com.hpy.ops360.atmservice.dto.NTicketHistory;
import com.hpy.ops360.atmservice.dto.SearchBankLocationDto;
import com.hpy.ops360.atmservice.dto.TicketFilterMainResponseDto;
import com.hpy.ops360.atmservice.dto.TicketFilterOptionsResponse;
import com.hpy.ops360.atmservice.dto.TicketFilterPagenationDto;
import com.hpy.ops360.atmservice.dto.TicketFilterRequestDTO;
import com.hpy.ops360.atmservice.dto.TicketHistoryDto;
import com.hpy.ops360.atmservice.dto.TransactionSummaryRequest;
import com.hpy.ops360.atmservice.dto.TransactionSummaryResponse;
import com.hpy.ops360.atmservice.dto.UserStatusDto;
import com.hpy.ops360.atmservice.entity.AtmDetailsPagnated;
import com.hpy.ops360.atmservice.entity.AtmIndent;
import com.hpy.ops360.atmservice.entity.AtmIndentDetails;
import com.hpy.ops360.atmservice.logapi.Loggable;
import com.hpy.ops360.atmservice.request.AtmDetailsPagnatedRequestDto;
import com.hpy.ops360.atmservice.request.AtmPaginationRequestDTO;
import com.hpy.ops360.atmservice.request.TicketRaisedCategoriesByAtmRequestDto;
import com.hpy.ops360.atmservice.response.AtmDetailsPagenationResponseDTO;
import com.hpy.ops360.atmservice.response.PagedResponse;
import com.hpy.ops360.atmservice.response.TicketRaisedResponseByAtm;
import com.hpy.ops360.atmservice.service.AtmDetailsFromSpService;
import com.hpy.ops360.atmservice.service.AtmDetailsPagenationService;
import com.hpy.ops360.atmservice.service.AtmDetailsService;
import com.hpy.ops360.atmservice.service.AtmListData_forSearchService;
import com.hpy.ops360.atmservice.service.CeAtmMappingService;
import com.hpy.ops360.atmservice.service.CmSynopsisService;
import com.hpy.ops360.atmservice.service.IndexOFAtmTicketFilterService;
import com.hpy.ops360.atmservice.service.IndexOfAtmFilterService;
import com.hpy.ops360.atmservice.service.IndexOfUpDawnAtmForCmService;
import com.hpy.ops360.atmservice.service.NTicketsHistoryCEService;
import com.hpy.ops360.atmservice.service.TicketRaisedCategoriesByAtmService;
import com.hpy.ops360.atmservice.service.TicketService;
import com.hpy.ops360.atmservice.service.TransactionSummaryService;
import com.hpy.ops360.atmservice.utils.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/atm")
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class AtmDetailsController {

	@Autowired
	private AtmDetailsService atmDetailsService;
	
	
	@Autowired
	private NTicketsHistoryCEService ceService;
	
	

	@Autowired
	private TicketRaisedCategoriesByAtmService ticketRaisedCategoriesByAtmService;

	@Autowired
	private IndexOfAtmFilterService indexOfAtmFilterService;

	private AtmDetailsFromSpService atmDetailsFromSpService;
	private CeAtmMappingService ceAtmMappingService;
	private RestUtilsImpl restUtils;

	@Autowired
	private IndexOfUpDawnAtmForCmService indexOfUpDawnAtmForCmService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private TransactionSummaryService service;

	@Autowired
	private AtmDetailsPagenationService atmDetailsPagenationService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private AtmListData_forSearchService atmListData_forSearchService;
	
	@Autowired
	private IndexOFAtmTicketFilterService atmTicketFilterService;

	public AtmDetailsController(AtmDetailsService atmDetailsService, CeAtmMappingService ceAtmMapping,
			RestUtilsImpl restUtils) {
		this.atmDetailsService = atmDetailsService;
		this.ceAtmMappingService = ceAtmMapping;
		this.restUtils = restUtils;
	}

//	@PostMapping("/nticketAtmHistory")
//	@Loggable
//	public ResponseEntity<List<NTicketHistory>> getNTicketHistoryBasedOnAtmId(
//			@RequestBody TicketHistoryDto ticketHistoryReqDto) {
//		return ResponseEntity.ok(atmDetailsService.getNTicketHistoryBasedOnAtmId(ticketHistoryReqDto));
//	}
	
	@PostMapping("/nticketAtmHistory")
	@Loggable
	public ResponseEntity<List<NTicketHistory>> getNTicketHistoryBasedOnAtmId(
			@RequestBody TicketHistoryDto ticketHistoryReqDto) {
		return ResponseEntity.ok(ceService.getnTicketList(ticketHistoryReqDto));
	}

	@GetMapping("/assigned/list")
	@Loggable
	public ResponseEntity<List<AssignedAtmDto>> getAssignedAtmDetailList() {
		try {
			return ResponseEntity.ok(atmDetailsService.getAssignedAtmDetailList());
		} catch (Exception e) {
			log.error("Error while parsing data", e);
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/assigned/mtd")
	@Loggable
	public ResponseEntity<List<AssignedAtmMtdDto>> getAssignedAtmMtdDetailList() {
		try {
			return ResponseEntity.ok(atmDetailsService.getAssignedAtmMtdDetailList());
		} catch (Exception e) {
			log.error("Error while parsing data", e);
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/atmId/list")
	@Loggable
	public ResponseEntity<List<AtmIdDto>> getAtmIdList() {
		try {
			return ResponseEntity.ok(atmDetailsService.getAtmIdList());
		} catch (Exception e) {
			log.error("Error while parsing data", e);
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/getAtmDetails")
	@Loggable
	public ResponseEntity<AssignedAtmDetailsDto> getAtmDetails(@RequestBody AtmIdDto atmIdDto) {

		return ResponseEntity.ok(atmDetailsService.getAtmDetails(atmIdDto.getAtmId()));

	}

	// get these details from atm_master table
	@GetMapping("/getSearchBankLocationList")
	@Loggable
	public ResponseEntity<List<SearchBankLocationDto>> getSearchBankLocationList() {
		return ResponseEntity.ok(atmDetailsService.getSearchBankLocationList());
	}

	@GetMapping("/getSearchLocationBankList")
	@Loggable
	public ResponseEntity<List<SearchBankLocationDto>> getSearchLocationBankList() {
		return ResponseEntity.ok(atmDetailsService.getSearchLocationBankList());
	}

	@PostMapping("/atmIndentList")
	@Loggable
	public ResponseEntity<List<AtmIndent>> getAtmIndentList() {
		return ResponseEntity.ok(atmDetailsService.getAtmIndentList());
	}

	@PostMapping("/atmIndentDetails")
	@Loggable
	public ResponseEntity<List<AtmIndentDetails>> getAtmIndentDetails(@RequestBody AtmIdDto atmIdDto) {
		return ResponseEntity.ok(atmDetailsService.getAtmIndentDetails(atmIdDto.getAtmId()));
	}

	@PostMapping("/getCmCeMappingList")
	@Loggable
	public ResponseEntity<IResponseDto> getCeAtmMappingList(@RequestBody CeAtmMappingDto ceAtmMappingDto) {
		return ResponseEntity.ok(restUtils.wrapResponse(ceAtmMappingService.getCeAtmMappingList(ceAtmMappingDto),
				"CE mapped atm list returned"));

	}

	@PostMapping("/getCeAssignedAtmList")
	@Loggable
	public ResponseEntity<IResponseDto> getCeAssignedAtmList(@RequestBody CeAssignedAtmDto ceAssignedAtmDto) {

		return ResponseEntity.ok(restUtils.wrapResponse(ceAtmMappingService.getCeAssignedAtmList(ceAssignedAtmDto),
				"Ce Assigned ATM List"));
	}

	@PostMapping("/updateInActCeAtmMapping")
	@Loggable
	public ResponseEntity<IResponseDto> updateInActCeAtmMapping(
			@RequestBody InActCeAtmMappingDto inActCeAtmMappingDto) {

		return ResponseEntity.ok(restUtils
				.wrapResponse(ceAtmMappingService.updateInActCeAtmMapping(inActCeAtmMappingDto), "Ce Atm Mapped"));
	}

	@GetMapping("/getIndexOfUpDawnAtmForCm")
	@Loggable
	public ResponseEntity<List<IndexOfUpDawnAtmForCmResponse>> getIndexOfUpDawnAtmForCm() {

		return ResponseEntity.ok(indexOfUpDawnAtmForCmService.getIndexOfUpDawnAtmForCm());

	}

	@PostMapping("/get-cm-atm-details_status")
	@Loggable
	public ResponseEntity<List<CMUnderCEAtmDetailsDTO>> getCMAtmDetails(@RequestBody UserStatusDto userStatusDto) {
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = cmSynopsisService.getCMAtmDetails(userStatusDto);
		return ResponseEntity.ok(atmDetailsDTO);
	}

	@GetMapping("/Index-Of-Atm-filters")
	@Loggable
	public ResponseEntity<IndexOfAtmFilterResponseDto> getFilters() {
		return ResponseEntity.ok(indexOfAtmFilterService.getAllFilterCounts());
	}

	@PostMapping("/transaction/summary")
	@Loggable
	public ResponseEntity<TransactionSummaryResponse> getSummary(@RequestBody TransactionSummaryRequest request) {
		TransactionSummaryResponse result = service.getFormattedSummary(request.getUserType(), request.getUserId(),
				request.getDate());
		return ResponseEntity.ok(result);
	}

	@PostMapping("/atmWise-tickets/categories")
	public ResponseEntity<TicketRaisedResponseByAtm> getTicketsCategoriesByAtm(
			@RequestBody TicketRaisedCategoriesByAtmRequestDto atmRequestDto) {
		TicketRaisedResponseByAtm responseDto = ticketRaisedCategoriesByAtmService.getTicketsCategories(atmRequestDto);
		return ResponseEntity.ok(responseDto);

	}

//	@PostMapping("/CM-All-Atm-List-pagenation")
//	public ResponseEntity<PagedResponse<AtmDetailsPagnated>> getAtmDetailsByPath(
//			// @PathVariable("userId") String userId,
//			AtmDetailsPagnatedRequestDto requestDto) {
//		PagedResponse<AtmDetailsPagnated> response = atmDetailsPagenationService
//				.getPaginatedAtmDetails(requestDto.getPage(), requestDto.getSize());
//		return ResponseEntity.ok(response);
//	}

	@PostMapping("/CM-All-Atm-List-pagenation")
	public AtmDetailsPagenationResponseDTO getPagedAtms(@RequestBody AtmPaginationRequestDTO request) {
		return atmDetailsPagenationService.getAtmData(request);
	}

	@PostMapping("/search-All-Index-of-atm")
	public ResponseEntity<List<AtmListData_forSearchResponseDto>> getAllAtmAgainstFilter(
			@RequestBody AtmListData_forSearchRequestDto atmRequestDto) {
		List<AtmListData_forSearchResponseDto> responseDto = atmListData_forSearchService
				.getAllAtmAgainstFilter(atmRequestDto);
		return ResponseEntity.ok(responseDto);

	}

	@PostMapping("/Index-of-atm-tickets-filter")
	public ResponseEntity<TicketFilterMainResponseDto> getTicketsForCm(@RequestBody TicketFilterRequestDTO request) {

		log.info("Received ticket filter request for pageSize: {}, pageNumber: {}", request.getPageSize(),
				request.getPageNumber());

		try {
			TicketFilterMainResponseDto response = ticketService.getTicketsForCM(request);

			log.info("Successfully processed ticket filter request. Total records: {}, Current page: {}",
					response.getTotalRecords(), response.getCurrentPage());

			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			log.warn("Invalid request parameters: {}", e.getMessage());
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			log.error("Unexpected error processing ticket filter request", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@GetMapping("/Index-of-atm-tickets-List-options/{atmId}")
	public ResponseEntity<TicketFilterOptionsResponse> getTicketFilterOptions(@PathVariable String atmId) {
	    log.info("Fetching ticket filter options for ATM ID: {}", atmId);
	    try {
	        TicketFilterOptionsResponse response = atmTicketFilterService.getTicketFilterOptionsWithCounts(atmId);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.error("Error fetching ticket filter options for ATM ID: {}", atmId, e);
	        return ResponseEntity.internalServerError().build();
	    }
	}
}
