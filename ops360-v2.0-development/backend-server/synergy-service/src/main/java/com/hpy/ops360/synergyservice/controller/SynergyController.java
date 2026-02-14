package com.hpy.ops360.synergyservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.synergyservice.request.dto.AtmIdDto;
import com.hpy.ops360.synergyservice.request.dto.RemarksrequestDto;
import com.hpy.ops360.synergyservice.request.dto.TicketHistoryDto;
import com.hpy.ops360.synergyservice.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.synergyservice.request.dto.UpdateTicketStatusDto;
import com.hpy.ops360.synergyservice.response.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.synergyservice.response.dto.AtmUptimeDetailsResp;
import com.hpy.ops360.synergyservice.response.dto.GenericResponseDto;
import com.hpy.ops360.synergyservice.response.dto.RemarksResponseDto;
import com.hpy.ops360.synergyservice.response.dto.SynergyResponse;
import com.hpy.ops360.synergyservice.service.SynergyService;
import com.hpy.rest.dto.ResponseDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/synergy")
@CrossOrigin("${app.cross-origin.allow}")
public class SynergyController {
	
	@Autowired
	private SynergyService synergyService;

	@GetMapping("/token")
	public ResponseEntity<String> getSynergyRequestId() {
		return ResponseEntity.ok(synergyService.getSynergyRequestId());
	}
	
	@PostMapping("/atmUptime")
	public ResponseEntity<AtmUptimeDetailsResp> getAtmUptimeFromSynergy(@RequestBody AtmIdDto atmIdDto) {
		return ResponseEntity.ok(synergyService.getAtmUptime(atmIdDto.getAtmId()));
	}
	
	@PostMapping("/getRemarksHistory")
	public ResponseEntity<RemarksResponseDto> getRemarkHistory(@RequestBody RemarksrequestDto requestsdto) {
		return ResponseEntity.ok(synergyService.getRemarksHistory(requestsdto));
	}
	
	@PostMapping("/getnticketshistory")
	public ResponseEntity<AtmHistoryNTicketsResponse> getnticket(@RequestBody TicketHistoryDto ticketHistoryReqDto) {
		log.info("SynergyController:getntickets:{}", ticketHistoryReqDto);
		return ResponseEntity.ok(synergyService.getntickets(ticketHistoryReqDto));
	}
	
	@PostMapping("/updateTicketStatus")
	public ResponseEntity<GenericResponseDto> updateTicketStatus(@RequestBody UpdateTicketStatusDto updateTicketStatusDto )
	{
		return ResponseEntity.ok(synergyService.updateTicketStatus(updateTicketStatusDto));
	}
	
	@PostMapping("/updateSubcallType")
	public ResponseEntity<ResponseDto<SynergyResponse>> updateSubcallType(@RequestBody UpdateSubcallTypeDto updateSubcallTypeDto)
	{
		SynergyResponse response = synergyService.updateSubcallType(updateSubcallTypeDto);
		ResponseDto<SynergyResponse> formattedResponse = new ResponseDto() {
		};
	    int statusCodeValue = HttpStatus.OK.value();
	    String statusMessage = "success";

	    formattedResponse.setResponseCode(statusCodeValue);
	    formattedResponse.setMessage(statusMessage);
	    formattedResponse.setData(response);
	    return ResponseEntity.ok(formattedResponse);
	}
}
