package com.hpy.ops360.report_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.report_service.dto.AtmUptimeDTO;
import com.hpy.ops360.report_service.dto.AtmUptimeRequestDownload;
import com.hpy.ops360.report_service.service.AtmUptimeFileExportService;
import com.hpy.ops360.report_service.service.AtmUptimeService;
import com.hpy.ops360.report_service.service.AtmUptimeServiceDownload;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/api/atmuptime-ticket-report")
@RequiredArgsConstructor
public class AtmUptimeTicketReportExportController {

	@Autowired
	private AtmUptimeServiceDownload reportService;

	@Autowired
	private AtmUptimeFileExportService fileExportService;

	@Autowired
	private RestUtils restUtils;

	@PostMapping("/export")
	public ResponseEntity<IResponseDto> exportReport(@RequestBody AtmUptimeRequestDownload request,
			@RequestParam String format) {
		List<AtmUptimeDTO> reportData = reportService.getUptimeReport(request);

		String fileUrl = fileExportService.exportToFile(reportData, format);

		return ResponseEntity.ok(restUtils.wrapResponse(fileUrl, "succesfull"));
	}

	

	
}
