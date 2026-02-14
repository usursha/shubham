package com.hpy.ops360.report_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.report_service.dto.DownTicketReportDTO;
import com.hpy.ops360.report_service.dto.DownTicketReportRequestdownload;
import com.hpy.ops360.report_service.service.DownTicketFileExportService;
import com.hpy.ops360.report_service.service.DownTicketReportService;
import com.hpy.ops360.report_service.service.DownTicketReportServiceDownload;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/api/down-ticket-report")
@RequiredArgsConstructor
public class DownTicketReportExportController {

	@Autowired
	private DownTicketReportServiceDownload reportService;

	@Autowired
	private DownTicketFileExportService fileExportService;

	@Autowired
	private RestUtils restUtils;

	@PostMapping("/export")
	public ResponseEntity<IResponseDto> exportReport(@RequestBody DownTicketReportRequestdownload request,
			@RequestParam String format) {
		List<DownTicketReportDTO> reportData = reportService.getReport(request);

		String fileUrl = fileExportService.exportToFile(reportData, format);

		return ResponseEntity.ok(restUtils.wrapResponse(fileUrl, "succesfull"));
	}

	

}
