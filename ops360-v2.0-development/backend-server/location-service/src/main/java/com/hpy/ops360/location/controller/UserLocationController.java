package com.hpy.ops360.location.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.location.dto.UserdistLocationDto;
import com.hpy.ops360.location.dto.UserdistLocationRequest;
import com.hpy.ops360.location.dto.UsertotaldistLocationDto;
import com.hpy.ops360.location.service.DistanceCalculateService;
import com.hpy.ops360.location.service.DistanceListCalculationService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/location")
@Slf4j
@AllArgsConstructor
public class UserLocationController {

	
	private DistanceCalculateService distanceCalculateService;
	
	private DistanceListCalculationService distanceListCalculationService;
	
	private RestUtils restUtils;
	
	
	
	@PostMapping("/lat_long_list")
	public ResponseEntity<IResponseDto> getLatLongList(@RequestBody UserdistLocationRequest request) {
		log.info("Inside getLatLongList controller");
		log.info("Request Recieved:- "+ request);
		List<UserdistLocationDto> dashboardData=distanceCalculateService.getLatLongList(request);
		log.info("Response Recieved from getLatLongList Service:- "+ dashboardData);
		UsertotaldistLocationDto totaldistance=distanceListCalculationService.getTotalDistance(dashboardData);
		log.info("Response Recieved from getTotalDistance Service:- "+ totaldistance);
		
		return ResponseEntity.ok(restUtils.wrapResponse(totaldistance, "Total Distance Covered is:- "+totaldistance.getTotalDistance()));
	}

}
