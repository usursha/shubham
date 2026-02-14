package com.hpy.ops360.ticketing.v2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.TicketGraphDetailsDTO;
import com.hpy.ops360.ticketing.service.TicketGraphDetailsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/graph")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class TicketGraphDetailsControllerV2 {

	@Autowired
	private TicketGraphDetailsService ticketGphService;

	@GetMapping("/ticketGraphDetails")
//	@Loggable
	public ResponseEntity<List<TicketGraphDetailsDTO>> getGraphDetails() {
		
		return ResponseEntity.ok(ticketGphService.getTicketGraphDetails());
	}

}
