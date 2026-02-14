package com.hpy.ops360.dashboard.custrespcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.AtmMtdUptimeDto;
import com.hpy.ops360.dashboard.dto.AtmStatusDtoForCm;
import com.hpy.ops360.dashboard.dto.AtmUpDownCountDto;
import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v2/dashboard-portal")
@CrossOrigin("${app.cross-origin.allow}")
public class PortaldashboardControllerV2 {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;
	
	@Autowired
	private RestUtils restUtils;

	@GetMapping("/currentUptime")
	public ResponseEntity<IResponseDto> getCurrentUptime() {
		log.info(" ************dashboard-portal2/getDashBoardDetails ");
		AtmMtdUptimeDto response=dashboardService.getCurrentUptime();
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully fetched the Current Uptime"));
	}

	@GetMapping("/numberOfCes")
	public ResponseEntity<IResponseDto> getNumberOfCes() {
		log.info(" ************dashboard-portal2/getNumberOfCes ");
		CMPortalDashboardDto response=dashboardService.getNumberOfCes();
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully fetched the Number of CEs"));
	}

	@GetMapping("/activeMachine")
	public ResponseEntity<IResponseDto> getCmPortalDashboard_ActiveMachine() {
		AtmUpDownCountDto dashboardData = dashboardService.getATMCountForCM();
		return ResponseEntity.ok(restUtils.wrapResponse(dashboardData, "Successfully fetched the all the Up down and total Active Machine"));
	}

	@GetMapping("/up-down-atmCount-forCE/{CeUserId}")
	@Loggable
	public ResponseEntity<IResponseDto> getAtmStatus(@PathVariable String CeUserId) {
		try {

			AtmStatusDtoForCm response = dashboardService.getAtmStatus(CeUserId);
			return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully fetched data for Up Down Count against CE"));

		} catch (Exception e) {
			log.error("Error getting ATM status: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


}