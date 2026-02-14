package com.hpy.ops360.dashboard.custrespcontroller;

import java.io.IOException;
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
import com.hpy.ops360.dashboard.dto.AtmTicketEventDto;
import com.hpy.ops360.dashboard.dto.AtmTicketsEventDto;
import com.hpy.ops360.dashboard.dto.CeAtmUptimeDto;
import com.hpy.ops360.dashboard.dto.DashboardDataDto;
import com.hpy.ops360.dashboard.dto.DashboardFlagDto;
import com.hpy.ops360.dashboard.dto.NotificationDto;
import com.hpy.ops360.dashboard.dto.OpenTicketsResponse;
import com.hpy.ops360.dashboard.dto.SiteVisitsDto;
import com.hpy.ops360.dashboard.dto.SynergyLoginResponse;
import com.hpy.ops360.dashboard.dto.TicketsRaisedResponseAll;
import com.hpy.ops360.dashboard.dto.UserAtmDetailsDto;
import com.hpy.ops360.dashboard.dto.UsersAtmDetailsDto;
import com.hpy.ops360.dashboard.service.AtmDetailsService;
import com.hpy.ops360.dashboard.service.AtmTicketEventService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.ops360.dashboard.service.SiteVisitService;
import com.hpy.ops360.dashboard.service.TicketsRaisedService;
import com.hpy.ops360.dashboard.service.UserAtmDetailsService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/dashboard")
@CrossOrigin("${app.cross-origin.allow}")
@AllArgsConstructor
@Slf4j
public class DashboardControllerV2 {

	private DashboardService dashboardService;

	private UserAtmDetailsService userAtmDetailsService;

	private AtmTicketEventService atmTicketEventService;

	private SiteVisitService siteVisitService;

	private AtmDetailsService atmDetailsService;

	private RestUtils restUtils;
	
	@Autowired
	private TicketsRaisedService ticketsRaisedService;

	@GetMapping("/details/task-summary")
	public ResponseEntity<IResponseDto> getDashboardData() {
		log.info("***** Inside getDashBoardDetails *****");
		DashboardDataDto dashboardData = dashboardService.getDashboardDataWithStringWithUptime();
		log.info("Data Returned successfully from getDashboardDataWithStringWithUptime:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "Dashboard Data fetched succesfully"));
	}

	@PostMapping("/insert-opentickets")
	public ResponseEntity<IResponseDto> insertOpenTicketList(@RequestBody List<AtmDetailsDto> atms) {
		log.info("***** Inside insertOpenTicketList *****");
		String dashboardData = dashboardService.insertOpenTicketList(atms);
		log.info("Data Returned successfully from insertOpenTicketList:- " + dashboardData);
		return ResponseEntity
				.ok(restUtils.wrapResponse(dashboardData, "Open Ticket List has been inserted succesfully"));
	}

//	@GetMapping("/notification")
//	@Timed
//	public ResponseEntity<IResponseDto> getNotificationDetails() throws IOException {
//
//		log.info("***** Inside getNotificationDetails *****");
//		List<NotificationDto> dashboardData = dashboardService.getNotificationDetails();
//		log.info("Data Returned successfully from getNotificationDetails:- " + dashboardData);
//		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "Notification Data fetched succesfully"));
//	}
	

	@GetMapping("/trequest")
	@Timed
	public ResponseEntity<SynergyLoginResponse> getSynergyRequestId() {
		return ResponseEntity.ok(dashboardService.getSynergyRequestId());

	}

	@PostMapping("/getopentickets")
	@Timed
	public ResponseEntity<IResponseDto> getOpenTicketDetails(@RequestBody List<AtmDetailsDto> atms) {
		log.info("***** Inside getOpenTicketDetails *****");
		OpenTicketsResponse dashboardData = dashboardService.getOpenTicketDetails(atms);
		log.info("Data Returned successfully from getOpenTicketDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "OpenTicketDetails Data fetched succesfully"));
	}

	@PostMapping("/userAtmDetails")
	@Timed
	public ResponseEntity<IResponseDto> getUserAtmDetails(@RequestBody UserAtmDetailsDto userAtmDetailsDto) {

		log.info("***** Inside getOpenTicketDetails *****");
		List<UsersAtmDetailsDto> dashboardData = userAtmDetailsService
				.getUserAtmDetails(userAtmDetailsDto.getUser_login_id());
		log.info("Data Returned successfully from getOpenTicketDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "OpenTicketDetails Data fetched succesfully"));
	}

	@PostMapping("/atmTicketEvent")
	@Timed
	public ResponseEntity<IResponseDto> getAtmTicketEventDetails(@RequestBody AtmTicketEventDto atmTicketEventDto) {

		log.info("***** Inside getAtmTicketEventDetails *****");
		List<AtmTicketsEventDto> dashboardData = atmTicketEventService
				.getAtmTicketEvent(atmTicketEventDto.getAtmTicketEventCode());
		log.info("Data Returned successfully from getAtmTicketEventDetails:- " + dashboardData);
		return ResponseEntity
				.ok(restUtils.wrapResponse(dashboardData, "AtmTicketEventDetails Data fetched succesfully"));
	}

	@PostMapping("/flag/{minutes}")
	@Timed
	public ResponseEntity<IResponseDto> getDashboardRefreshFlag(@PathVariable int minutes) {

		log.info("***** Inside getDashboardRefreshFlag *****");
		DashboardFlagDto dashboardData = dashboardService.getDashboardRefreshFlag(minutes);
		log.info("Data Returned successfully from getDashboardRefreshFlag:- " + dashboardData);
		return ResponseEntity
				.ok(restUtils.wrapResponse(dashboardData, "DashboardRefreshFlag Data fetched succesfully"));
	}

	@GetMapping("/site-visit-details")
	@Timed
	public ResponseEntity<IResponseDto> getSiteVisitDetails() {

		log.info("***** Inside getSiteVisitDetails *****");
		List<SiteVisitsDto> dashboardData = siteVisitService.getSiteVisitDetails();
		log.info("Data Returned successfully from getSiteVisitDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "SiteVisitDetails Data fetched succesfully"));
	}

	@PostMapping("/atmIndentDetails")
	@Timed
	public ResponseEntity<IResponseDto> getAtmIndentDetails(@RequestBody AtmDetailsDto atmIdDto) {

		log.info("***** Inside getAtmIndentDetails *****");
		List<AtmIndentDetailsDto> dashboardData = atmDetailsService.getAtmIndentDetails(atmIdDto.getAtmid());
		log.info("Data Returned successfully from getAtmIndentDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "AtmIndentDetails Data fetched succesfully"));
	}

	@GetMapping("/mtdUptime")
	@Timed
	public ResponseEntity<IResponseDto> getCeUptimeFromSp() {
		log.info("***** Inside getCeUptimeFromSp *****");
		CeAtmUptimeDto dashboardData = dashboardService.getCeUptimeFromSp();
		log.info("Data Returned successfully from getCeUptimeFromSp:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "AtmIndentDetails Data fetched succesfully"));
	}

	
	@GetMapping("/tickets/raised/categories")
	public ResponseEntity<IResponseDto> getRaisedTicketsCategories() {
		TicketsRaisedResponseAll response = ticketsRaisedService.getTicketCategoriesForUser();		
		return ResponseEntity.ok(restUtils.wrapResponse(response,"Tickets Raised categories fetched sucessfully "));

	}
	
}
