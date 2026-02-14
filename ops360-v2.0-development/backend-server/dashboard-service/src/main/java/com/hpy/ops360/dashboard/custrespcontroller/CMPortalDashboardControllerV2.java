package com.hpy.ops360.dashboard.custrespcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountResponseDto;
import com.hpy.ops360.dashboard.dto.UserDto;
import com.hpy.ops360.dashboard.service.CEAtmDetailsServiceFor_Cm;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.ops360.dashboard.service.MtdUptimeService;
import com.hpy.ops360.dashboard.service.UserAtmDetailsService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v2/cm-dashboard")
@CrossOrigin("${app.cross-origin.allow}")
public class CMPortalDashboardControllerV2 {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private CEAtmDetailsServiceFor_Cm ceAtmDetailsServiceFor_Cm;

	@Autowired
	private RestUtils restUtils;

	@GetMapping("/details")
	@Timed
	public ResponseEntity<IResponseDto> getDashBoardDetails() {
		log.info("***** Inside CM getDashBoardDetails *****");
		CMPortalDashboardDto dashboardData = dashboardService.getCmPortalDashboardData();
		log.info("Data Returned successfully from getCmPortalDashboardData:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "CM Dashboard Data fetched succesfully"));
	}

	@GetMapping("/getCmSynopsisDetails")
	@Timed
	public ResponseEntity<IResponseDto> getCmSynopsisDetails() {

		log.info("***** Inside CM getCmSynopsisDetails *****");
		List<CmSynopsisDTO> dashboardData = cmSynopsisService.getCmSynopsisDetails();
		log.info("Data Returned successfully from getCmSynopsisDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "CM Dashboard Data fetched succesfully"));
	}
	
	@GetMapping("/cm-synopsis-details")
	@Timed
	public ResponseEntity<IResponseDto> getCmSynopsisDetailsV2() {
		log.info("***** Inside CM getCmSynopsisDetails *****");
		List<CmSynopsisDTO> dashboardData = cmSynopsisService.getCmSynopsisDetailsv2();
		log.info("Data Returned successfully from getCmSynopsisDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "CM Dashboard Data fetched succesfully"));
	}

	@GetMapping("/active_Machine")
	@Timed
	public ResponseEntity<IResponseDto> getCmPortalDashboard_ActiveMachine() {
		log.info("***** Inside CM getCmPortalDashboard_ActiveMachine *****");
		CMPortalDashboardDto_AtmStatusDto dashboardData = dashboardService.setAtmStatusDto_cm();
		log.info("Data Returned successfully from getCmSynopsisDetails:- " + dashboardData);
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "CM Dashboard Data fetched succesfully"));

	}

	@GetMapping("/counts")
	@Timed
	public ResponseEntity<IResponseDto> getTicketsRaisedCount() {

		log.info("Ticket Controller Executed");
		TicketsRaisedCountResponseDto ticketCounts = dashboardService.getTicketsRaisedCount();
		log.info("Data Returned successfully from getTicketsRaisedCount:- " + ticketCounts);
		return ResponseEntity.ok(restUtils.wrapResponse(ticketCounts, "CM ticketCounts Data fetched succesfully"));
	}

	@PostMapping("/update")
	public ResponseEntity<IResponseDto> updateFlagStatus(@RequestBody FlagStatusDto flagStatusDto) {
		log.info("updateFlagStatus Controller Executed");
		FlagStatusforCynopsysResponseDto response = cmSynopsisService.insertOrUpdateFlagStatus(flagStatusDto);
		log.info("Data Returned successfully from insertOrUpdateFlagStatus:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "CM insertOrUpdateFlagStatus updated succesfully"));
	}

	@PostMapping("/ce-mtd-uptime")
	@Timed
	public ResponseEntity<IResponseDto> getCeUptimeFromSp(@RequestBody UserDto userDto) {
		log.info("updateFlagStatus Controller Executed");
		CeAtmUptimeDto response = ceAtmDetailsServiceFor_Cm.getMtdUptimeFromSp(userDto.getUsername());
		log.info("Data Returned successfully from getMtdUptimeFromSp:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "CM getMtdUptimeFromSp Fetched succesfully"));
	}

}
