package com.hpy.sampatti_data_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;
import com.hpy.sampatti_data_service.logapi.Loggable;
import com.hpy.sampatti_data_service.request.AtmDto;
import com.hpy.sampatti_data_service.request.UserDto;
import com.hpy.sampatti_data_service.response.ATMDataResp;
import com.hpy.sampatti_data_service.response.CMUnderCEAtmDetailsDTO;
import com.hpy.sampatti_data_service.response.DashboardDataResp;
import com.hpy.sampatti_data_service.response.TokenResponse;
import com.hpy.sampatti_data_service.service.CmSynopsisService;
import com.hpy.sampatti_data_service.service.DateDto;
import com.hpy.sampatti_data_service.service.SampattiService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/sampatti")
@CrossOrigin("${app.cross-origin.allow}")
public class SampattiController {

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private SampattiService sampattiService;

	@Autowired
	private RestUtils restUtils;

	@PostMapping("/get-token")
	@Loggable
	public ResponseEntity<TokenResponse> getToken(@RequestBody UserDto userDto) {
		return ResponseEntity.ok(sampattiService.getToken(userDto.getUsername()));
	}

	@PostMapping("/get-dashboard-data")
	@Loggable
	public ResponseEntity<DashboardDataResp> getDashboardData(@RequestBody UserDto reqDto) {
		return ResponseEntity.ok(sampattiService.getDashboardData(reqDto.getUsername()));
	}

	@GetMapping("/get-transaction-last-updated-date-time")
	@Loggable
	public ResponseEntity<DateDto> getTransactionLastUpdatedDateTime() throws Exception {
		return ResponseEntity.ok(sampattiService.getTransactionLastUpdatedDateTime());
	}

	@PostMapping("/getatmdetailssampatti")
	@Loggable
	public ResponseEntity<ATMDataResp> getAtmDetailsSampatti(@RequestBody AtmDto reqDto) {
		log.info("Received request to fetch ATM details for username: {}", reqDto.getUserId());
		ATMDataResp response = sampattiService.getAtmData(reqDto.getUserId());
		log.info("Successfully fetched ATM details for username: {}", reqDto.getUserId());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/get_CMUnderCEAtmDetails")
	@Loggable
	public ResponseEntity<List<CMUnderCEAtmDetailsDTO>> getCMUnderCEAtmDetails(@RequestBody AtmDto reqDto) {
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = cmSynopsisService.getCMUnderCEAtmDetails(reqDto.getUserId());
		return ResponseEntity.ok(atmDetailsDTO);
	}

	@GetMapping("/get-latest-transaction-updated-date-time")
	@Loggable
	public ResponseEntity<IResponseDto> getLatestTransactionUpdatedDateTime() throws Exception {
		log.info("Inside getLatestTransactionUpdatedDateTime");
		DateDto transactionLastUpdatedDateTime = sampattiService.getTransactionLastUpdatedDateTime();
		return ResponseEntity.ok(restUtils.wrapResponse(transactionLastUpdatedDateTime, "success"));

	}

}
