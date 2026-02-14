package com.MapPUC.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MapPUC.dto.UserdistLocationDto;
import com.MapPUC.dto.UserdistLocationRequest;
import com.MapPUC.dto.UsertotaldistLocationDto;
import com.MapPUC.service.DistanceCalculateService;
import com.MapPUC.service.DistanceCalculationService;
import com.MapPUC.service.DistanceListCalculationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/location")
@Slf4j
@AllArgsConstructor
public class UserLocationController {
	
	private DistanceListCalculationService distanceListCalculationService;
	private DistanceCalculateService distanceCalculateService;
	private DistanceCalculationService totaldistanceCalculationService;
	

//	@PostMapping("/adduserlocation")
//	public ResponseEntity<?> addUserLocationDetails(@RequestBody UserLocationDto userLocationDto) {
//		try {
//
//			return ResponseEntity.ok(userLocationService.addUserLocationDetails(userLocationDto));
//
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save data");
//		}
//
//	}
	
	@PostMapping("/lat_long_list")
	public ResponseEntity<UsertotaldistLocationDto> getLatLongList(@RequestBody UserdistLocationRequest request) {
		log.info("Inside getLatLongList controller");
		log.info("Request Recieved:- "+ request);
		List<UserdistLocationDto> dashboardData=distanceCalculateService.getLatLongList(request);
		log.info("Response Recieved from getLatLongList Service:- "+ dashboardData);
		UsertotaldistLocationDto totaldistance=distanceListCalculationService.getTotalDistance(dashboardData);
		log.info("Response Recieved from getTotalDistance Service:- "+ totaldistance);
		return ResponseEntity.ok(totaldistance);
	}
	
	@GetMapping("/lat_long_list-test")
	public ResponseEntity<UsertotaldistLocationDto> getLatLongListdata() {
		log.info("Inside getLatLongList controller");
//		log.info("Request Recieved:- "+ request);
//		List<UserdistLocationDto> dashboardData=distanceCalculateService.getLatLongList(request);
//		log.info("Response Recieved from getLatLongList Service:- "+ dashboardData);
		UsertotaldistLocationDto totaldistance=totaldistanceCalculationService.getTotalDistanceAndTime();
		log.info("Response Recieved from getTotalDistance Service:- "+ totaldistance);
		return ResponseEntity.ok(totaldistance);
//		return ResponseEntity.ok(restUtils.wrapResponse(totaldistance, "Total Distance Covered for User:- "+ request.getUsername()+" on date:- "+ request.getCreated_on()));
	}

}
