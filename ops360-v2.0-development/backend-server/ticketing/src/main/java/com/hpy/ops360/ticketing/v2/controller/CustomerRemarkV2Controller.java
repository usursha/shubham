package com.hpy.ops360.ticketing.v2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.dto.CustomerRemarkDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.CustomerRemarkService;
import com.hpy.ops360.ticketing.ticket.dto.OwnerReqDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v2/customer")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class CustomerRemarkV2Controller {

	private final CustomerRemarkService customerRemarkService;
	
	@Autowired
	private RestUtils restUtils;

	@PostMapping("/remark/list")
	@Loggable
	public ResponseEntity<IResponseDto> getCutomerRemarks(@RequestBody OwnerReqDto ownerReqDto) {
		List<CustomerRemarkDto> response = customerRemarkService.getCutomerRemarks(ownerReqDto.getSubcallType(),ownerReqDto.getBroadCategory());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "succesfull"));
	}

}