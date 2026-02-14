package com.hpy.mappingservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.automapping.responseDto.AutoAssignResponseDto;
import com.hpy.mappingservice.automapping.service.AtmReassignmentService;
import com.hpy.mappingservice.request.dto.ATMReassignmentRequest;
import com.hpy.mappingservice.response.dto.ATMReassignmentResponse;
import com.hpy.mappingservice.response.dto.TemporaryMappingLeaveListResponseDto;
import com.hpy.mappingservice.service.ATMAssignmentService;
import com.hpy.mappingservice.service.ATMAssignmentService_ManualAssign;
import com.hpy.mappingservice.service.LoginService;
import com.hpy.mappingservice.service.TemporaryMappingLeaveListService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/mapping")
@CrossOrigin("${app.cross-origin.allow}")
public class TemporaryMapping {

	@Autowired
	private LoginService loginService;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private TemporaryMappingLeaveListService temporaryMappingLeaveListService;

	@Autowired
	private ATMAssignmentService atmAssignmentService;

	@Autowired
	private ATMAssignmentService_ManualAssign service_ManualAssign;
	

	@Autowired
	private AtmReassignmentService atmReassignmentService;


	@GetMapping("/Temporary-Mapping-Leave-List")
	public ResponseEntity<IResponseDto> getUserLeaveDetailsByParam() {

		List<TemporaryMappingLeaveListResponseDto> userLeaveDetails = temporaryMappingLeaveListService
				.getUserLeaveDetails();

		return ResponseEntity.ok(restUtils.wrapResponse(userLeaveDetails, "OK"));
	}

	@PostMapping("/reassign")
	public ResponseEntity<IResponseDto> reassignATMs(@RequestBody ATMReassignmentRequest request) {

		List<ATMReassignmentResponse> reassignments = atmAssignmentService.reassignATMs(request);
		return ResponseEntity.ok(restUtils.wrapResponse(reassignments, "Sucessfull"));
	}

	@GetMapping("/manual-reassignATM/{atmId}")
	public ResponseEntity<IResponseDto> manualReassignATM(@PathVariable String atmId) {

		try {
			List<ATMReassignmentResponse> reassignments = service_ManualAssign.manualReassignSingleATM(atmId);
			return ResponseEntity.ok(restUtils.wrapResponse(reassignments, "Sucessfull"));

		} catch (RuntimeException e) {
			// Handle specific business exceptions
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			// Handle unexpected errors
			return ResponseEntity.internalServerError().build();
		}
	}
	
	 @GetMapping("/auto-assign/{originalCEUserId}")
		public ResponseEntity<IResponseDto>getAtmAutoAssign(@PathVariable String originalCEUserId) {
			List<AutoAssignResponseDto> autoassign = atmReassignmentService.getAtmAutoAssignByOriginalCEUserId(originalCEUserId);
			return ResponseEntity.ok(restUtils.wrapResponse(autoassign, "Sucessfull"));
			
		}


}
