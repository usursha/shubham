package com.hpy.ops360.ticketing.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.dto.AtmIdDto;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.HpyMatrixRespDto;
import com.hpy.ops360.ticketing.dto.VendorIdDto;
import com.hpy.ops360.ticketing.dto.VendorMatrixRespDto;
import com.hpy.ops360.ticketing.request.AgentCallReq;
import com.hpy.ops360.ticketing.request.HpyDetailsRequest;
import com.hpy.ops360.ticketing.request.VendorDetailsReq;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.service.HpyMatrixService;
import com.hpy.ops360.ticketing.service.VendorMatrixService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v2/app/matrix")
@AllArgsConstructor
@Slf4j
@CrossOrigin("${app.cross-origin.allow}")
public class AppMatrixController {

	private HpyMatrixService hpyMatrixService;

	private VendorMatrixService vendorMatrixService;
	
	@Autowired
	private RestUtils restUtils;

	@PostMapping("/hpyMatrix/list")
	public ResponseEntity<IResponseDto> getHpyMatrixDetails(@RequestBody AtmIdDto atmIdDto) {
	    log.info("***** Inside getHpyMatrixDetails *****");
	    log.info("Request Received: " + atmIdDto);
	    
	    // Call the service to get HPY Matrix details
	    HpyMatrixRespDto result = hpyMatrixService.getHpyMatrixDetails(atmIdDto.getAtmId());
	    
	    log.info("Response Returned from getHpyMatrixDetails: " + result);
	    
	    // Wrap the response in IResponseDto
	    return ResponseEntity.ok(restUtils.wrapResponse(result, "HPY Matrix details fetched successfully"));
	}


	@PostMapping("/vendor-matrix/list")
	public ResponseEntity<IResponseDto> getVendorMatrixDetails(@RequestBody VendorIdDto vendorIdDto) {
	    log.info("***** Inside getVendorMatrixDetails *****");
	    log.info("Request Received: " + vendorIdDto);
	    
	    // Call the service to get Vendor Matrix details
	    VendorMatrixRespDto result = vendorMatrixService.getVendorMatrixDetails(vendorIdDto.getVendor(), vendorIdDto.getAtmId());
	    
	    log.info("Response Returned from getVendorMatrixDetails: " + result);
	    
	    // Wrap the response in IResponseDto
	    return ResponseEntity.ok(restUtils.wrapResponse(result, "Vendor Matrix details fetched successfully"));
	}


	@PostMapping("/call-vendor")
	public ResponseEntity<IResponseDto> callVendor(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, 
	                                                @RequestBody VendorDetailsReq vendorDetailsReq) {
	    log.info("***** Inside callVendor *****");
	    log.info("Request Received: " + vendorDetailsReq);
	    log.info("Authorization Token: " + token);
	    
	    // Call the service to make a vendor call
	    GenericResponseDto response = vendorMatrixService.callVendor(vendorDetailsReq, token);
	    
	    log.info("Response Returned from callVendor: " + response);
	    
	    // Wrap the response in IResponseDto using restUtils
	    return ResponseEntity.ok(restUtils.wrapResponse(response, "Vendor call completed successfully"));
	}

	
	@PostMapping("/call-hpy")
	public ResponseEntity<IResponseDto> callHpy(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, 
	                                             @RequestBody HpyDetailsRequest hpyDetailsReq) {
	    log.info("***** Inside callHpy *****");
	    log.info("Request Received: " + hpyDetailsReq);
	    log.info("Authorization Token: " + token);
	    GenericResponseDto response = hpyMatrixService.callHpy(hpyDetailsReq, token);
	    
	    log.info("Response Returned from callHpy: " + response);
	    return ResponseEntity.ok(restUtils.wrapResponse(response, "HPY call completed successfully"));
	}

	
	@PostMapping("/call-agent")
	public ResponseEntity<IResponseDto> callAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, 
	                                               @RequestBody AgentCallReq agentCallReq) {
	    log.info("***** Inside callAgent *****");
	    log.info("Request Received: " + agentCallReq);
	    log.info("Authorization Token: " + token);
	    ResponseMessage response = hpyMatrixService.callCustomer(agentCallReq.getAgentContactNumber(), token);
	    log.info("Response Returned from callAgent: " + response);
	    return ResponseEntity.ok(restUtils.wrapResponse(response, "Agent call completed successfully"));
	}


}
