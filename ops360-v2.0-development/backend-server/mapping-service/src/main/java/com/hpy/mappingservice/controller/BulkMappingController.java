package com.hpy.mappingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.bulkdto.ATMMappingRequestDTO;
import com.hpy.mappingservice.request.CE_userId;
import com.hpy.mappingservice.request.dto.CE_unmapped_atm_dist;
import com.hpy.mappingservice.response.dto.DistinctATMMetadataDTO;
import com.hpy.mappingservice.response.dto.LeaveUserDto;
import com.hpy.mappingservice.response.dto.MappedUserDto;
import com.hpy.mappingservice.response.dto.UnmappedATMResponseDTO;
import com.hpy.mappingservice.service.LeaveUserService;
import com.hpy.mappingservice.service.MappedUserService;
import com.hpy.mappingservice.service.UnmappedATMService;
import com.hpy.mappingservice.service.bulkservice.ATMMappingAltService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/portal/bulk-mapping")
@Slf4j
public class BulkMappingController {

	@Autowired
	private RestUtilImpl restUtils;

	@Autowired
	private LeaveUserService leaveUserService;

	@Autowired
	private MappedUserService mappedUserService;

	@Autowired
	private UnmappedATMService atmService;

	@Autowired
	private ATMMappingAltService service;

//	@GetMapping("/exited-ce-user-name")
//	public ResponseEntity<IResponseDto> getExitedEeMappingList() {
//		log.info("******* Inside getExitedEeMappingList Method *********");
//		List<LeaveUserDto> users = leaveUserService.getApprovedLeaveUsers();
//		List<String> ceNames = users.stream().map(LeaveUserDto::getFullName).distinct().toList();
//		return ResponseEntity.ok(restUtils.wrapResponseListOfString(ceNames, "successfully fetched exited User names"));
//	}

	@PostMapping("/temp-ce-leave-list")
	public ResponseEntity<IResponseDto> getApprovedLeaveUsers(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getApprovedLeaveUsers Method *********");
		List<LeaveUserDto> users = leaveUserService.getApprovedLeaveUsers(ceUserRequest.getExcludeCEUsername());
		return ResponseEntity.ok(restUtils.wrapResponse(users, "Successfully fetched exited user details"));
	}

	@PostMapping("/all-ce-userdetails")
	public ResponseEntity<IResponseDto> getAllCEUsers(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getAllCEUsers Method *********");
		List<MappedUserDto> users = mappedUserService.getMappedUsers(ceUserRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(users, "successfully fetched all User Details"));
	}

	@PostMapping("/unmapped-atms")
	public ResponseEntity<IResponseDto> getATMsByCE(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getATMsByCE Method *********");
		List<UnmappedATMResponseDTO> result = atmService.getATMsForCE(ceUserRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "successfully fetched all unmapped atm Details"));
	}

	@PostMapping("/filter")
	public ResponseEntity<IResponseDto> getfilterATMsByCE(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getfilterATMsByCE Method *********");
		DistinctATMMetadataDTO result = atmService.getFilterForCE(ceUserRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Successfully fetched distinct bank names and cities"));
	}

	@PostMapping("/unmapped/distance")
	public ResponseEntity<IResponseDto> getunmappedselectATMsByCE(
			@RequestBody CE_unmapped_atm_dist ce_unmapped_atm_dist) {
		log.info("******* Inside getunmappedselectATMsByCE Method *********");
		List<UnmappedATMResponseDTO> result = atmService.getunmappedATMsdistForCE(ce_unmapped_atm_dist);
		return ResponseEntity
				.ok(restUtils.wrapResponse(result, "successfully fetched all unmapped atm Details for distance"));
	}

	@PostMapping("/assign")
	public ResponseEntity<IResponseDto> assignATMs(@RequestBody ATMMappingRequestDTO request) {
		log.info("******* Inside getunmappedselectATMsByCE Method *********");
		String result = service.assignMappings(request);
		return ResponseEntity.ok(restUtils.wrapNullResponse(result, HttpStatus.OK));
	}
}
