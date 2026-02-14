package com.hpy.ops360.dashboard.custrespcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.hpy.ops360.dashboard.dto.GetCmPortalATMScreenDataDto;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/Cm_portalATMDetails")
@CrossOrigin("${app.cross-origin.allow}")
public class CMPortalAtmDetailsControllerV2 {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@Autowired
	private RestUtils restUtils;

	@GetMapping("uptime_active/{userId}")
	public ResponseEntity<IResponseDto> getAtmScreenDataForCm(@PathVariable String userId) {
		log.info("**** Inside getAtmScreenDataForCm Controller ****");
		log.info("Request Recieved:- " + userId);
		GetCmPortalATMScreenDataDto atmUptimeAndActiveMachineDto = dashboardService.getCmPortalATMScreenData(userId);
		log.info("Response Recieved From atmUptimeAndActiveMachineDto" + atmUptimeAndActiveMachineDto);

		if (atmUptimeAndActiveMachineDto != null) {
			return ResponseEntity.ok(restUtils.wrapResponse(atmUptimeAndActiveMachineDto,
					"Successfully fetched the Data of ATM Screen Data for CM"));
		} else {
			return ResponseEntity.ok(
					restUtils.wrapNullResponse("Cannot Fetched the data For respective User", HttpStatus.NOT_FOUND));
		}
	}

	@GetMapping("/Ce_details/{userId}")
	public ResponseEntity<IResponseDto> getPersonalOfficialDetails(@PathVariable String userId) {
		log.info("**** Inside getPersonalOfficialDetails Controller ****");
		log.info("Request Recieved:- " + userId);
		CEDetailsDto response = cmSynopsisService.getCEDetails(userId);
		log.info("Response Recieved From cmSynopsisService:- " + response);
		return ResponseEntity
				.ok(restUtils.wrapResponse(response, "Successfully fetched the CE Details as Per User ID"));
	}

	@GetMapping("/counts/{userId}")
	public ResponseEntity<IResponseDto> getTicketsRaisedCount(@PathVariable String userId) {
		log.info("**** Inside getTicketsRaisedCount Controller ****");
		log.info("Request Recieved:- " + userId);
		TicketsRaisedCountDTO ticketCounts = dashboardService.getCETicketsRaisedCount(userId);
		log.info("Response Recieved From cmSynopsisService:- " + ticketCounts);
		return ResponseEntity.ok(
				restUtils.wrapResponse(ticketCounts, "Successfully fetched the Ticket Raised Count as Per User ID"));
	}

	@GetMapping("/get_CMUnderCEAtmDetails/{userId}")
	public ResponseEntity<IResponseDto> getCMUnderCEAtmDetails(@PathVariable String userId) {
		log.info("**** Inside getCMUnderCEAtmDetails Controller ****");
		log.info("Request Recieved:- " + userId);
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = cmSynopsisService.getCMUnderCEAtmDetails(userId);
		log.info("Response Recieved From cmSynopsisService:- " + atmDetailsDTO);
		return ResponseEntity.ok(restUtils.wrapResponse(atmDetailsDTO, "Fetched Remarks Data"));
	}

	@GetMapping("/ticket_details/{atmId}")
	public ResponseEntity<IResponseDto> getATMIndexDetails(@PathVariable String atmId) {
		log.info("**** Inside getATMIndexDetails Controller ****");
		log.info("Request Recieved:- " + atmId);
		ATMIndexDetailsDTO response = cmSynopsisService.getATMIndexDetails(atmId);
		log.info("Response Recieved From cmSynopsisService:- " + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully fetched the Ticket Data of ATM ID"));
	}

	// Controller Method
	@GetMapping("/download-excel-atm-index/{userId}")
	// @Loggable
	public ResponseEntity<IResponseDto> downloadExcelAtmIndex(@PathVariable String userId) throws IOException {
		String base64Encoded = cmSynopsisService.generateExcelBase64AtmIndex(userId);
		return ResponseEntity.ok(restUtils.wrapResponse(base64Encoded, "Excel file generated successfully"));
	}

//	@GetMapping("/download-csv-atm-index/{userId}")
//	@Loggable // Uncomment if you have this annotation defined and configured
//	public ResponseEntity<IResponseDto> downloadAtmIndexCsv(@PathVariable String userId) throws IOException {
//		ByteArrayResource resource = cmSynopsisService.generateCsvResourceAtmIndex_base64(userId);
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=atm_index.json");
//		IResponseDto responseDto = restUtils.wrapByteArrayResourceResponse(resource,
//				"CSV file content returned in JSON response.");
//		return ResponseEntity.ok().headers(headers).body(responseDto);
//	}
	
	
	@GetMapping("/download-csv-atm-index/{userId}")
	@Loggable
	public ResponseEntity<IResponseDto> downloadAtmIndexCsv(@PathVariable String userId) throws IOException {
	    // Generate the CSV as ByteArrayResource using your existing method
	    ByteArrayResource resource = cmSynopsisService.generateCsvResourceAtmIndex(userId);
	    
	    // Use the new base64 wrapper method
	    IResponseDto responseDto = restUtils.wrapByteArrayResourceResponseAsBase64(resource,
	            "CSV file content returned as base64 in JSON response.");
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=atm_index.csv");
	    
	    return ResponseEntity.ok().headers(headers).body(responseDto);
	}


}
