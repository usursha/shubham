package com.hpy.ops360.sampatti.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.request.TeamLeaderboardDetailsReqDto;
import com.hpy.ops360.sampatti.service.TeamLeaderBoardService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/portal/team-leaderboard")
@CrossOrigin("${app.cross-origin.allow}")
public class PortalTeamLeaderBoardController {
	
	private TeamLeaderBoardService teamLeaderBoardService;
	
	private RestUtils restUtils;

	public PortalTeamLeaderBoardController(TeamLeaderBoardService teamLeaderBoardService,RestUtils restUtils) {
		super();
		this.teamLeaderBoardService = teamLeaderBoardService;
		this.restUtils= restUtils;
	}
	
	@GetMapping("/list-data/{cmUsername}")
	public ResponseEntity<IResponseDto> getTeamLeaderBoardListData(@PathVariable("cmUsername") String username)
	{
		log.info("calling PortalTeamLeaderBoardController | getTeamLeaderBoardListData() method");
		return ResponseEntity.ok(restUtils.wrapResponse(teamLeaderBoardService.getTeamLeaderBoardListData(username), "success"));
	}
	
	@GetMapping("/filter-data")
	public ResponseEntity<IResponseDto> getTeamLeaderboardFilterData()
	{
		return ResponseEntity.ok(restUtils.wrapResponse(teamLeaderBoardService.getTeamLeaderboardFilterData(), "success"));
	}
	
	@PostMapping("/details")
	public ResponseEntity<IResponseDto> getTeamLeaderboardDetails(@RequestBody TeamLeaderboardDetailsReqDto reqDto)
	{
		return ResponseEntity.ok(restUtils.wrapResponse(teamLeaderBoardService.getTeamLeaderboardDetails(reqDto.getUsername(), reqDto.getParamMonthYear(), reqDto.getSortTypeId(), reqDto.getTargetAchievedMin(), reqDto.getTargetAchievedMax()), "success"));
	}

}
