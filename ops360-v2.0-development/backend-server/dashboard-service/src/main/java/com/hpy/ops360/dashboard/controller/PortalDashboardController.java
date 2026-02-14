package com.hpy.ops360.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.ApiStatusResponse;
import com.hpy.ops360.dashboard.dto.AtmMtdUptimeDto;
import com.hpy.ops360.dashboard.dto.AtmUpDownCountDto;
import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto;
import com.hpy.ops360.dashboard.dto.CmSynopsisDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountResponseDto;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardDataService;
import com.hpy.ops360.dashboard.service.DashboardDatalocService;
import com.hpy.ops360.dashboard.service.DashboardService;

import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/dashboard-portal")
@CrossOrigin("${app.cross-origin.allow}")
public class PortalDashboardController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private DashboardDataService dashboardDataService;

	@Autowired
	private DashboardDatalocService dashboardDatalocService;

	@GetMapping("/currentUptime")
	@Timed
	@Loggable
	public ResponseEntity<AtmMtdUptimeDto> getCurrentUptime() {
		log.info(" ************dashboard-portal/getDashBoardDetails ");
		return ResponseEntity.ok(dashboardService.getCurrentUptime());
	}

	@GetMapping("/numberOfCes")
	@Timed
	@Loggable
	public ResponseEntity<CMPortalDashboardDto> getNumberOfCes() {
		log.info(" ************dashboard-portal/getNumberOfCes ");
		return ResponseEntity.ok(dashboardService.getNumberOfCes());
	}

	@GetMapping("/cmSynopsisDetails")
	@Timed
	@Loggable
	public ResponseEntity<List<CmSynopsisDTO>> getCmSynopsisDetails() {
		return ResponseEntity.ok(cmSynopsisService.getCmSynopsisDetails());
	}

	@GetMapping("/sync/fetch-status")
	public List<ApiStatusResponse> checkDataChanges(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		String bearerToken = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			bearerToken = authorizationHeader.substring(7); // Extracts the token part
		}

		return dashboardDataService.fetchAndCompareData(bearerToken);
	}

	@GetMapping("/fetch-status")
	public List<ApiStatusResponse> checklocDataChanges(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		String bearerToken = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			bearerToken = authorizationHeader.substring(7); // Extracts the token part
		}

		return dashboardDatalocService.fetchAndCompareData(bearerToken);
	}

	@GetMapping("/activeMachine")
	@Timed
	@Loggable
	public ResponseEntity<AtmUpDownCountDto> getCmPortalDashboard_ActiveMachine() {
		AtmUpDownCountDto dashboardData = dashboardService.getATMCountForCM();
		return new ResponseEntity<>(dashboardData, HttpStatus.OK);
	}

	@GetMapping("/counts")
	@Timed
	@Loggable
	public ResponseEntity<TicketsRaisedCountResponseDto> getTicketsRaisedCount() {
		log.info("Ticket Controller Executed");
		TicketsRaisedCountResponseDto ticketCounts = dashboardService.getTicketsRaisedCount();
		return ResponseEntity.ok(ticketCounts);
	}

}
