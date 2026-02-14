package com.hpy.mappingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.request.CE_userId;
import com.hpy.mappingservice.request.dto.SecondaryATMMappingRequestDTO;
import com.hpy.mappingservice.response.dto.DistinctATMMetadataDTO;
import com.hpy.mappingservice.response.dto.MappedUserDto;
import com.hpy.mappingservice.response.dto.UnmappedATMResponseDTO;
import com.hpy.mappingservice.service.MappedUserService;
import com.hpy.mappingservice.service.SecondaryATMMappingAltService;
import com.hpy.mappingservice.service.UnmappedATMService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/portal/secondary-mapping")
@Slf4j
public class SecondaryMapping {

	@Autowired
	private RestUtilImpl restUtils;

	@Autowired
	private UnmappedATMService atmService;

	@Autowired
	private MappedUserService mappedUserService;
	
	@Autowired
	private SecondaryATMMappingAltService service;
	
	@PostMapping("/unmapped-atms")
	public ResponseEntity<IResponseDto> getATMsByCE(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getATMsByCE Method *********");
		List<UnmappedATMResponseDTO> result = atmService.getATMsForSecondaryCE(ceUserRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "successfully fetched all unmapped atm Details"));
	}

	@PostMapping("/secondary-ce-userdetails")
	public ResponseEntity<IResponseDto> getAllCEUsers(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getAllCEUsers Method *********");
		MappedUserDto users = mappedUserService.getSecondaryMappedUsers(ceUserRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(users, "successfully fetched all User Details"));
	}

	@PostMapping("/filter")
	public ResponseEntity<IResponseDto> getfilterATMsByCE(@RequestBody CE_userId ceUserRequest) {
		log.info("******* Inside getfilterATMsByCE Method *********");
		DistinctATMMetadataDTO result = atmService.getFilterForSecondaryCE(ceUserRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Successfully fetched distinct bank names and cities"));
	}
	
	@PostMapping("/assign")
	public ResponseEntity<IResponseDto> assignATMs(@RequestBody SecondaryATMMappingRequestDTO request) {
		log.info("******* Inside getunmappedselectATMsByCE Method *********");
		String result = service.assignMappings(request);
		return ResponseEntity.ok(restUtils.wrapNullResponse(result, HttpStatus.OK));
	}
	
	
}
