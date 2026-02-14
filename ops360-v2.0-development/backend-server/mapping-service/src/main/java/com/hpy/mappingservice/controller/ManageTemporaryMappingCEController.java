package com.hpy.mappingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.request.dto.ManageUpcomingLeaveCeAtmDetailsReqDto;
import com.hpy.mappingservice.request.dto.ManageUpcomingLeaveCeSubmitReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualCeListReqDto;
import com.hpy.mappingservice.response.dto.LeaveFilterResponseDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeaveCeAtmDetailsResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesCeAtmFilterResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesCeResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesResDto2;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListResDto;
import com.hpy.mappingservice.service.ManageTemporaryMappingCEService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/portal/manage-temporary-ce")
@CrossOrigin("${app.cross-origin.allow}")
public class ManageTemporaryMappingCEController {
	

	@Autowired
	private RestUtilImpl restUtils;
	
	@Autowired
	private ManageTemporaryMappingCEService service;
	
	@GetMapping("/upcoming-leaves-ce-details")
	public ResponseEntity <IResponseDto> getTempCeData(){
		ManageUpcomingLeavesResDto2 result = service.getUpcomingLeavesDetails();
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}
	
	
	@GetMapping("/upcoming-leave-filter")
	public ResponseEntity <IResponseDto> getFilterData(){
		LeaveFilterResponseDto counts = service.getLeaveFilters();
	    return ResponseEntity.ok(restUtils.wrapResponse(counts, "Successfully fetched leave counts"));
	}
	
	
	@PostMapping("/upcoming-leave-ce-atmDetails")
	public ResponseEntity <IResponseDto> getMappedAtmDetails(@RequestBody ManageUpcomingLeaveCeAtmDetailsReqDto req){
		List<ManageUpcomingLeaveCeAtmDetailsResDto> result = service.getAtmDetails(req);
	    return ResponseEntity.ok(restUtils.wrapResponse(result, "Successfully fetched ATM counts"));

	}
	
	
	@PostMapping("/upcoming-modify-primaryCe-Details")
	public ResponseEntity <IResponseDto> getCeData(@RequestBody ManageUpcomingLeaveCeAtmDetailsReqDto req){
		ManageUpcomingLeavesCeResDto result = service.getModifyCeDetails(req);
		return ResponseEntity.ok(restUtils.wrapResponse(result, "Successfully fetched Primary Ce Details"));
	}
	
	
	@PostMapping("/upcoming-modify-secondaryCe-list")
	public ResponseEntity <IResponseDto> getCeList(@RequestBody TemporaryCeManualCeListReqDto request){
		TemporaryCeManualCeListResDto result = service.getTempMappingCeList(request);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}
	
	@PostMapping("/upcoming-leave-modify-filter")
	public ResponseEntity<IResponseDto> getAtmFilterData(@RequestBody ManageUpcomingLeaveCeAtmDetailsReqDto req) {
	    log.info("🔔 Received request for upcoming leave ATM filter: {}", req);

	    ManageUpcomingLeavesCeAtmFilterResDto counts = service.getModifyAtm(req);

	    log.info("✅ Successfully fetched ATM filter data for CE: {}", req.getCeUserId());
	    return ResponseEntity.ok(restUtils.wrapResponse(counts, "Successfully fetched leave counts"));
	}

	
	@PostMapping("/submit")
	public ResponseEntity <IResponseDto> getSubmitData(@RequestBody ManageUpcomingLeaveCeSubmitReqDto request){
		String result = service.submitMappedAtmData(request);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}
	

	
	
	
	
	
}
