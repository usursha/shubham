package com.hpy.ops360.sampatti.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.request.MyScorecardIncentiveFilterRequest;
import com.hpy.ops360.sampatti.dto.response.IncentiveResponse;
import com.hpy.ops360.sampatti.dto.response.MyScorecardIncentiveFilterResponse;
import com.hpy.ops360.sampatti.service.MonthlyIncentiveService;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseListDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/portal/incentive")
@CrossOrigin("${app.cross-origin.allow}")
public class PortalIncentiveController {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private MonthlyIncentiveService incentiveService;

	@Autowired
	private RestUtilsImpl restutil;

	/*
	 * API for My-ScoreCard-filter done by shubham
	 */
	@PostMapping("/Cm-my-scorcard-list")
	public ResponseEntity<IResponseDto> getUserIncentivesByFilters(
			@RequestBody MyScorecardIncentiveFilterRequest filterRequest) {

		log.info("Controller received POST request for incentives with filters: {}", filterRequest);

		try {
			MyScorecardIncentiveFilterResponse response = incentiveService.getUserIncentivesByFilters(
					filterRequest.getYearId(), filterRequest.getMinAchieved(), filterRequest.getMaxAchieved(),
					filterRequest.getSortId());

			return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
		} catch (Exception e) {
			log.error("Error while fetching user incentives", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/*
	 * API for My-ScoreCard done by shubham
	 */
	@GetMapping("/Cm-my-scorcard-monthly-filter")
	@Operation(summary = "Get Incentive Data", description = "Retrieves monthly incentive data for a CM users")
	public ResponseEntity<IResponseDto> getIncentiveData() {
		IncentiveResponse response = incentiveService.getMyScorecardIncentiveData();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

}
