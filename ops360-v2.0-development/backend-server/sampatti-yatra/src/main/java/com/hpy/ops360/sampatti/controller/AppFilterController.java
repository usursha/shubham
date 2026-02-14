package com.hpy.ops360.sampatti.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.FilterDataRequestDto;
import com.hpy.ops360.sampatti.service.FilterDataService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

@RestController
@RequestMapping("/app/filter")
@CrossOrigin("${app.cross-origin.allow}")
public class AppFilterController {
	
	private FilterDataService filterDataService;
	
	private RestUtils restUtils;

	public AppFilterController(FilterDataService filterDataService,RestUtils restUtils) {
		super();
		this.filterDataService = filterDataService;
		this.restUtils= restUtils;
	}
	
	@PostMapping("/leaderboard-filter-data")
	public ResponseEntity<IResponseDto> getLeaderBoardFilterData(@RequestBody FilterDataRequestDto filterDataRequestDto){
		return ResponseEntity.ok(restUtils.wrapResponse(filterDataService.getLeaderBoardCeFilterData(filterDataRequestDto), "success"));
	}

}
