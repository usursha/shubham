package com.hpy.ops360.dashboard.controller;

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

import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto;
import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto_AtmStatusDto;
import com.hpy.ops360.dashboard.dto.CeAtmUptimeDto;
import com.hpy.ops360.dashboard.dto.CmSynopsisDTO;
import com.hpy.ops360.dashboard.dto.FlagStatusDto;
import com.hpy.ops360.dashboard.dto.FlagStatusforCynopsysResponseDto;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountResponseDto;
import com.hpy.ops360.dashboard.dto.TicketsRaisedResponseAll;
import com.hpy.ops360.dashboard.dto.UserDto;
import com.hpy.ops360.dashboard.entity.TicketsRaisedCount;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.CEAtmDetailsServiceFor_Cm;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.ops360.dashboard.service.MtdUptimeService;
import com.hpy.ops360.dashboard.service.TicketsRaisedService;
import com.hpy.ops360.dashboard.service.UserAtmDetailsService;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/cm-dashboard")
@CrossOrigin("${app.cross-origin.allow}")
public class CMPortalDashboardController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private CEAtmDetailsServiceFor_Cm ceAtmDetailsServiceFor_Cm;

	@Autowired
	private TicketsRaisedService ticketsRaisedService;

	@GetMapping("/details")
	@Timed
	@Loggable
	public ResponseEntity<CMPortalDashboardDto> getDashBoardDetails() {
		log.info("1) ************getDashBoardDetails hit");
		return ResponseEntity.ok(dashboardService.getCmPortalDashboardData());
		// --I am going to use the class of raised ticketing microservice (
		// getTicketsRaisedCount )

	}

	@GetMapping("/getCmSynopsisDetails")
	@Timed
	@Loggable
	public ResponseEntity<List<CmSynopsisDTO>> getCmSynopsisDetails() {
		return ResponseEntity.ok(cmSynopsisService.getCmSynopsisDetails());
	}

	@GetMapping("/active_Machine")
	@Timed
	@Loggable
	public ResponseEntity<CMPortalDashboardDto_AtmStatusDto> getCmPortalDashboard_ActiveMachine() {
		CMPortalDashboardDto_AtmStatusDto dashboardData = dashboardService.setAtmStatusDto_cm();
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

	@PostMapping("/update")
	@Loggable
	public ResponseEntity<FlagStatusforCynopsysResponseDto> updateFlagStatus(@RequestBody FlagStatusDto flagStatusDto) {
		FlagStatusforCynopsysResponseDto response = cmSynopsisService.insertOrUpdateFlagStatus(flagStatusDto);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/ce-mtd-uptime")
	@Timed
	@Loggable
	public ResponseEntity<CeAtmUptimeDto> getCeUptimeFromSp(@RequestBody UserDto userDto) {
		return ResponseEntity.ok(ceAtmDetailsServiceFor_Cm.getMtdUptimeFromSp(userDto.getUsername()));
	}

	@GetMapping("/tickets/raised/categories")
	public ResponseEntity<TicketsRaisedResponseAll> getRaisedTicketsCategories() {
		TicketsRaisedResponseAll response = ticketsRaisedService.getTicketCategoriesForUser();
		return ResponseEntity.ok(response);

	}
}
