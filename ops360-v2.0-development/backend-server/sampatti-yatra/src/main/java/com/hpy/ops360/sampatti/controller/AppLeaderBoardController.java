package com.hpy.ops360.sampatti.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.AppLeaderBoardDTO;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardRequestDto;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardResponseListDto;
import com.hpy.ops360.sampatti.dto.ResponseMapperDto;
import com.hpy.ops360.sampatti.dto.UserIncentiveMonthYearDto;
import com.hpy.ops360.sampatti.service.AppLeaderBoardCachingService;
import com.hpy.ops360.sampatti.service.UserIncentiveMonthYearService;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/app/leader-board")
@CrossOrigin("${app.cross-origin.allow}")
public class AppLeaderBoardController {
	
	@Autowired
	private AppLeaderBoardCachingService service;
	
	@Autowired
	private UserIncentiveMonthYearService incentiveMonthYearService;
	
	@Autowired
	private RestUtilsImpl restutil;
	

	@PostMapping("/data")
	public ResponseEntity<IResponseDto> getCEScoreCardData(@Valid @RequestBody AppLeaderBoardRequestDto request) throws MalformedURLException, IOException{	
		AppLeaderBoardResponseListDto response=service.getCeLeaderBoardDataList(request);
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}
	
	@GetMapping("/incentive-month-year")
	public ResponseEntity<IResponseDto> getMonthYearDetails(){
		 log.info("Enter inside getMonthYearDetails Method.");
		List<UserIncentiveMonthYearDto> response=incentiveMonthYearService.getIncentivesMonthYear();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}


}
