package com.hpy.ops360.ticketing.controller;

import java.util.List;

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

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class CustomerRemarkController {

	private final CustomerRemarkService customerRemarkService;

	@PostMapping("/remark/list")
	@Loggable
	public ResponseEntity<List<CustomerRemarkDto>> getCutomerRemarks(@RequestBody OwnerReqDto ownerReqDto) {
		return ResponseEntity.ok(customerRemarkService.getCutomerRemarks(ownerReqDto.getSubcallType(),ownerReqDto.getBroadCategory()));
	}

}