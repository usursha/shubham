package com.hpy.mappingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.request.dto.TemporaryCeManualAssignReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualAtmRequestDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualCeListReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualFilterReqDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualAtmResponseDto2;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListResDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualFilterResDto;
import com.hpy.mappingservice.service.TemporaryCeManualMappingService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/portal/temporary-ce-mapping")
@CrossOrigin("${app.cross-origin.allow}")
public class TemporaryCeManualMappingController {
	
	@Autowired
	private RestUtilImpl restUtils;
	
	@Autowired
	private TemporaryCeManualMappingService service;
	
	
	@PostMapping("/manual-mapping-atm-details")
	public ResponseEntity <IResponseDto> getDateRange(@RequestBody TemporaryCeManualAtmRequestDto request){
		TemporaryCeManualAtmResponseDto2 result = service.getCeMappingDetailsData(request);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}
	
	@PostMapping("/manual-mapping-ce-list")
	public ResponseEntity <IResponseDto> getCeList(@RequestBody TemporaryCeManualCeListReqDto request){
		TemporaryCeManualCeListResDto result = service.getTempMappingCeList(request);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}
	
	@PostMapping("/manual-mapping-ce-filter")
	public ResponseEntity <IResponseDto> getCeFilter(@RequestBody TemporaryCeManualFilterReqDto request){
		TemporaryCeManualFilterResDto result = service.getManualCeMappingFilter(request);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
	}
	
    @PostMapping("/assign-manual")
    public ResponseEntity<IResponseDto> assignManualATMs(@RequestBody TemporaryCeManualAssignReqDto request) {
        log.info("******* Inside assignManualATMs Method *********");
        String result = service.assignManualMappings(request);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
    }
	

}
