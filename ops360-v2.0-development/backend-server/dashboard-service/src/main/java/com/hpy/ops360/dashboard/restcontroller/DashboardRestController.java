package com.hpy.ops360.dashboard.restcontroller;

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
import com.hpy.ops360.dashboard.dto.CmSynopsisDTOListWrapper;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountResponseDto;
import com.hpy.ops360.dashboard.dto.UserDto;
import com.hpy.ops360.dashboard.service.CEAtmDetailsServiceFor_Cm;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.ops360.dashboard.service.MtdUptimeService;
import com.hpy.ops360.dashboard.service.UserAtmDetailsService;
import com.hpy.ops360.dashboard.util.ResponseMessageUtil;
import com.hpy.rest.dto.ResponseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/cm-dashboard2")
@CrossOrigin("${app.cross-origin.allow}")
public class DashboardRestController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private UserAtmDetailsService atmDetailsService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private MtdUptimeService mtdUptimeService;

	@Autowired
	private CEAtmDetailsServiceFor_Cm ceAtmDetailsServiceFor_Cm;

	@GetMapping("/details")
	public ResponseEntity<ResponseDto<CMPortalDashboardDto>> getDashBoardDetails() {

		ResponseDto<CMPortalDashboardDto> formattedResponse = new ResponseDto<>();

		CMPortalDashboardDto dashboardData = dashboardService.getCmPortalDashboardData();

		int statusCodeValue = HttpStatus.OK.value();
		String statusMessage = ResponseMessageUtil.getMessageForStatusCode(statusCodeValue);

		formattedResponse.setResponseCode(statusCodeValue);
		formattedResponse.setMessage(statusMessage);
		formattedResponse.setData(dashboardData);

		return ResponseEntity.ok(formattedResponse);
	}

	@GetMapping("/getCmSynopsisDetails")
	public ResponseEntity<ResponseDto<CmSynopsisDTOListWrapper>> getCmSynopsisDetails() {
		ResponseDto<CmSynopsisDTOListWrapper> formattedResponse = new ResponseDto<CmSynopsisDTOListWrapper>();
		List<CmSynopsisDTO> response = cmSynopsisService.getCmSynopsisDetails();

		CmSynopsisDTOListWrapper responseWrapper = new CmSynopsisDTOListWrapper();
		responseWrapper.setCmSynopsisDTOList(response);
		int statusCodeValue = HttpStatus.OK.value();
		String statusMessage = ResponseMessageUtil.getMessageForStatusCode(statusCodeValue);

		formattedResponse.setResponseCode(statusCodeValue);
		formattedResponse.setMessage(statusMessage);
		formattedResponse.setData(responseWrapper);

		return ResponseEntity.ok(formattedResponse);
	}

	@GetMapping("/active_Machine")
	public ResponseEntity<ResponseDto<CMPortalDashboardDto_AtmStatusDto>> getCmPortalDashboard_ActiveMachine() {
		CMPortalDashboardDto_AtmStatusDto dashboardData = dashboardService.setAtmStatusDto_cm();

		ResponseDto<CMPortalDashboardDto_AtmStatusDto> formattedResponse = new ResponseDto<>();
		int statusCodeValue = HttpStatus.OK.value();
		String statusMessage = ResponseMessageUtil.getMessageForStatusCode(statusCodeValue);

		formattedResponse.setResponseCode(statusCodeValue);
		formattedResponse.setMessage(statusMessage);
		formattedResponse.setData(dashboardData);

		return ResponseEntity.ok(formattedResponse);
	}

	@GetMapping("/counts")
	public ResponseEntity<ResponseDto<TicketsRaisedCountResponseDto>> getTicketsRaisedCount() {
		TicketsRaisedCountResponseDto ticketCounts = dashboardService.getTicketsRaisedCount();

		ResponseDto<TicketsRaisedCountResponseDto> responseWrapper = new ResponseDto<>();
		int statusCodeValue = HttpStatus.OK.value();
		String statusMessage = ResponseMessageUtil.getMessageForStatusCode(statusCodeValue);

		responseWrapper.setResponseCode(statusCodeValue);
		responseWrapper.setMessage(statusMessage);
		responseWrapper.setData(ticketCounts);

		return ResponseEntity.ok(responseWrapper);
	}

	@PostMapping("/ce-mtd-uptime")
	public ResponseEntity<ResponseDto<CeAtmUptimeDto>> getCeUptimeFromSp1(@RequestBody UserDto userDto) {
		CeAtmUptimeDto uptimeData = ceAtmDetailsServiceFor_Cm.getMtdUptimeFromSp(userDto.getUsername());

		ResponseDto<CeAtmUptimeDto> formattedResponse = new ResponseDto<>();
		int statusCodeValue = HttpStatus.OK.value(); // Default to 200 OK
		String statusMessage = ResponseMessageUtil.getMessageForStatusCode(statusCodeValue);

		formattedResponse.setResponseCode(statusCodeValue);
		formattedResponse.setMessage(statusMessage);
		formattedResponse.setData(uptimeData);

		return ResponseEntity.ok(formattedResponse);
	}

}