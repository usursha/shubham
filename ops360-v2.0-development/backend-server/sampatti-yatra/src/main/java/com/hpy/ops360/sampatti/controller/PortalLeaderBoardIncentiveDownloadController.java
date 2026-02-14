package com.hpy.ops360.sampatti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.LeaderBoardDownloadRequest;
import com.hpy.ops360.sampatti.dto.response.GenericResponseDto;
import com.hpy.ops360.sampatti.service.LeaderBoardIncentiveDownloadService;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/portal/leaderboard/download")
@CrossOrigin("${app.cross-origin.allow}")
public class PortalLeaderBoardIncentiveDownloadController {

	@Autowired
	private LeaderBoardIncentiveDownloadService incentiveService;
	
	@Autowired
	private RestUtilsImpl restutil;

	@PostMapping("/leaderBoardIncentiveExcelDownload")
	public ResponseEntity<IResponseDto> generateReportExcel(@RequestBody LeaderBoardDownloadRequest reportRequest) {
		GenericResponseDto response =incentiveService.getDataExcel(reportRequest.getMonthYear(), reportRequest.getUserType());
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));

	}
	
	@PostMapping("/leaderBoardIncentiveCSVDownload")
	public ResponseEntity<IResponseDto> generateReportCSV(@RequestBody LeaderBoardDownloadRequest reportRequest) {
		GenericResponseDto response =incentiveService.getDataCSV(reportRequest.getMonthYear(), reportRequest.getUserType());
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));

	}
}
