package com.hpy.ops360.report_service.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.report_service.dto.ATMUptimeFilterResponse;
import com.hpy.ops360.report_service.dto.AtmUptimeDTO;
import com.hpy.ops360.report_service.dto.AtmUptimeRequest;
import com.hpy.ops360.report_service.dto.AtmUptimeRequestFilter;
import com.hpy.ops360.report_service.dto.AtmUptimeRequestSearch;
import com.hpy.ops360.report_service.dto.AtmUptimedownloadRequest;
import com.hpy.ops360.report_service.dto.AtmuptimeTicketData;
import com.hpy.ops360.report_service.service.ATMUptimeFilterTicketService;
import com.hpy.ops360.report_service.service.AtmUptimeService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v2/api/atmuptimereport")
public class AtmUptimeController {

	@Autowired
	private AtmUptimeService service;

	@Autowired
	private ATMUptimeFilterTicketService atmfilterService;

	@Autowired
	private RestUtils restUtil;

	@PostMapping("/filter")
	public ResponseEntity<IResponseDto> filterTickets(@RequestBody AtmUptimeRequestFilter request) {
		log.info("Received filter request: {}", request);
		ATMUptimeFilterResponse response = atmfilterService.getFilteredTickets(request);
		log.info("Returning filtered tickets response with {} results", response.getAtmid().size());
		return ResponseEntity.ok(restUtil.wrapResponse(response, "Successfully fetched the filter response"));
	}

	@PostMapping("/data")
	public ResponseEntity<IResponseDto> getUptime(@RequestBody AtmUptimeRequest request) {
		log.info("Received request for ATM uptime report with parameters: {}", request);
		AtmuptimeTicketData result = service.getUptimeReport(request);
		return ResponseEntity.ok(restUtil.wrapResponse(result, "Successfully fetched the ATM uptime report"));
	}

	@PostMapping("/search")
	public ResponseEntity<IResponseDto> getUptimeSearch(@RequestBody AtmUptimeRequestSearch request) {
		log.info("Received request for ATM uptime search with parameters: {}", request);
		List<String> result = service.getUptimeReportSearch(request);
		return ResponseEntity
				.ok(restUtil.wrapResponseListOfString(result, "Successfully fetched the ATM uptime report"));
	}
	
	@PostMapping("/download")
	public ResponseEntity<IResponseDto> exportdownloadReport(@RequestBody AtmUptimedownloadRequest request)
			throws IOException {

		List<AtmUptimeDTO> result = service.getUptimeReportDownload(request);
		String fileUrl = service.writeExcel(result);

		return ResponseEntity.ok(restUtil.wrapResponse(fileUrl, "succesfull"));
	}
}
