package com.hpy.ops360.report_service.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.report_service.dto.DownTicketData;
import com.hpy.ops360.report_service.dto.DownTicketReportDTO;
import com.hpy.ops360.report_service.dto.DownTicketReportDownloadRequest;
import com.hpy.ops360.report_service.dto.DownTicketReportRequest;
import com.hpy.ops360.report_service.dto.DownTicketReportRequestSearch;
import com.hpy.ops360.report_service.dto.TicketFilterResponse;
import com.hpy.ops360.report_service.request.TicketFilterRequest;
import com.hpy.ops360.report_service.service.DownFilterTicketService;
import com.hpy.ops360.report_service.service.DownTicketReportService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v2/portal/downreport")
public class DownTicketController {

	@Autowired
	private DownFilterTicketService ticketService;

	@Autowired
	private DownTicketReportService service;

	@Autowired
	private RestUtils restUtils;

	@PostMapping("/filter")
	public ResponseEntity<IResponseDto> filterTickets(@RequestBody TicketFilterRequest request) {
		log.info("Received filter request: {}", request);
		TicketFilterResponse response = ticketService.getFilteredTickets(request);
		log.info("Returning filtered tickets response with {} results", response.getAtmid().size());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully fetched the filter response"));
	}

	@PostMapping("/data")
	public ResponseEntity<IResponseDto> getReport(@RequestBody DownTicketReportRequest request) {
		log.info("Received report request: {}", request);
		DownTicketData report = service.getReport(request);
		return ResponseEntity.ok(restUtils.wrapResponse(report, "Successfully fetched the Report Data response"));
	}

	@PostMapping("/search")
	public ResponseEntity<IResponseDto> getReportsearch(@RequestBody DownTicketReportRequestSearch request) {
		log.info("Received report request: {}", request);
		List<String> report = service.getReportSearch(request);
		return ResponseEntity
				.ok(restUtils.wrapResponseListOfString(report, "Successfully fetched the Report Data response"));
	}
	
	@PostMapping("/download")
	public ResponseEntity<IResponseDto> exportReportDownload(@RequestBody DownTicketReportDownloadRequest request)
			throws IOException {
		List<DownTicketReportDTO> report = service.getReportDownload(request);

		String fileUrl = service.writeExcel(report);

		return ResponseEntity.ok(restUtils.wrapResponse(fileUrl, "succesfull"));
	}

}
