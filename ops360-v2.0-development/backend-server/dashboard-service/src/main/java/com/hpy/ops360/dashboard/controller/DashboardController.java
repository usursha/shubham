package com.hpy.ops360.dashboard.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.hpy.ops360.dashboard.dto.AtmStatusDtoForCm;
import com.hpy.ops360.dashboard.dto.AtmTicketEventDto;
import com.hpy.ops360.dashboard.dto.AtmTicketsEventDto;
import com.hpy.ops360.dashboard.dto.CeAtmUptimeDto;
import com.hpy.ops360.dashboard.dto.DashboardDataDto;
import com.hpy.ops360.dashboard.dto.DashboardFlagDto;
import com.hpy.ops360.dashboard.dto.NotificationDto;
import com.hpy.ops360.dashboard.dto.SiteVisitsDto;
import com.hpy.ops360.dashboard.dto.SynergyLoginResponse;
import com.hpy.ops360.dashboard.dto.UserAtmDetailsDto;
import com.hpy.ops360.dashboard.dto.UsersAtmDetailsDto;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.AtmDetailsService;
import com.hpy.ops360.dashboard.service.AtmTicketEventService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.ops360.dashboard.service.SiteVisitService;
import com.hpy.ops360.dashboard.service.UserAtmDetailsService;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("${app.cross-origin.allow}")
@AllArgsConstructor
@Slf4j
public class DashboardController {

	private DashboardService dashboardService;

	private UserAtmDetailsService userAtmDetailsService;

	private AtmTicketEventService atmTicketEventService;

	private SiteVisitService siteVisitService;

	private AtmDetailsService atmDetailsService;

	@GetMapping("/details")
	@Loggable
	public ResponseEntity<DashboardDataDto> getDashboardData() {
		log.info("*** Inside DashboardController ***");
		return ResponseEntity.ok(dashboardService.getDashboardDetails());
	}

	@GetMapping("/up-down-atmCount-forCE/{CeUserId}")
	@Loggable
	public ResponseEntity<AtmStatusDtoForCm> getAtmStatus(@PathVariable String CeUserId) {
		try {
			AtmStatusDtoForCm status = dashboardService.getAtmStatusWithHims(CeUserId);
			return ResponseEntity.ok(status);
		} catch (Exception e) {
			log.error("Error getting ATM status: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/insert-opentickets")
	@Loggable
	public ResponseEntity<String> insertOpenTicketList(@RequestBody List<AtmDetailsDto> atms) {
		return ResponseEntity.ok(dashboardService.insertOpenTicketList(atms));
	}

	@GetMapping("/notification")
	@Timed
	@Loggable
	public ResponseEntity<List<NotificationDto>> getNotificationDetails() {

		try {
			return ResponseEntity.ok(dashboardService.getNotificationDetails());
		} catch (IOException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/trequest")
	@Timed
	@Loggable
	public ResponseEntity<SynergyLoginResponse> getSynergyRequestId() {
		return ResponseEntity.ok(dashboardService.getSynergyRequestId());

	}

	@PostMapping("/getopentickets")
	@Timed
	@Loggable
	public ResponseEntity<Object> getOpenTicketDetails(@RequestBody List<AtmDetailsDto> atms) {
		return ResponseEntity.ok(dashboardService.getOpenTicketDetails(atms));
	}

	@PostMapping("/userAtmDetails")
	@Timed
	@Loggable
	public ResponseEntity<List<UsersAtmDetailsDto>> getUserAtmDetails(
			@RequestBody UserAtmDetailsDto userAtmDetailsDto) {
		return ResponseEntity.ok(userAtmDetailsService.getUserAtmDetails(userAtmDetailsDto.getUser_login_id()));
	}

	@PostMapping("/atmTicketEvent")
	@Timed
	@Loggable
	public ResponseEntity<List<AtmTicketsEventDto>> getAtmTicketEventDetails(
			@RequestBody AtmTicketEventDto atmTicketEventDto) {

		return ResponseEntity.ok(atmTicketEventService.getAtmTicketEvent(atmTicketEventDto.getAtmTicketEventCode()));
	}

	@PostMapping("/flag/{minutes}")
	@Timed
	@Loggable
	public ResponseEntity<DashboardFlagDto> getDashboardRefreshFlag(@PathVariable int minutes) {
		return ResponseEntity.ok(dashboardService.getDashboardRefreshFlag(minutes));
	}

	@GetMapping("/site-visit-details")
	@Timed
	@Loggable
	public ResponseEntity<List<SiteVisitsDto>> getSiteVisitDetails() {
		return ResponseEntity.ok(siteVisitService.getSiteVisitDetails());
	}

	@PostMapping("/atmIndentDetails")
	@Timed
	@Loggable
	public ResponseEntity<List<AtmIndentDetailsDto>> getAtmIndentDetails(@RequestBody AtmDetailsDto atmIdDto) {
		return ResponseEntity.ok(atmDetailsService.getAtmIndentDetails(atmIdDto.getAtmid()));
	}

	@GetMapping("/mtdUptime")
	@Timed
	@Loggable
	public ResponseEntity<CeAtmUptimeDto> getCeUptimeFromSp() {
		log.info("*** Inside getCeUptimeFromSp ****");
		return ResponseEntity.ok(dashboardService.getCeUptimeFromSp());
	}

}
