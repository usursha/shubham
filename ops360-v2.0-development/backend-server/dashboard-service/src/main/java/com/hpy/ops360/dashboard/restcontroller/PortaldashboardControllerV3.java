package com.hpy.ops360.dashboard.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.AtmMtdUptimeDto;
import com.hpy.ops360.dashboard.dto.AtmUpDownCountDto;
import com.hpy.ops360.dashboard.dto.CEDetailsDto;
import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto;
import com.hpy.ops360.dashboard.dto.CmSynopsisDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountResponseDto;
import com.hpy.ops360.dashboard.entity.AtmUpDownCount;
import com.hpy.ops360.dashboard.entity.TicketsRaisedCount;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.rest.dto.ResponseDto;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/dashboard-portal2")
@CrossOrigin("${app.cross-origin.allow}")
public class PortaldashboardControllerV3 {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@GetMapping("/currentUptime")
	public ResponseEntity<ResponseDto<AtmMtdUptimeDto>> getCurrentUptime() {
		log.info(" ************dashboard-portal2/getDashBoardDetails ");
		
		AtmMtdUptimeDto response=dashboardService.getCurrentUptime();
		ResponseDto<AtmMtdUptimeDto> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Successfully fetched the Current Uptime");
        formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
		
	}

	@GetMapping("/numberOfCes")
	public ResponseEntity<ResponseDto<CMPortalDashboardDto>> getNumberOfCes() {
		log.info(" ************dashboard-portal2/getNumberOfCes ");
		
		CMPortalDashboardDto response=dashboardService.getNumberOfCes();
		ResponseDto<CMPortalDashboardDto> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Successfully fetched the Number of CEs");
        formattedResponse.setData(response);
		
		return ResponseEntity.ok(formattedResponse);
	}

	@GetMapping("/activeMachine")
	public ResponseEntity<ResponseDto<AtmUpDownCountDto>> getCmPortalDashboard_ActiveMachine() {
		AtmUpDownCountDto dashboardData = dashboardService.getATMCountForCM();
		ResponseDto<AtmUpDownCountDto> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Successfully fetched the all the Up down and total Active Machine");
        formattedResponse.setData(dashboardData);
		return ResponseEntity.ok(formattedResponse);
	}

	 

}
