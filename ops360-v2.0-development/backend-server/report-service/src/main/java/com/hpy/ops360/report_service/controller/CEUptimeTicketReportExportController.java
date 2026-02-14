package com.hpy.ops360.report_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.report_service.dto.UptimeReportRequestDownload;
import com.hpy.ops360.report_service.dto.UptimeReportResultDTO;
import com.hpy.ops360.report_service.service.UptimeReportService;
import com.hpy.ops360.report_service.service.UptimeTicketFileExportService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/api/ceuptime-ticket-report")
@RequiredArgsConstructor
@Slf4j
public class CEUptimeTicketReportExportController {

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private UptimeReportService uptimeReportService;

	@Autowired
	private UptimeTicketFileExportService fileExportService;

	@PostMapping("/export")
	public ResponseEntity<IResponseDto> exportReport(@RequestBody UptimeReportRequestDownload request,
			@RequestParam String format) {
		List<UptimeReportResultDTO> report = uptimeReportService.getDownloadReport(request);

		String fileUrl = fileExportService.exportToFile(report, format);

		return ResponseEntity.ok(restUtils.wrapResponse(fileUrl, "succesfull"));
	}

	

	
}
