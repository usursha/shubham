package com.hpy.mappingservice.controller;


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

import com.hpy.mappingservice.request.dto.PermanentCEFilterRequestDto;
import com.hpy.mappingservice.request.dto.PrimaryCeAtmDetailsRequestDto;
import com.hpy.mappingservice.request.dto.SecondaryCeDetailsRequestDto;
import com.hpy.mappingservice.request.dto.SubmitPermanentMappingRequestDto;
import com.hpy.mappingservice.response.dto.ExitedCeMappingResponseDto;
import com.hpy.mappingservice.response.dto.PermanentCEFilterResponseDto;
import com.hpy.mappingservice.response.dto.PrimaryCeAtmDetailsResponseDto;
import com.hpy.mappingservice.response.dto.SecondaryCeDetailsResponseDto;
import com.hpy.mappingservice.service.ExitedCeMappingService;
import com.hpy.mappingservice.service.LoginService;
import com.hpy.mappingservice.service.MapperService;
import com.hpy.mappingservice.service.MasterService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/portal/permanent")
@CrossOrigin("${app.cross-origin.allow}")
public class PermanentMappingController {
	
	private MapperService mapperService;
	
	@Autowired
	private RestUtilImpl restUtils;
	
	@Autowired 
	private LoginService loginService;
	
	@Autowired
	private MasterService masterService;
	
	@Autowired
	private ExitedCeMappingService exitedCeMappingService;
	
	
	 @GetMapping("/exited-ce-mapping-list")
		public ResponseEntity<IResponseDto> getExitedEeMappingList() {
	    	log.info("******* Inside getExitedEeMappingList Method *********");
			List<ExitedCeMappingResponseDto> exitedCeMappingList = exitedCeMappingService.getExitedCeMappingList();
			return ResponseEntity.ok(restUtils.wrapResponse(exitedCeMappingList, "success"));
		}
	 
	 @GetMapping("/exited-ce-mapping/primary-ce-details/{ceId}")
		public ResponseEntity<IResponseDto> getPrimaryceDetails(@PathVariable String ceId) {
	    	log.info("******* Inside getPrimaryceDetails Method *********");
			ExitedCeMappingResponseDto primaryceDetails = exitedCeMappingService.getPrimaryceDetails(ceId);
			return ResponseEntity.ok(restUtils.wrapResponse(primaryceDetails, "success"));
		}

	 @PostMapping("/exited-ce-mapping/secondary-ce-details")
		public ResponseEntity<IResponseDto> getSecondaryCeDetailsList(@RequestBody SecondaryCeDetailsRequestDto requesrDto) {
	    	log.info("******* Inside getSecondaryCeDetails Method *********");
			List<SecondaryCeDetailsResponseDto> secondaryCeDetailsList = exitedCeMappingService.getSecondaryCeDetailsList(requesrDto);
			return ResponseEntity.ok(restUtils.wrapResponse(secondaryCeDetailsList, "success"));
		}
	 
	 
	 @PostMapping("/exited-ce-mapping/primary-ceAtm-details")
		public ResponseEntity<IResponseDto> getPrimaryCeAtmDetailsList(@RequestBody PrimaryCeAtmDetailsRequestDto requesrDto) {
	    	log.info("******* Inside getPrimaryCeAtmDetailsList Method *********");
		List<PrimaryCeAtmDetailsResponseDto> primaryCeAtmDetailsList = exitedCeMappingService.getPrimaryCeAtmDetailsList(requesrDto);
			return ResponseEntity.ok(restUtils.wrapResponse(primaryCeAtmDetailsList, "success"));
		}
	
	
	 @PostMapping("/exited-ce-mapping/ce-filter")
		public ResponseEntity<IResponseDto> getPermanentCeFilteredList(@RequestBody PermanentCEFilterRequestDto requestDto ) {
	    	log.info("******* Inside getPermanentCeFilteredList Method *********");
	    	PermanentCEFilterResponseDto getfilterData = exitedCeMappingService.getPermanentCEFilterData(requestDto);
			return ResponseEntity.ok(restUtils.wrapResponse(getfilterData, "success"));
		}
	 
	 
	 @PostMapping("/exited-ce-mapping/submit-mapping")
		public ResponseEntity<IResponseDto> submitPermanentCeAtmMapping(@RequestBody SubmitPermanentMappingRequestDto requestDto ) {
	    	log.info("******* Inside submitPermanentCeAtmMapping Method *********");
	    	String submitPermanentCEMapping = exitedCeMappingService.submitPermanentCEMapping(requestDto);
			return ResponseEntity.ok(restUtils.wrapResponse(submitPermanentCEMapping, "success"));
		}

}
