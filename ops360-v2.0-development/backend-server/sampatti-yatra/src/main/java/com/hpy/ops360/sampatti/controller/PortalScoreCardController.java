package com.hpy.ops360.sampatti.controller;



import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hpy.ops360.sampatti.dto.HierarchyStatsDto;
import com.hpy.ops360.sampatti.service.HierarchyStatsService;
import com.hpy.ops360.sampatti.service.UserIncentiveService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import java.util.List;

@RestController
@RequestMapping("/portal/scoreboard")
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class PortalScoreCardController {


    private UserIncentiveService service;
	
	private HierarchyStatsService hierarchyStatsService;
	
	private RestUtils restUtils;
	

	public PortalScoreCardController(UserIncentiveService service, HierarchyStatsService hierarchyStatsService,
			RestUtils restUtils) {
		super();
		this.service = service;
		this.hierarchyStatsService = hierarchyStatsService;
		this.restUtils = restUtils;
	}
	

    @GetMapping("/incentive/financial-year")
    public ResponseEntity<IResponseDto> getIncentives() {
        return ResponseEntity.ok(restUtils.wrapResponse(service.getFinancialYearIncentives(), "success"));
    }
    
    @GetMapping("/count/stats")
    public ResponseEntity<IResponseDto> getHierarchyStats() {
        log.info("Received request to fetch hierarchy stats");

        HierarchyStatsDto response = hierarchyStatsService.getHierarchyStats();

        if (response != null) {
            log.info("Hierarchy stats fetched successfully");
            log.debug("HierarchyStats Response: {}", response);
        } else {
            log.warn("No hierarchy stats found for the logged-in user");
        }

        return ResponseEntity.ok(restUtils.wrapResponse(response, "success"));
    }

}
