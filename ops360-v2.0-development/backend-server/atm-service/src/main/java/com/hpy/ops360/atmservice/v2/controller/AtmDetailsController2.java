package com.hpy.ops360.atmservice.v2.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmMtdDto;
import com.hpy.ops360.atmservice.dto.AtmIdDto;
import com.hpy.ops360.atmservice.dto.AtmListData_forSearchRequestDto;
import com.hpy.ops360.atmservice.dto.AtmListData_forSearchResponseDto;
import com.hpy.ops360.atmservice.dto.AtmMtdResDto;
import com.hpy.ops360.atmservice.dto.AtmUpDownStatusAndDownTimeDto;
import com.hpy.ops360.atmservice.dto.BankNameAndAtmUpTimeDto;
import com.hpy.ops360.atmservice.dto.CEWiseBankListMtdUptimeResDto2;
import com.hpy.ops360.atmservice.dto.CMUnderCEAtmDetailsDTO;
import com.hpy.ops360.atmservice.dto.CMWiseBankListMtdUptimeResDto2;
import com.hpy.ops360.atmservice.dto.CeAssignedAtmDto;
import com.hpy.ops360.atmservice.dto.CeAtmMappingDto;
import com.hpy.ops360.atmservice.dto.CeWiseBankListMtdUptimeReqDto;
import com.hpy.ops360.atmservice.dto.InActCeAtmMappingDto;
import com.hpy.ops360.atmservice.dto.IndexOfAtmFilterResponseDto;
import com.hpy.ops360.atmservice.dto.IndexOfUpDawnAtmForCmResponse;
import com.hpy.ops360.atmservice.dto.NTicketHistory;
import com.hpy.ops360.atmservice.dto.SearchBankLocationDto;
import com.hpy.ops360.atmservice.dto.TicketDetailDto;
import com.hpy.ops360.atmservice.dto.TicketFilterMainResponseDto;
import com.hpy.ops360.atmservice.dto.TicketFilterOptionsResponse;
import com.hpy.ops360.atmservice.dto.TicketFilterRequestDTO;
import com.hpy.ops360.atmservice.dto.TicketHistoryDto;
import com.hpy.ops360.atmservice.dto.TicketSearchRequestDto;
import com.hpy.ops360.atmservice.dto.TransactionSummaryRequest;
import com.hpy.ops360.atmservice.dto.TransactionSummaryResponse;
import com.hpy.ops360.atmservice.dto.UserStatusDto;
import com.hpy.ops360.atmservice.entity.AtmIndent;
import com.hpy.ops360.atmservice.entity.AtmIndentDetails;
import com.hpy.ops360.atmservice.logapi.Loggable;
import com.hpy.ops360.atmservice.request.AtmMtdReqDto;
import com.hpy.ops360.atmservice.request.AtmPaginationRequestDTO;
import com.hpy.ops360.atmservice.request.CeWiseAtmPaginationRequestDTO;
import com.hpy.ops360.atmservice.request.CeWiseAtmSearchRequestDTO;
import com.hpy.ops360.atmservice.request.TicketRaisedCategoriesByAtmRequestDto;
import com.hpy.ops360.atmservice.response.AtmDetailsPagenationResponseDTO;
import com.hpy.ops360.atmservice.response.CeWiseAtmDetailsPagenationResponseDTO;
import com.hpy.ops360.atmservice.response.TicketRaisedResponseByAtm;
import com.hpy.ops360.atmservice.service.AtmDetailsFromSpService;
import com.hpy.ops360.atmservice.service.AtmDetailsPagenationService;
import com.hpy.ops360.atmservice.service.AtmDetailsService;
import com.hpy.ops360.atmservice.service.AtmListData_forSearchService;
import com.hpy.ops360.atmservice.service.AtmMtdService;
import com.hpy.ops360.atmservice.service.AtmUpDownStatusAndDownTimeService;
import com.hpy.ops360.atmservice.service.BankNameAndAtmUpTimeService;
import com.hpy.ops360.atmservice.service.CEWiseBankListMtdUptimeService;
import com.hpy.ops360.atmservice.service.CMWiseBankListMtdUptimeService;
import com.hpy.ops360.atmservice.service.CeAtmMappingService;
import com.hpy.ops360.atmservice.service.CeBankWiseMTDService;
import com.hpy.ops360.atmservice.service.CewiseallBanksService;
import com.hpy.ops360.atmservice.service.CmSynopsisService;
import com.hpy.ops360.atmservice.service.IndexOFAtmTicketFilterService;
import com.hpy.ops360.atmservice.service.IndexOfAtmFilterService;
import com.hpy.ops360.atmservice.service.IndexOfUpDawnAtmForCmService;
import com.hpy.ops360.atmservice.service.NTicketsHistoryCEService;
import com.hpy.ops360.atmservice.service.SearchTicketsDetailsrService;
import com.hpy.ops360.atmservice.service.TicketRaisedCategoriesByAtmService;
import com.hpy.ops360.atmservice.service.TicketService;
import com.hpy.ops360.atmservice.service.TransactionSummaryService;
import com.hpy.ops360.atmservice.utils.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/atm")
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class AtmDetailsController2 {

	@Autowired
	private AtmDetailsService atmDetailsService;

	@Autowired
	private IndexOfAtmFilterService indexOfAtmFilterService;

	@Autowired
	private AtmDetailsFromSpService atmDetailsFromSpService;
	private CeAtmMappingService ceAtmMappingService;
	private RestUtilsImpl restUtils;

	@Autowired
	private CewiseallBanksService cewiseallbanksservice;

	@Autowired
	private IndexOfUpDawnAtmForCmService indexOfUpDawnAtmForCmService;

	@Autowired
	private CeBankWiseMTDService cebankWiseMTDService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private TransactionSummaryService service;

	@Autowired
	private BankNameAndAtmUpTimeService bankNameAndAtmUpTimeService;

	@Autowired
	private AtmUpDownStatusAndDownTimeService atmUpDownStatusAndDownTimeService;

	@Autowired
	private AtmDetailsPagenationService atmDetailsPagenationService;

	@Autowired
	private CMWiseBankListMtdUptimeService cmWiseBankListMtdUptimeService;
	
	@Autowired
	private CEWiseBankListMtdUptimeService ceWiseBankListMtdUptimeService;

	@Autowired
	private TicketRaisedCategoriesByAtmService ticketRaisedCategoriesByAtmService;

	@Autowired
	private AtmListData_forSearchService atmListData_forSearchService;

	@Autowired
	private IndexOFAtmTicketFilterService atmTicketFilterService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private SearchTicketsDetailsrService detailsrService;

	@Autowired
	private NTicketsHistoryCEService ceService;

	@Autowired
	private AtmMtdService atmMtdService;

	public AtmDetailsController2(AtmDetailsService atmDetailsService, CeAtmMappingService ceAtmMapping,
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

//	@GetMapping("/Index-Of-Atm-filters")
//	@Loggable
//	public ResponseEntity<IndexOfAtmFilterResponseDto> getFilters() {
//		return ResponseEntity.ok(indexOfAtmFilterService.getFilters());
//	}

//	@GetMapping("/Index-Of-Atm-filters")
//	@Loggable
//	public ResponseEntity<IResponseDto> getFilters() {
//		IndexOfAtmFilterResponseDto response = indexOfAtmFilterService.getFilters();
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "successfull"));
//	}

	@PostMapping("/transaction/summary")
	@Loggable
	public ResponseEntity<IResponseDto> getSummary(@RequestBody TransactionSummaryRequest request) {
		TransactionSummaryResponse result = service.getFormattedSummary(request.getUserType(), request.getUserId(),
				request.getDate());
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Transaction Summary data fetched Successfully"));
	}

//	@PostMapping("/allBanks")
//	@Loggable
//	   public ResponseEntity<List<allBanksResponsedto>> getallBankList(@RequestBody allBanksdto req) {
//        List<allBanksResponsedto> result = allbanksservice.getAllBanksList(req.getCmUserId());
//        return ResponseEntity.ok(result);
//    }

//	@PostMapping("/bankwise/mtduptime")
//	@Loggable
//	public ResponseEntity<IResponseDto> getBankWiseMTD(@RequestBody BankWiseMTDDto request) {
//		BankWiseMTDResponseDto result = bankWiseMTDService.getBankWiseMTD(request.getCmUserId(), request.getBankName());
//		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull"));
//	}

//	@PostMapping("/ce-bank-wise-mtd-uptime-avg")
//	@Loggable
//
//	   public ResponseEntity<List<allBanksResponsedto>> getallBankList(@RequestBody allBanksdto req) {
//
//	   //public ResponseEntity<List<allBanksResponsedto>> getCewiseallBankList(@RequestBody allBanksdto req) {
//
//        List<allBanksResponsedto> result = allbanksservice.getAllBanksList(req.getCmUserId());
//        return ResponseEntity.ok(result);
//    }

//	@PostMapping("/ce-wise-allBanks")
//	@Loggable
//	public ResponseEntity<List<CewiseallBanksResponseDto>> getcewiseBanksList(@RequestBody CewiseallBanksDto req) {
//		List<CewiseallBanksResponseDto> result = cewiseallbanksservice.getcewiseBanksList(req.getCeUserId());
//		return ResponseEntity.ok(result);
//	}
//
//	@PostMapping("/CeBankWiseMTD")
//	public ResponseEntity<IResponseDto> getCeBankWiseMTD(@RequestBody CeBankWiseMTDDto request) {
//		CeBankWiseMTDResponseDto result = cebankWiseMTDService.getCeBankWiseMTD(request.getCeUserId(),
//				request.getBankName());
//		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull"));
//	}
	
	@PostMapping("/CEWiseBankListMtdUptime")
	public ResponseEntity<IResponseDto> getCeWiseBankListMtdUptime(@RequestBody CeWiseBankListMtdUptimeReqDto request) {
		CEWiseBankListMtdUptimeResDto2 result = ceWiseBankListMtdUptimeService.getCEWiseData(request);
		return ResponseEntity
				.ok(restUtils.wrapResponse(result, "Successfully fetched CM-wise bank list with MTD uptime."));
	}

	@GetMapping("/getBankNameAndAtmUpTime/{atmId}")
	@Loggable
	public ResponseEntity<IResponseDto> getBankNameAndAtmUpTime(@PathVariable String atmId) {
		BankNameAndAtmUpTimeDto result = bankNameAndAtmUpTimeService.getBankNameAndAtmUpTime(atmId);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull"));
	}

	@GetMapping("/getAtmUpDownStatusAndDownTime/{atmId}")
	public ResponseEntity<IResponseDto> getAtmUpDownStatusAndDownTime(@PathVariable String atmId) {
		AtmUpDownStatusAndDownTimeDto result = atmUpDownStatusAndDownTimeService.fetchAtmUpdownStatus(atmId);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull"));
	}

	@GetMapping("/Index-Of-Atm-filters")
	@Loggable
	public ResponseEntity<IResponseDto> getFilters() {
		IndexOfAtmFilterResponseDto result = indexOfAtmFilterService.getAllFilterCounts();
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull"));
	}

	@PostMapping("/CM-All-Atm-List-pagenation")
	public ResponseEntity<IResponseDto> getAtmDetailsByPath(@RequestBody AtmPaginationRequestDTO request) {

		AtmDetailsPagenationResponseDTO response = atmDetailsPagenationService.getAtmData(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Sucesssfull data fetched..."));
	}

//	@PostMapping("/CMWiseBankListMtdUptime")
//	public ResponseEntity<IResponseDto> getCMWiseBankListMtdUptime(@RequestBody CMWiseBankListMtdUptimeReqDto request) {
//		List<CMWiseBankListMtdUptimeResDto> result = cmWiseBankListMtdUptimeService.getCMWiseData(request);
//		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull"));
//	}

	@GetMapping("/CMWiseBankListMtdUptime")
	public ResponseEntity<IResponseDto> getCMWiseBankListMtdUptime() {
		CMWiseBankListMtdUptimeResDto2 result = cmWiseBankListMtdUptimeService.getCMWiseData();
		return ResponseEntity
				.ok(restUtils.wrapResponse(result, "Successfully fetched CM-wise bank list with MTD uptime."));
	}

	@PostMapping("/singleAtmWise-tickets/categories")
	public ResponseEntity<IResponseDto> getTicketsCategoriesByAtm(
			@RequestBody TicketRaisedCategoriesByAtmRequestDto atmRequestDto) {
		TicketRaisedResponseByAtm responseDto = ticketRaisedCategoriesByAtmService.getTicketsCategories(atmRequestDto);
		// return ResponseEntity.ok(responseDto);
		return ResponseEntity
				.ok(restUtils.wrapResponse(responseDto, "Sucesssfull Fetched Atm Wise Ticket Raised Categories"));

	}

	@PostMapping("/search-All-Index-of-atm")
	public ResponseEntity<IResponseDto> getAllAtmAgainstFilter(
			@RequestBody AtmListData_forSearchRequestDto atmRequestDto) {
		List<AtmListData_forSearchResponseDto> result = atmListData_forSearchService
				.getAllAtmAgainstFilter(atmRequestDto);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucesssfull Fetched All Atm index"));
	}

	@PostMapping("/atm-mtd")
	public ResponseEntity<IResponseDto> getConfirmationRequired(@RequestBody AtmMtdReqDto reqDto) {
		List<AtmMtdResDto> result = atmMtdService.getAtmMTD(reqDto);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}

	@PostMapping("/Index-of-atm-tickets-filter")
	public ResponseEntity<IResponseDto> getTicketsForCm(@RequestBody TicketFilterRequestDTO request) {

		log.info("Received ticket filter request for pageSize: {}, pageNumber: {}", request.getPageSize(),
				request.getPageNumber());

		try {
			TicketFilterMainResponseDto response = ticketService.getTicketsForCM(request);

			log.info("Successfully processed ticket filter request. Total records: {}, Current page: {}",
					response.getTotalRecords(), response.getCurrentPage());

			return ResponseEntity.ok(restUtils.wrapResponse(response, "Success"));

		} catch (IllegalArgumentException e) {
			log.warn("Invalid request parameters: {}", e.getMessage());
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			log.error("Unexpected error processing ticket filter request", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/Index-of-atm-tickets-List-options/{atmId}")
	public ResponseEntity<IResponseDto> getTicketFilterOptions(@PathVariable String atmId) {
		log.info("Fetching ticket filter options for ATM ID: {}", atmId);
		try {
			TicketFilterOptionsResponse response = atmTicketFilterService.getTicketFilterOptionsWithCounts(atmId);
			return ResponseEntity.ok(restUtils.wrapResponse(response, "Success"));
		} catch (Exception e) {
			log.error("Error fetching ticket filter options for ATM ID: {}", atmId, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PostMapping("/search-list-Index-of-atm-tickets")
	@Loggable
	public ResponseEntity<IResponseDto> getSearchListOfTicketDetailsList(
			@RequestBody TicketSearchRequestDto requestdto) {
		List<TicketDetailDto> response = detailsrService.getTicketSearchDetails(requestdto);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Success"));
	}
	
	@PostMapping("/all-CE-atm-list-pagenation")
	public ResponseEntity<IResponseDto> getAllCEAtmListpagenation(@RequestBody CeWiseAtmPaginationRequestDTO request) {
		log.info("******* Inside getAllCEAtmListpagenation Method *********");
		log.info("Request Recieved: " + request);
		CeWiseAtmDetailsPagenationResponseDTO ceWiseAtmDetailsData = atmDetailsPagenationService.getCeWiseAtmDetailsData(request);
		return ResponseEntity.ok(restUtils.wrapResponse(ceWiseAtmDetailsData, "Success"));
	}
	
	@PostMapping("/search-all-CE-atm-list")
	public ResponseEntity<IResponseDto> getSearchAllCeAtmData(@RequestBody CeWiseAtmSearchRequestDTO request) {
		log.info("******* Inside getSearchAllCeAtmData Method *********");
		log.info("Request Recieved: " + request);
		List<String> ceWiseAtmSearchData = atmDetailsPagenationService.getCeWiseAtmSearchData(request);
		return ResponseEntity.ok(restUtils.wrapResponseListOfString(ceWiseAtmSearchData, "Success"));
	}
}