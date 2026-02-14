package com.hpy.ops360.ticketing.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.VendorMatrixDto;
import com.hpy.ops360.ticketing.dto.VendorMatrixLevelDto;
import com.hpy.ops360.ticketing.dto.VendorMatrixRespDto;
import com.hpy.ops360.ticketing.entity.VendorMatrix;
import com.hpy.ops360.ticketing.entity.VendorMatrixDetails;
import com.hpy.ops360.ticketing.feignclient.request.dto.CallRequestDto;
import com.hpy.ops360.ticketing.feignclient.request.dto.UsernameDto;
import com.hpy.ops360.ticketing.fiegnclient.KnowlarityFeignClient;
import com.hpy.ops360.ticketing.fiegnclient.UamFeignClient;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.VendorMatrixDetailsRepository;
import com.hpy.ops360.ticketing.repository.VendorMatrixRepository;
import com.hpy.ops360.ticketing.request.VendorDetailsReq;
import com.hpy.ops360.ticketing.response.CeUserMobileResponse;
import com.hpy.ops360.ticketing.utils.CountryCode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class VendorMatrixService {

	private VendorMatrixRepository vendorMatrixRepository;
	
	private VendorMatrixDetailsRepository vendorMatrixDetailsRepository;
	
	private KnowlarityFeignClient knowlarityFeignClient;
	
	private LoginService loginService;
	
	private UamFeignClient uamFeignClient;

	public VendorMatrixRespDto getVendorMatrixDetails(String vendor, String atmId) {
		List<VendorMatrix> vendorMatrices = vendorMatrixRepository.getVendorMatrixDetails(vendor, atmId);
		VendorMatrixRespDto vendorMatrixDto = new VendorMatrixRespDto();
		if (!vendorMatrices.isEmpty()) {
			vendorMatrixDto.setData(new ArrayList<>());
			Map<Integer, List<VendorMatrixDto>> vendorMap = new HashMap<>();
			for (VendorMatrix vendorMatrix : vendorMatrices) {
				if (!vendorMap.containsKey(vendorMatrix.getLevel())) {
					vendorMap.put(vendorMatrix.getLevel(), new ArrayList<>());
				}
				vendorMap.get(vendorMatrix.getLevel())
						.add(VendorMatrixDto.builder().category(vendorMatrix.getCategory())
								.contactPerson(vendorMatrix.getContactPerson()).email(vendorMatrix.getEmail()).phoneNumber(vendorMatrix.getPhoneNumber())
								.build());
			}
			for (Map.Entry<Integer, List<VendorMatrixDto>> matrix : vendorMap.entrySet()) {
				vendorMatrixDto.getData().add(new VendorMatrixLevelDto(matrix.getKey(), matrix.getValue()));

			}
			vendorMatrixDto.setLocation(vendorMatrices.get(0).getLocation());
			return vendorMatrixDto;
		}
		vendorMatrixDto.setData(Collections.emptyList());
		vendorMatrixDto.setLocation("");
		return vendorMatrixDto;
	}
	
	public GenericResponseDto callVendor(VendorDetailsReq vendorDetailsReq,String token)
	{
		VendorMatrixDetails vendorMatrixDetails = vendorMatrixDetailsRepository.getVendorContactDetails(vendorDetailsReq.getVendor(), vendorDetailsReq.getAtmId(), vendorDetailsReq.getVendorName());
		if (vendorMatrixDetails==null) {
			return new GenericResponseDto("failed", "Vendor Number Not Found");
		}
		
//		ResponseEntity<ResponseDto<UserMobileDto>> uamResponse = uamFeignClient.getMobile(token, UsernameDto.builder().username(loginService.getLoggedInUser()).build());
		ResponseEntity<CeUserMobileResponse> uamResponse = uamFeignClient.getMobile(token, UsernameDto.builder().username(loginService.getLoggedInUser()).build());
		if (uamResponse.getStatusCode()== HttpStatus.OK) {
			
			if (uamResponse.getBody().getData()==null) {
				return new GenericResponseDto("failed", "Ce Number Not Found");
			}
			ResponseEntity<?> response = knowlarityFeignClient.makeCall(CallRequestDto.builder().agentNumber(CountryCode.addCountryCode(vendorMatrixDetails.getContactNumber())).customerNumber(CountryCode.addCountryCode(uamResponse.getBody().getData().getMobile())).build(),token);
			if (response.getStatusCode()== HttpStatus.OK) {
				return new GenericResponseDto("success", "Call Placed Successfully");
			}
		}
		return new GenericResponseDto("failed", "Call Not Placed");
	}

	
	
}
