package com.hpy.ops360.dashboard.V2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.AtmDetailsDto;
import com.hpy.ops360.dashboard.dto.AtmIndentDetailsDto;
import com.hpy.ops360.dashboard.dto.CEDetailsDto;
import com.hpy.ops360.dashboard.dto.DashboardDataDto;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.AtmDetailsService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/app/dashboard")
@CrossOrigin("${app.cross-origin.allow}")
@AllArgsConstructor
@Slf4j
public class AppDashboardV2Controller {

	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private AtmDetailsService atmDetailsService;
	
	@Autowired
	private RestUtils restUtils;
	
	@GetMapping("/details")
	@Loggable
	public ResponseEntity<IResponseDto> getDashboardData() {
		log.info("*** Inside DashboardController ***");
		DashboardDataDto response=dashboardService.getDashboardDataWithStringWithUptime();
		log.info("Response Recieved from DashboardController:- "+ response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Dashboard Data Fetched Successfully"));
	}
	
	@PostMapping("/atm-indent-details")
	@Timed
	@Loggable
	public ResponseEntity<IResponseDto> getAtmIndentDetails(@RequestBody AtmDetailsDto atmIdDto) {
		log.info("*** Inside getAtmIndentDetails ***");
		List<AtmIndentDetailsDto> response=atmDetailsService.getAtmIndentDetails(atmIdDto.getAtmid());
		log.info("Response Recieved from getAtmIndentDetails:- "+ response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "ATM Indent Details fetched Successfully"));
	}
	
}
