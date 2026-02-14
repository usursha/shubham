package com.hpy.ops360.ticketing.v2.controller;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.ticketing.config.DisableSslClass;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.ticketing.dto.AtmIdDto;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryByDateRequest;
import com.hpy.ops360.ticketing.dto.TicketHistoryByDateResponse;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.UpdateEtaDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.response.AtmUptimeDetailsResp;
import com.hpy.ops360.ticketing.response.CreateTicketResponse;
import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
import com.hpy.ops360.ticketing.response.SynergyResponse;
import com.hpy.ops360.ticketing.service.SynergyService;
import com.hpy.ops360.ticketing.ticket.dto.CreateTicketDto;
import com.hpy.ops360.ticketing.ticket.dto.RemarksResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/synergy")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class SynergyControllerV2 {

	private final SynergyService synergyService;

	@GetMapping("/token")
	@Loggable
	public ResponseEntity<SynergyResponse> getSynergyRequestId() {
		return ResponseEntity.ok(synergyService.getSynergyRequestId());
	}

	@PostMapping("/getnticketshistory")
	@Loggable
	public ResponseEntity<AtmHistoryNTicketsResponse> getnticket(@RequestBody TicketHistoryDto ticketHistoryReqDto) {
		log.info("SynergyController:getntickets:{}", ticketHistoryReqDto);
		return ResponseEntity.ok(synergyService.getntickets(ticketHistoryReqDto));
	}

	@PostMapping("/open-tickets")
	@Loggable
	public ResponseEntity<OpenTicketsResponse> getOpenTickets(@RequestBody JsonNode request) {
	    try {
	    	DisableSslClass.disableSSLVerification();
	        List<AtmDetailsDto> atms = new ArrayList<>();
	        ObjectMapper objectMapper = new ObjectMapper();
	        
	        if (request.isArray()) {
	            // Handle array input
	            atms = objectMapper.readValue(request.toString(), new TypeReference<List<AtmDetailsDto>>() {});
	        } else {
	            // Handle single object input
	            AtmDetailsDto atm = objectMapper.convertValue(request, AtmDetailsDto.class);
	            atms.add(atm);
	        }
	        
	        log.info("Received request to fetch open tickets for {} ATMs", atms.size());
	        
	        OpenTicketsResponse response = synergyService.getOpenTicketDetails(atms);
	        if (response == null) {
	            log.warn("No response received from ticket service");
	            return ResponseEntity.noContent().build();
	        }
	        
	        log.info("Successfully retrieved open tickets");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.error("Error fetching open tickets: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@PostMapping("/createticket")
	@Loggable
	public ResponseEntity<CreateTicketResponse> createTicket(@RequestBody CreateTicketDto ticket) {
		return ResponseEntity.ok(synergyService.createTicket(ticket));
	}

	@PostMapping("/addComment")
	@Loggable
	public ResponseEntity<SynergyResponse> updateEta(@RequestBody UpdateEtaDto updateEtaDto) {
		return ResponseEntity.ok(synergyService.updateEta(updateEtaDto));
	}

	@PostMapping("/getTicketHistory")
	@Loggable
	public ResponseEntity<RemarksResponseDto> getRemarkHistory(@RequestBody RemarksrequestDto requestsdto) {
		return ResponseEntity.ok(synergyService.getRemarksHistory(requestsdto));
	}

	@PostMapping("/atmUptime")
	@Loggable
	public ResponseEntity<AtmUptimeDetailsResp> getAtmUptimeFromSynergy(@RequestBody AtmIdDto atmIdDto) {
		return ResponseEntity.ok(synergyService.getAtmUptime(atmIdDto.getAtmId()));
	}

}