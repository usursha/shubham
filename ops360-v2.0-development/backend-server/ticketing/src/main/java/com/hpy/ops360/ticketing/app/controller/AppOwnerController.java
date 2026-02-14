package com.hpy.ops360.ticketing.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.dto.OwnerDto;
import com.hpy.ops360.ticketing.dto.SubCallTypeDto;
import com.hpy.ops360.ticketing.service.OwnerService;
import com.hpy.ops360.ticketing.ticket.dto.BroadCategoryDto2;
import com.hpy.ops360.ticketing.ticket.dto.SelectedCategoryDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/app/owner")
@AllArgsConstructor
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class AppOwnerController {

	private final OwnerService ownerService;
	
	@Autowired
	private final RestUtils restUtils;

	
	@PostMapping("/list")
	public ResponseEntity<IResponseDto> getOwners(@RequestBody BroadCategoryDto2 broadCategoryDto) {
	    log.info("***** Inside getOwners *****");
	    log.info("Request Received: " + broadCategoryDto);
	    List<OwnerDto> result = ownerService.getOwners(broadCategoryDto.getCategory(),broadCategoryDto.getSubcallType());
	    log.info("Response Returned from getOwners: " + result);
	    return ResponseEntity.ok(restUtils.wrapResponse(result, "Owner details fetched successfully"));
	}


	@PostMapping("/broad-category")
	public ResponseEntity<IResponseDto> getBroadCategory(@RequestBody SubCallTypeDto subCallTypeDto) {
	    log.info("***** Inside getBroadCategory *****");
	    log.info("Request Received: " + subCallTypeDto);
	    
	    // Call the service to get broad category based on subcall type
	    SelectedCategoryDto result = ownerService.getBroadCategory(subCallTypeDto.getSubcallType(),subCallTypeDto.getTicketNumber(),subCallTypeDto.getAtmId());
	    
	    log.info("Response Returned from getBroadCategory: " + result);
	    
	    // Wrap the response in IResponseDto
	    return ResponseEntity.ok(restUtils.wrapResponse(result, "Broad category details fetched successfully"));
	}


}
