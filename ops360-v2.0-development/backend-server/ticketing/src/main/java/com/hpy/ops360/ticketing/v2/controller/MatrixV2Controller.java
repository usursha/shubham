package com.hpy.ops360.ticketing.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.request.AgentCallReq;
import com.hpy.ops360.ticketing.request.HpyDetailsRequest;
import com.hpy.ops360.ticketing.request.VendorDetailsReq;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.service.HpyMatrixService;
import com.hpy.ops360.ticketing.service.VendorMatrixService;
import com.hpy.rest.dto.ResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v2/matrix")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class MatrixV2Controller {

	private HpyMatrixService hpyMatrixService;

	private VendorMatrixService vendorMatrixService;
	
	@Autowired
	private RestUtils restUtils;

	@PostMapping("/hpyMatrix/list")
	@Loggable
	public ResponseEntity<HpyMatrixRespDto> getHpyMatrixDetails(@RequestBody AtmIdDto atmIdDto) {
		return ResponseEntity.ok(hpyMatrixService.getHpyMatrixDetails(atmIdDto.getAtmId()));

	}

	@PostMapping("/vendorMatrix/list")
	@Loggable
	public ResponseEntity<VendorMatrixRespDto> getVendorMatrixDetails(@RequestBody VendorIdDto vendorIdDto) {
		return ResponseEntity
				.ok(vendorMatrixService.getVendorMatrixDetails(vendorIdDto.getVendor(), vendorIdDto.getAtmId()));

	}
	
	@PostMapping("/call-vendor")
	@Loggable
	public ResponseEntity<ResponseDto<GenericResponseDto>> callVendor(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody VendorDetailsReq vendorDetailsReq)
	{
		GenericResponseDto response= vendorMatrixService.callVendor(vendorDetailsReq,token);
		ResponseDto<GenericResponseDto> formattedResponse = new ResponseDto() {
		};
	    int statusCodeValue = HttpStatus.OK.value();
	    String statusMessage = "success";

	    formattedResponse.setResponseCode(statusCodeValue);
	    formattedResponse.setMessage(statusMessage);
	    formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
	}
	
	@PostMapping("/call-hpy")
	@Loggable
	public ResponseEntity<ResponseDto<GenericResponseDto>> callHpy(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody HpyDetailsRequest hpyDetailsReq)
	{
		GenericResponseDto response= hpyMatrixService.callHpy(hpyDetailsReq,token);
		ResponseDto<GenericResponseDto> formattedResponse = new ResponseDto() {
		};
	    int statusCodeValue = HttpStatus.OK.value();
	    String statusMessage = "success";

	    formattedResponse.setResponseCode(statusCodeValue);
	    formattedResponse.setMessage(statusMessage);
	    formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
	}
	
	@PostMapping("/call-agent")
	@Loggable
	public ResponseEntity<ResponseDto<ResponseMessage>> callAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody AgentCallReq agentCallReq)
	{
		ResponseMessage response= hpyMatrixService.callCustomer(agentCallReq.getAgentContactNumber(),token);
		ResponseDto<ResponseMessage> formattedResponse = new ResponseDto() {
		};
	    int statusCodeValue = HttpStatus.OK.value();
	    String statusMessage = "success";

	    formattedResponse.setResponseCode(statusCodeValue);
	    formattedResponse.setMessage(statusMessage);
	    formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
	}


}
