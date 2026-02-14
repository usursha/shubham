package com.hpy.ops360.dashboard.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.ATMIndexDetailsDTO;
import com.hpy.ops360.dashboard.dto.CEDetailsDto;
import com.hpy.ops360.dashboard.dto.CMUnderCEAtmDetailsDTO;
import com.hpy.ops360.dashboard.dto.CMUnderCEAtmDetailsDTO2;
import com.hpy.ops360.dashboard.dto.CsvResponseDTO;
import com.hpy.ops360.dashboard.dto.GetCmPortalATMScreenDataDto;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.dto.UserMastDto;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.ops360.dashboard.service.UserService;
import com.hpy.ops360.dashboard.util.RestUtilsDashboard;
import com.hpy.rest.dto.IResponseDto;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("Cm_portalATMDetails")
@CrossOrigin("${app.cross-origin.allow}")
public class CMPortalAtmDetailsController {

	@Autowired
	private RestUtilsDashboard restUtils;

	@Autowired
	private UserService userService;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@GetMapping("uptime_active/{userId}")
	@Timed
	@Loggable
	public ResponseEntity<GetCmPortalATMScreenDataDto> getAtmScreenDataForCm(@PathVariable String userId) {
		GetCmPortalATMScreenDataDto atmUptimeAndActiveMachineDto = dashboardService.getCmPortalATMScreenData(userId);

		if (atmUptimeAndActiveMachineDto != null) {
			return ResponseEntity.ok(atmUptimeAndActiveMachineDto);
		} else {
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/Ce_details/{userId}")
	@Timed
	@Loggable
	public ResponseEntity<CEDetailsDto> getPersonalOfficialDetails(@PathVariable String userId) {
		return ResponseEntity.ok(cmSynopsisService.getCEDetails(userId));
	}

	@GetMapping("/Ce_details_Personal/{userId}")
	@Timed
	@Loggable
	public ResponseEntity<IResponseDto> getPersonalOfficialDetailsLatest(@PathVariable String userId) {
		CEDetailsDto ceDetails = cmSynopsisService.getCEDetails(userId);
		return ResponseEntity.ok(restUtils.wrapResponse(ceDetails, "success"));

	}

	@GetMapping("/counts/{userId}")
	@Timed
	@Loggable
	public ResponseEntity<TicketsRaisedCountDTO> getTicketsRaisedCount(@PathVariable String userId) {
		log.info("Ticket COntroller Executed");
		TicketsRaisedCountDTO ticketCounts = dashboardService.getCETicketsRaisedCount(userId);
		return ResponseEntity.ok(ticketCounts);

	}

	@GetMapping("/get_CMUnderCEAtmDetails/{userId}")
	@Timed
	@Loggable
	public ResponseEntity<List<CMUnderCEAtmDetailsDTO>> getCMUnderCEAtmDetails(@PathVariable String userId) {
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = cmSynopsisService.getCMUnderCEAtmDetails(userId);
		return ResponseEntity.ok(atmDetailsDTO);
	}

	@GetMapping("/get-cm-atm-details/{userId}")
	@Timed
	@Loggable
	public ResponseEntity<List<CMUnderCEAtmDetailsDTO2>> getCMAtmDetails(@PathVariable String userId) {
		List<CMUnderCEAtmDetailsDTO2> atmDetailsDTO = cmSynopsisService.getCMAtmDetails(userId);
		return ResponseEntity.ok(atmDetailsDTO);
	}

	@GetMapping("/ticket_details/{atmId}")
	@Timed
	@Loggable
	public ResponseEntity<ATMIndexDetailsDTO> getATMIndexDetails(@PathVariable String atmId) {
		return ResponseEntity.ok(cmSynopsisService.getATMIndexDetails(atmId));
	}

	@GetMapping("/download-csv/{userId}")
	@Loggable
	public ResponseEntity<Resource> downloadCsv(@PathVariable String userId) throws IOException {
		ByteArrayResource resource = cmSynopsisService.generateCsvResource(userId);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=atm_data.csv")
				.contentType(MediaType.parseMediaType("text/csv")).body(resource);
	}

	@GetMapping("/download-csv-atm-index/{userId}")
	@Loggable
	public ResponseEntity<Resource> downloadAtmIndexCsv(@PathVariable String userId) throws IOException {
		ByteArrayResource resource = cmSynopsisService.generateCsvResourceAtmIndex(userId);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=atm_data.csv")
				.contentType(MediaType.parseMediaType("text/csv")).body(resource);
	}

	@GetMapping("/download-csvbase64/{userId}")
	@Loggable
	public ResponseEntity<CsvResponseDTO> downloadCsvBase64(@PathVariable String userId) throws IOException {
		String base64Content = cmSynopsisService.generateBase64CsvContent(userId);
		CsvResponseDTO response = new CsvResponseDTO();
		response.setBase64Content(base64Content);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/download-excel/{userId}")
	@Loggable
	public ResponseEntity<String> downloadExcel(@PathVariable String userId) throws IOException {
		String base64Encoded = cmSynopsisService.generateExcelBase64(userId);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json")
				.body("{\"data\":\"" + base64Encoded + "\"}");
	}

	@GetMapping("/download-excel-atm-index/{userId}")
	@Loggable
	public ResponseEntity<String> downloadExcelAtmIndex(@PathVariable String userId) throws IOException {
		String base64Encoded = cmSynopsisService.generateExcelBase64AtmIndex(userId);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json")
				.body("{\"data\":\"" + base64Encoded + "\"}");
	}

	@GetMapping("/user-details")
	@Loggable
	public ResponseEntity<IResponseDto> getUserDetails() {

		log.info("****** Inside getUserDetails Controller ******");

		UserMastDto response = userService.getUserMast();
		log.info("Returned Response from Controller:- " + response);

		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));

	}

}
