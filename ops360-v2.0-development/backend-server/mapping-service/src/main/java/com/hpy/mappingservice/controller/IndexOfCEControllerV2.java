package com.hpy.mappingservice.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hpy.mappingservice.request.dto.AtmMappingRequest;
import com.hpy.mappingservice.request.dto.Base64FileRequestDto;
import com.hpy.mappingservice.request.dto.CEIndexRequestDto;
import com.hpy.mappingservice.request.dto.CeMappingRequest;
import com.hpy.mappingservice.request.dto.IndexOfCeRequestDto;
import com.hpy.mappingservice.request.dto.IndexOfCeSearchRequestDto;
import com.hpy.mappingservice.request.dto.MappingRecordReq;
import com.hpy.mappingservice.request.dto.SubmitAtmCeMappingDto;
import com.hpy.mappingservice.request.dto.UserDto;
import com.hpy.mappingservice.response.dto.CEAreaIndexResponseDTO;
import com.hpy.mappingservice.response.dto.CECityIndexResponseDTO;
import com.hpy.mappingservice.response.dto.CEIndexResponseDto;
import com.hpy.mappingservice.response.dto.CEIndexWrapperDto;
import com.hpy.mappingservice.response.dto.CEMTDTargetIndexResponseDTO;
import com.hpy.mappingservice.response.dto.CESearchIndexResponseDto;
import com.hpy.mappingservice.response.dto.FilteredCEResponseDto;
import com.hpy.mappingservice.service.CEIndexService;
import com.hpy.mappingservice.service.IndexOfCeService;
import com.hpy.mappingservice.service.LoginService;
import com.hpy.mappingservice.service.MapperService;
import com.hpy.mappingservice.service.MasterService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v2/portal/indexOfCE")
@CrossOrigin("${app.cross-origin.allow}")
public class IndexOfCEControllerV2 {
	
	private MapperService mapperService;
	private RestUtilImpl restUtils;
	
	@Autowired
	private CEIndexService searchIndexService;
	
	@Autowired 
	private LoginService loginService;
	
	@Autowired
	private MasterService masterService;
	
	@Autowired
	private IndexOfCeService indexOfCeService;
	
	
	public IndexOfCEControllerV2(MapperService mapperService, RestUtilImpl restUtils) {
		super();
		this.mapperService = mapperService;
		this.restUtils = restUtils;
	}



	@GetMapping("/get-all-ce-list")
	public ResponseEntity<IResponseDto> getAllCeList()
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.getAllCeList(), "Ce List Returned Successfully"));
	}
	
	@GetMapping("get-all-unassigned-atms")
	public ResponseEntity<IResponseDto> getAllCeUnassignedAtmList()
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.getAllCeUnassignedAtmList(), "Ce Unassigned Atm List Returned Successfully"));
	}
	
	@PostMapping("get-all-assigned-atms")
	public ResponseEntity<IResponseDto> getAllCeAssignedAtmList(@RequestBody UserDto userDto)
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.getAllAssignedAtmList(userDto.getUsername()), "Ce Assigned Atm List Returned Successfully"));
	}
	
	@PostMapping("update-atm-ce-mapping")
	public ResponseEntity<IResponseDto> updateAtmCeMapping(@RequestBody AtmMappingRequest request)
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.updateAtmCeMapping(request.getUsername(),request.getAssignedAtms(),request.getUnAssignedAtms()), "Ce Assigned and Unassigned Atm List Updated Successfully"));
	}
	
	@GetMapping("/get-all-cm-list")
	public ResponseEntity<IResponseDto> getAllCmList()
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.getAllCmList(), "Cm List Returned Successfully"));
	}
	
	@GetMapping("get-all-unassigned-ce")
	public ResponseEntity<IResponseDto> getAllUnassignedCeList()
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.getAllUnassignedCeList(), "Unassigned Ce List Returned Successfully"));
	}
	
	@PostMapping("get-all-assigned-ce")
	public ResponseEntity<IResponseDto> getAllAssignedCeList(@RequestBody UserDto userDto)
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.getAllAssignedCeList(userDto.getUsername()), "Assigned Ce List Returned Successfully"));
	}
	
	@PostMapping("update-ce-cm-mapping")
	public ResponseEntity<IResponseDto> updateCeCmMapping(@RequestBody CeMappingRequest request)
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.updateCeCmMapping(request.getUsername(),request.getAssignedCes(),request.getUnAssignedCes()), "Ce Assigned and Unassigned Atm List Updated Successfully"));
	}
	
	@PostMapping("verify-init-mapping-excel")
	public ResponseEntity<IResponseDto> verifyUpdatedCeMappingExcel(@RequestParam("file") MultipartFile file) throws IOException
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.verifyMappingExcel(file), "Excel Verification Api Successfully Completed"));
	}
	
	@PostMapping("verify-mapping-excel")
	public ResponseEntity<IResponseDto> verifyUpdatedCeMappingExcel(@RequestBody Base64FileRequestDto requestDto) throws IOException
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.convertBase64StringToMultipart(requestDto), "Excel Verification Api Successfully Completed"));
	}
	
	@PostMapping("verify-each-row")
	public ResponseEntity<IResponseDto> verifyEachRow(@RequestBody MappingRecordReq req) 
	{
		return ResponseEntity.ok(restUtils.wrapResponse(mapperService.verifyEachRow(req), "Record Verification Api Successfully Completed"));
	}
	
	@PostMapping("/submit-atm-ce-users-mapping")
	public ResponseEntity<IResponseDto> submitAtmMappings(@RequestBody List<SubmitAtmCeMappingDto> atmMappings) {
	    
	    return ResponseEntity.ok(restUtils.wrapResponse(mapperService.submitAtmCeMapping(atmMappings), "Submit Mapping Response Successfully completed "));
	}
	
	@PostMapping("/submit-atm-ce-users-mapping-excel")
	public ResponseEntity<IResponseDto> submitAtmMappings(@RequestParam("file") MultipartFile file) {
	    
	    return ResponseEntity.ok(restUtils.wrapResponse(mapperService.submitAtmCeMappingDataFromExcel(file), "Submit Mapping Response Successfully completed "));
	}
	@PostMapping("/ce-index-search") 
    public ResponseEntity<IResponseDto> getCeSearchIndexDetails() {
        log.info("Received request for CE Index search. Request DTO: {}");
            List<CESearchIndexResponseDto> result = searchIndexService.getCeSearchIndexDetails();
            log.info("Successfully fetched {} CE Index details.", result.size());
            return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
        
    }
	
	
    @PostMapping("/ce-index")
    public ResponseEntity<IResponseDto> getCeIndexDetails(@RequestBody CEIndexRequestDto reqDto) {
           List<CEIndexResponseDto> result = searchIndexService.getCeIndexDetails(reqDto);
              return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
        
    }
    

    @GetMapping("/ce-index-area") 
    public ResponseEntity<IResponseDto> getCEAreaIndexDetails() {
            List<CEAreaIndexResponseDTO> result = searchIndexService.getCEAreaIndexDetails();
          return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
        
    }
    

    @GetMapping("/ce-index-city") 
    public ResponseEntity<IResponseDto> getCECityIndexDetails() {
           List<CECityIndexResponseDTO> result = searchIndexService.getCECityIndexDetails();
           return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
        
    }

    @GetMapping("/ce-index-mtdTarget") 
    public ResponseEntity<IResponseDto> getCEMTDTargetIndexDetails() {
       List<CEMTDTargetIndexResponseDTO> result = searchIndexService.getCEMTDTargetIndexDetails();
            return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
        
    }
    
    @PostMapping("/index-of-ce")
    public ResponseEntity<IResponseDto> getIndexOfCeDetails(@RequestBody IndexOfCeRequestDto requestDto) {
    	log.info("******* Inside getIndexOfCeDetails Method *********");
		log.info("Request Recieved: " + requestDto);
        CEIndexWrapperDto result = indexOfCeService.getIndexOfCeDetails(requestDto);
        return ResponseEntity.ok(restUtils.wrapResponse(result, "Successful"));
    }
    
    @PostMapping("/index-of-ce-search") 
    public ResponseEntity<IResponseDto> getIndexOfCeSearchDetails(@RequestBody IndexOfCeSearchRequestDto requestDto) {
    	log.info("******* Inside getIndexOfCeSearchDetails Method *********");
		log.info("Request Recieved: " + requestDto);
        List<String> indexOfCeSearchDetails = indexOfCeService.getIndexOfCeSearchDetails(requestDto);
        log.info("Successfully fetched CE Index search data.");
        return ResponseEntity.ok(restUtils.wrapResponseListOfString(indexOfCeSearchDetails, "Successful"));
    }
    
    @GetMapping("/ce-filter")
	public ResponseEntity<IResponseDto> getFilteredList() {
    	log.info("******* Inside getFilteredList Method *********");
		FilteredCEResponseDto response=indexOfCeService.getfilterData();
		return ResponseEntity.ok(restUtils.wrapResponse(response, "success"));
	}
}
