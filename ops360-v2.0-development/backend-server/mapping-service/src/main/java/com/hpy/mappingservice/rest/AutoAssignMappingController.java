package com.hpy.mappingservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.automapping.requestDto.AutoAssignCeListRequestDto;
import com.hpy.mappingservice.automapping.responseDto.AutoAssignCeListResponseDto;
import com.hpy.mappingservice.automapping.responseDto.AutoAssignFilterResponseDto;
import com.hpy.mappingservice.automapping.responseDto.AutoAssignResponseDto;
import com.hpy.mappingservice.automapping.service.AtmReassignmentService;
import com.hpy.mappingservice.automapping.service.AutoAssignCeListService;
import com.hpy.mappingservice.automapping.service.AutoAssignFilterService;
import com.hpy.mappingservice.request.dto.ATMReassignmentRequest;
import com.hpy.mappingservice.request.dto.TemporaryCeManualAssignReqDto;
import com.hpy.mappingservice.response.dto.ATMReassignmentResponse;
import com.hpy.mappingservice.response.dto.LeaveUserDto;
import com.hpy.mappingservice.response.dto.TemporaryMappingLeaveListResponseDto;
import com.hpy.mappingservice.service.ATMAssignmentService;
import com.hpy.mappingservice.service.ATMAssignmentService_ManualAssign;
import com.hpy.mappingservice.service.AutoLeaveUserService;
import com.hpy.mappingservice.service.LoginService;
import com.hpy.mappingservice.service.TemporaryCeManualMappingService;
import com.hpy.mappingservice.service.TemporaryMappingLeaveListService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/portal/auto-mapping")
@CrossOrigin("${app.cross-origin.allow}")
public class AutoAssignMappingController {

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

	@Autowired
	private AutoAssignCeListService autoAssignCeListService;

	@Autowired
	private TemporaryCeManualMappingService service;

	@Autowired
	private AutoAssignFilterService autoAssignFilterService;

	@Autowired
	private AutoLeaveUserService autoLeaveUserService;

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
	public ResponseEntity<IResponseDto> getAtmAutoAssign(@PathVariable String originalCEUserId) {
		List<AutoAssignResponseDto> autoassign = atmReassignmentService
				.getAtmAutoAssignByOriginalCEUserId(originalCEUserId);
		return ResponseEntity.ok(restUtils.wrapResponse(autoassign, "Sucessfull"));

	}

	@GetMapping("/auto-assign-filter/{originalCEUserId}")
	public ResponseEntity<IResponseDto> getAutoAssignFilter(@PathVariable String originalCEUserId) {

		AutoAssignFilterResponseDto result = autoAssignFilterService.getAutoAssignFilterData(originalCEUserId);

		return ResponseEntity.ok(restUtils.wrapResponse(result, "Sucessfull"));
	}

	@PostMapping("/remaining-ceList")
	public ResponseEntity<IResponseDto> getAutoAssignRemainingCE(@RequestBody AutoAssignCeListRequestDto request) {

		List<AutoAssignCeListResponseDto> results = autoAssignCeListService
				.getAutoAssignRemainingCE(request.getCmUserId(), request.getExcludedCeUserId());
		return ResponseEntity.ok(restUtils.wrapResponse(results, "Sucessfull"));

	}

	@GetMapping("/temp-ce-leave-list")
	public ResponseEntity<IResponseDto> getApprovedLeaveUsers() {
		log.info("******* Inside getApprovedLeaveUsers Method *********");
		List<LeaveUserDto> users = autoLeaveUserService.getApprovedLeaveUsers();
		return ResponseEntity.ok(restUtils.wrapResponse(users, "successfully fetched exited User Details"));
	}

	 @PostMapping("/auto-assign")
	    public ResponseEntity<IResponseDto> assignManualATMs(@RequestBody TemporaryCeManualAssignReqDto request) {
	        log.info("******* Inside assignAutoATMs Method *********");
	        String result = service.assignManualMappings(request);
	        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	    }

}
