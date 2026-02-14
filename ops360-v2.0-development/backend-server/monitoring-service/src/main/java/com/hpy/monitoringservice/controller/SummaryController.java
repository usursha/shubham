package com.hpy.monitoringservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.monitoringservice.dto.ControllersummaryDTO;
import com.hpy.monitoringservice.dto.SummaryRequest;
import com.hpy.monitoringservice.service.ApiSummaryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/graph")
@CrossOrigin("${app.cross-origin.allow}")
@Slf4j
public class SummaryController {

	@Autowired
    private ApiSummaryService apiLogService;

	@PostMapping("/allstats")
    public List<ControllersummaryDTO> getControllerStatsByInterval(@RequestBody SummaryRequest request) {
		log.info("*** Inside Controller ***");
		List<ControllersummaryDTO> response=apiLogService.getControllerStatsByInterval(request);
		log.info("Response:--"+ response);
		return response;
    }
}
