package com.hpy.ops360.ticketing.v2.controller;

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
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.OwnerService;
import com.hpy.ops360.ticketing.ticket.dto.BroadCategoryDto;
import com.hpy.ops360.ticketing.ticket.dto.BroadCategoryDto2;
import com.hpy.ops360.ticketing.ticket.dto.SelectedCategoryDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v2/owner")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class OwnerV2Controller {

	private final OwnerService ownerService;
	
	@Autowired
	private RestUtils restUtils;

	//api to return subcalltype list based on selected broad category ---subcalltype 
	@PostMapping("/list")
	@Loggable
	public ResponseEntity<List<OwnerDto>> getOwners(@RequestBody BroadCategoryDto2 broadCategoryDto) {
		return ResponseEntity.ok(ownerService.getOwners(broadCategoryDto.getCategory(),broadCategoryDto.getSubcallType()));

	}
	
	//api to return subcalltype list based on selected broad category ---subcalltype 
	@PostMapping("/subcallType/list/portal")
	@Loggable
	public ResponseEntity<IResponseDto> getSubcallTypeListPortal(@RequestBody BroadCategoryDto broadCategoryDto) {
		List<OwnerDto> response = ownerService.getSubcallTypeListPortal(broadCategoryDto.getCategory());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "succesfull"));

	}
	
	//api to return broad category list and selected broad category based on selected subcalltype for fresh tickets
	@PostMapping("/broad-category")//--owner
	@Loggable
	public ResponseEntity<IResponseDto> getBroadCategory(@RequestBody SubCallTypeDto subCallTypeDto)
	{
		SelectedCategoryDto response = ownerService.getBroadCategory(subCallTypeDto.getSubcallType(),subCallTypeDto.getTicketNumber(),subCallTypeDto.getAtmId());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "succesfull"));

	}

}
