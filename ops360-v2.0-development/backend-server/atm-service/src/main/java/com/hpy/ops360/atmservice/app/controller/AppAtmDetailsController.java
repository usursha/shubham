package com.hpy.ops360.atmservice.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmMtdDto;
import com.hpy.ops360.atmservice.dto.AtmIdDto;
import com.hpy.ops360.atmservice.dto.NTicketHistory;
import com.hpy.ops360.atmservice.dto.SearchBankLocationDto;
import com.hpy.ops360.atmservice.dto.TicketHistoryDto;
import com.hpy.ops360.atmservice.logapi.Loggable;
import com.hpy.ops360.atmservice.service.AtmDetailsService;
import com.hpy.ops360.atmservice.utils.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/app/atm")
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class AppAtmDetailsController {

	@Autowired
	private AtmDetailsService atmDetailsService;

	@Autowired
	private RestUtilsImpl restUtils;

	@PostMapping("/n-ticket-atm-history")
	@Loggable
	public ResponseEntity<IResponseDto> getNTicketHistoryBasedOnAtmId(
			@RequestBody TicketHistoryDto ticketHistoryReqDto) {
		log.info("*** Inside getNTicketHistoryBasedOnAtmId");
		List<NTicketHistory> response = atmDetailsService.getNTicketHistoryBasedOnAtmId(ticketHistoryReqDto);
		log.info("Response Recieved from getNTicketHistoryBasedOnAtmId:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Get NTicket History Fetched Successfully"));
	}

	@GetMapping("/assigned/mtd")
	@Loggable
	public ResponseEntity<IResponseDto> getAssignedAtmMtdDetailList() {
		try {
			log.info("*** Inside getAssignedAtmMtdDetailList");
			List<AssignedAtmMtdDto> response = atmDetailsService.getAssignedAtmMtdDetailList();
			return ResponseEntity.ok(restUtils.wrapResponse(response, "Assigned ATM Fetched Successfully"));
		} catch (Exception e) {
			log.error("Error while parsing data", e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/assigned/list")
	@Loggable
	public ResponseEntity<IResponseDto> getAssignedAtmDetailList() {
		try {
			log.info("*** Inside getAssignedAtmDetailList");
			List<AssignedAtmDto> response = atmDetailsService.getAssignedAtmDetailList();
			return ResponseEntity.ok(restUtils.wrapResponse(response, "Get Assigned atm details fetched"));
		} catch (Exception e) {
			log.error("Error while parsing data", e);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/atmId/list")
	@Loggable
	public ResponseEntity<IResponseDto> getAtmIdList() {
		try {
			log.info("*** Inside getAtmIdList");
			List<AtmIdDto> response = atmDetailsService.getAtmIdList();
			return ResponseEntity.ok(restUtils.wrapResponse(response, "Atm ID List fetched"));
		} catch (Exception e) {
			log.error("Error while parsing data", e);
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/get-atm-details")
	@Loggable
	public ResponseEntity<IResponseDto> getAtmDetails(@RequestBody AtmIdDto atmIdDto) {
		log.info("*** Indise getAtmDetails");
		AssignedAtmDetailsDto response = atmDetailsService.getAtmDetails(atmIdDto.getAtmId());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Atm Details Fetched"));
	}
	
	@GetMapping("/get-search-bank-location-list")
	@Loggable
	public ResponseEntity<IResponseDto> getSearchBankLocationList() {
		log.info("*** Indise getSearchBankLocationList");
		List<SearchBankLocationDto> response= atmDetailsService.getSearchBankLocationList();
		return ResponseEntity.ok(restUtils.wrapResponse(response, "getSearchBankLocationList fetched"));
	}
	
	
}
