package com.hpy.ops360.ticketing.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.CeMachineUpDownCountDto;
import com.hpy.ops360.ticketing.cm.dto.UserDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryResponse;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.CmTaskService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v2/app/task")
@CrossOrigin("${app.cross-origin.allow}")
public class AppTaskController {

	@Autowired
	private CmTaskService taskService;
	
	@Autowired
	private RestUtils restUtils;
	
	
	@PostMapping("/ce-machine-up-down-count")
	@Loggable
	public ResponseEntity<IResponseDto> getCeMachineUpDownCount(@RequestBody UserDto userDto) {
		log.info("*** Inside getCeMachineUpDownCount");
		CeMachineUpDownCountDto response= taskService.getCeMachineUpDownCount(userDto.getUsername());
		log.info("Response Recieved From getCeMachineUpDownCount:- "+ response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Machine Updown Count Fetched Successfully"));
	}
	
	@PostMapping("/ce-n-tickets-history")
	@Loggable
	public ResponseEntity<TicketHistoryResponse> getNTicketsHistoryCE(@RequestBody TicketHistoryDto request) {
		log.info("*** Inside getNTicketsHistoryCE");
		TicketHistoryResponse response = taskService.processTickets_forNticketHistory_Ce(request);
		log.info("Response Recieved From getNTicketsHistoryCE:- "+ response);
		return ResponseEntity.ok(response);
	}
}
