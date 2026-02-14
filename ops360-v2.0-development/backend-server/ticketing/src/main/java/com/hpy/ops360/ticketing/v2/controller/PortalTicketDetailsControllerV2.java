package com.hpy.ops360.ticketing.v2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.PortalTicketDetailsService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;


@RestController
@RequestMapping("/v2/portal/ticket")
@CrossOrigin("${app.cross-origin.allow}")
public class PortalTicketDetailsControllerV2 {
	
	private PortalTicketDetailsService portalTicketDetailsService;
	
	private RestUtils restUtils;
	public PortalTicketDetailsControllerV2(PortalTicketDetailsService portalTicketDetailsService,RestUtils restUtils) {
		super();
		this.portalTicketDetailsService = portalTicketDetailsService;
		this.restUtils = restUtils;
	}
	
	@Loggable
	@PostMapping("/details")
	public ResponseEntity<IResponseDto> getPortalTicketDetailsTab(@RequestBody TicketDetailsReqWithoutReqId request)
	{
		return ResponseEntity.ok(restUtils.wrapResponse(portalTicketDetailsService.getTicketTabDetails(request), "success"));
	}


	
	
	

}
