package com.hpy.ops360.report_service.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.report_service.dto.DowtimeDataDTO;
import com.hpy.ops360.report_service.dto.MtdStatusDto;
import com.hpy.ops360.report_service.dto.UptimeReportRequest;
import com.hpy.ops360.report_service.dto.UptimeReportRequestSearch;
import com.hpy.ops360.report_service.dto.UptimeReportResultDTO;
import com.hpy.ops360.report_service.dto.UptimeReportdownloadRequest;
import com.hpy.ops360.report_service.dto.UptimeResponseFilter;
import com.hpy.ops360.report_service.service.CeUserService;
import com.hpy.ops360.report_service.service.MtdStatusService;
import com.hpy.ops360.report_service.service.UptimeReportService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v2/portal/ceuptimereport")
public class CeUserController {

	@Autowired
	private CeUserService ceUserService;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private UptimeReportService uptimeReportService;

	@Autowired
	private MtdStatusService mtdStatusService;

	@GetMapping("/filter")
	public ResponseEntity<IResponseDto> getCeUsers() {
		log.info("Received request to fetch CE Users");
		UptimeResponseFilter response = ceUserService.getCeUsers();
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully fetched the filter response"));
	}

	@PostMapping("/data")
	public ResponseEntity<IResponseDto> getUptimeReport(@RequestBody UptimeReportRequest request) {
		log.info("Received request for uptime report with parameters: {}", request);
		DowtimeDataDTO report = uptimeReportService.getReport(request);
		return ResponseEntity.ok(restUtils.wrapResponse(report, "Successfully fetched the report data response"));
	}

	@GetMapping("/mtd-date")
	public ResponseEntity<IResponseDto> getMtdStatus() {
		MtdStatusDto dto = mtdStatusService.getMtdStatus();
		return ResponseEntity.ok(restUtils.wrapResponse(dto, "Fetched the date"));
	}

	@PostMapping("/search")
	public ResponseEntity<IResponseDto> getUptimeReportSearch(@RequestBody UptimeReportRequestSearch request) {
		log.info("Received request for uptime report with parameters: {}", request);
		List<String> report = uptimeReportService.getFilteredUserIds(request);
		return ResponseEntity
				.ok(restUtils.wrapResponseListOfString(report, "Successfully fetched the report data response"));
	}
	
	@PostMapping("/download")
	public ResponseEntity<IResponseDto> downloadReport(@RequestBody UptimeReportdownloadRequest request)
			throws IOException {

		List<UptimeReportResultDTO> report = uptimeReportService.getReportdownload(request);

		String fileUrl = uptimeReportService.writeExcel(report);

		return ResponseEntity.ok(restUtils.wrapResponse(fileUrl, "succesfull"));
	}

}
