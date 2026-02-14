package com.hpy.mappingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.request.dto.ATMExtendRequestDTO;
import com.hpy.mappingservice.request.dto.ATMResettingRequestDTO;
import com.hpy.mappingservice.response.dto.ActiveLeaveUserDto;
import com.hpy.mappingservice.response.dto.ActivecombLeaveUserDto;
import com.hpy.mappingservice.response.dto.LeaveFilterResponseDto;
import com.hpy.mappingservice.response.dto.PrimaryExecutiveDto;
import com.hpy.mappingservice.response.dto.ResetReasonDropdownDto;
import com.hpy.mappingservice.service.ActiveLeaveUserService;
import com.hpy.mappingservice.service.GraphMappingService;
import com.hpy.mappingservice.service.ResetReasonDropdownService;
import com.hpy.mappingservice.service.bulkservice.ATMMappingAltService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/portal/leave-users")
@Slf4j
public class LeaveUserController {

	@Autowired
	private ActiveLeaveUserService activeleaveUserService;

	@Autowired
	private GraphMappingService graphMappingService;

	@Autowired
	private ATMMappingAltService service;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private ResetReasonDropdownService dropdownService;

	@GetMapping("/active")
	public ResponseEntity<IResponseDto> getActiveLeaveUsers() {
		log.info("API call: /leave-users/active for manager {}");
		List<ActiveLeaveUserDto> users = activeleaveUserService.getActiveLeaveUsers();

		List<PrimaryExecutiveDto> executives = graphMappingService.getPrimaryExecutivesWithTemporaryMappings();

		ActivecombLeaveUserDto data = new ActivecombLeaveUserDto();

		data.setUserdata(users);
		data.setGraphdata(executives);
		return ResponseEntity
				.ok(restUtils.wrapResponse(data, "Successfully fetched active leave users and graph data"));
	}

	@GetMapping("/reasons-dropdown")
	public ResponseEntity<IResponseDto> getDropdownReasons() {
		log.info("API call: /leave-users/reasons-dropdown");
		List<ResetReasonDropdownDto> response=dropdownService.getActiveReasonDtos();
		return ResponseEntity
				.ok(restUtils.wrapResponse(response, "Successfully fetched dropdown list")); 
	}

	@GetMapping("/leave-counts")
	public ResponseEntity<IResponseDto> getLeaveCounts() {
		log.info("API call: /leave-users/leave-counts");
		LeaveFilterResponseDto counts = activeleaveUserService.getLeaveFilters();
		return ResponseEntity.ok(restUtils.wrapResponse(counts, "Successfully fetched leave counts"));
	}

	@PostMapping("/reset")
	public ResponseEntity<IResponseDto> resetCEMapping(@RequestBody ATMResettingRequestDTO request) {
		log.info("API call: /leave-users/reset");
		String response = service.resetMappings(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully Reset Mappings"));
	}
	
	@PostMapping("/extend")
	public ResponseEntity<IResponseDto> extendCEMapping(@RequestBody List<ATMExtendRequestDTO> request) {
		log.info("API call: /leave-users/extend");
		String response = service.extendMappings(request);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Successfully Extended Mappings"));
	}

}
