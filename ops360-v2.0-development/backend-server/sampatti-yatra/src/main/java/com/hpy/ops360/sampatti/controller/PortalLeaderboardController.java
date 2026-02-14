package com.hpy.ops360.sampatti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.FilterUserMasterRequestDto;
import com.hpy.ops360.sampatti.dto.FilteredResponseDto;
import com.hpy.ops360.sampatti.dto.LeaderBoardRespCountDto;
import com.hpy.ops360.sampatti.dto.LeaderBoardSearchRespCountDto;
import com.hpy.ops360.sampatti.dto.LeaderboardMonthDto;
import com.hpy.ops360.sampatti.dto.LeaderboardRequestDto;
import com.hpy.ops360.sampatti.dto.PortalLeaderBoardRespCountDto;
import com.hpy.ops360.sampatti.dto.SearchLeaderboardRequestDto;
import com.hpy.ops360.sampatti.service.FilterUserMasterService;
import com.hpy.ops360.sampatti.service.LeaderboardMonthService;
import com.hpy.ops360.sampatti.service.LeaderboardService;
import com.hpy.ops360.sampatti.service.SearchLeaderboardService;
import com.hpy.ops360.sampatti.service.UserIncentiveMonthYearService;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/portal/leaderboard")
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class PortalLeaderboardController {

	
	@Autowired
	UserIncentiveMonthYearService incentiveMonthYearService;
	
	@Autowired
    private LeaderboardMonthService leaderboardmonthService;
	
	@Autowired
	private LeaderboardService leaderboardService;

	@Autowired
	private FilterUserMasterService service;
	
	@Autowired
	private SearchLeaderboardService searchLeaderboardService;

	@Autowired
	private RestUtilsImpl restutil;

	@PostMapping("/data")
	public ResponseEntity<IResponseDto> getLeaderboardData(HttpServletRequest httprequest,@RequestBody LeaderboardRequestDto requestDto) {
		log.info("Received leaderboard request: {}", requestDto);
		LeaderBoardRespCountDto response=leaderboardService.getLeaderboardData(httprequest,requestDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
	
	@PostMapping("/portal/data")
	public ResponseEntity<IResponseDto> getportalLeaderboardData(HttpServletRequest httprequest,@RequestBody LeaderboardRequestDto requestDto) {
		log.info("Received leaderboard request: {}", requestDto);
		PortalLeaderBoardRespCountDto response=leaderboardService.getportalLeaderboardData(httprequest,requestDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
	
	@PostMapping("/totalcounts")
	public ResponseEntity<IResponseDto> getLeaderboardtotalData(HttpServletRequest httprequest,@RequestBody LeaderboardRequestDto requestDto) {
		log.info("Received leaderboard request: {}", requestDto);
		LeaderBoardRespCountDto response=leaderboardService.getLeaderboardData(httprequest,requestDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
	
	@PostMapping("/search")
	public ResponseEntity<IResponseDto> getsearchLeaderboardName(@RequestBody SearchLeaderboardRequestDto requestDto) {
		log.info("Received Search leaderboard request: {}", requestDto);
		List<LeaderBoardSearchRespCountDto> response=searchLeaderboardService.getLeaderboardsearchData(requestDto);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@GetMapping("/month")
	public ResponseEntity<IResponseDto> getMonthData() {
		log.info("Received leaderboard request: {}");
		List<LeaderboardMonthDto> response=leaderboardmonthService.getLeaderboardMonthData();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

	@PostMapping("/filter")
	public ResponseEntity<IResponseDto> getFilteredList(@RequestBody FilterUserMasterRequestDto request) {
		log.info("Received leaderboard request: {}", request);
		FilteredResponseDto response=service.getfilterData(request);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

}
