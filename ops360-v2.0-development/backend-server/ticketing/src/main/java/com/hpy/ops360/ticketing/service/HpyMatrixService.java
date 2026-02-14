package com.hpy.ops360.ticketing.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.HpyMatrixDto;
import com.hpy.ops360.ticketing.dto.HpyMatrixRespDto;
import com.hpy.ops360.ticketing.entity.HpyMatrix;
import com.hpy.ops360.ticketing.entity.HpyMatrixDetails;
import com.hpy.ops360.ticketing.feignclient.request.dto.CallRequestDto;
import com.hpy.ops360.ticketing.feignclient.request.dto.UsernameDto;
import com.hpy.ops360.ticketing.fiegnclient.KnowlarityFeignClient;
import com.hpy.ops360.ticketing.fiegnclient.UamFeignClient;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.HpyMatrixDetailsRepository;
import com.hpy.ops360.ticketing.repository.HpyMatrixRepository;
import com.hpy.ops360.ticketing.request.HpyDetailsRequest;
import com.hpy.ops360.ticketing.response.CeUserMobileResponse;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.utils.CountryCode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class HpyMatrixService {

	private HpyMatrixRepository hpyMatrixRepository;

	private LoginService loginService;
	
	private HpyMatrixDetailsRepository hpyMatrixDetailsRepository;
	
	private UamFeignClient uamFeignClient;
	
	private KnowlarityFeignClient knowlarityFeignClient;

//	private HpyMatrixMapper hpyMatrixMapper;

	public HpyMatrixRespDto getHpyMatrixDetails(String atmId) {

		List<HpyMatrix> hpyMatrixList = hpyMatrixRepository.getHpyMatrixDetails(atmId, loginService.getLoggedInUser());
		HpyMatrixRespDto hpyMatrixRespDto = new HpyMatrixRespDto();
		if (!hpyMatrixList.isEmpty()) {
			hpyMatrixRespDto.setData(new ArrayList<>());
			Map<Integer, List<HpyMatrixDto>> matrixMap = new HashMap<>();
//			HpyMatrixDto hpyMatrixDto = new HpyMatrixDto();
			for (HpyMatrix hpyMatrix : hpyMatrixList) {
				if (!matrixMap.containsKey(hpyMatrix.getLevel())) {
					matrixMap.put(hpyMatrix.getLevel(), new ArrayList<>());
				}
				matrixMap.get(hpyMatrix.getLevel())
						.add(HpyMatrixDto.builder().category(hpyMatrix.getCategory())
								.contactPerson(hpyMatrix.getContactPerson()).email(hpyMatrix.getEmail())
								.phoneNumber(hpyMatrix.getPhoneNumber()).build());
			}
			for (Entry<Integer, List<HpyMatrixDto>> matrix : matrixMap.entrySet()) {
				hpyMatrixRespDto.getData()
						.add(new com.hpy.ops360.ticketing.dto.HpyMatrixLevelDto(matrix.getKey(), matrix.getValue()));
			}
			hpyMatrixRespDto.setLocation(hpyMatrixList.get(0).getLocation());
			return hpyMatrixRespDto;
		}
		hpyMatrixRespDto.setData(Collections.emptyList());
		hpyMatrixRespDto.setLocation("");
		return hpyMatrixRespDto;
	}

	public GenericResponseDto callHpy(HpyDetailsRequest hpyDetailsReq, String token) {
		HpyMatrixDetails hpyMatrixDetails = hpyMatrixDetailsRepository.getHpyMatrixDetails(hpyDetailsReq.getAtmId(),loginService.getLoggedInUser(),hpyDetailsReq.getLevel(),hpyDetailsReq.getHpyName());
		if (hpyMatrixDetails==null) {
			return new GenericResponseDto("failed", "Hpy Agent Number Not Found");
		}
		
//		ResponseEntity<ResponseDto<UserMobileDto>> uamResponse = uamFeignClient.getMobile(token, UsernameDto.builder().username(loginService.getLoggedInUser()).build());
		ResponseEntity<CeUserMobileResponse> uamResponse = uamFeignClient.getMobile(token, UsernameDto.builder().username(loginService.getLoggedInUser()).build());
		if (uamResponse.getStatusCode()== HttpStatus.OK) {
			
			if (uamResponse.getBody().getData()==null) {
				return new GenericResponseDto("failed", "Ce Number Not Found");
			}
			ResponseEntity<?> response = knowlarityFeignClient.makeCall(CallRequestDto.builder().agentNumber(CountryCode.addCountryCode(uamResponse.getBody().getData().getMobile())).customerNumber(CountryCode.addCountryCode(hpyMatrixDetails.getContactNumber())).build(),token);
			if (response.getStatusCode()== HttpStatus.OK) {
				return new GenericResponseDto("success", "Call Placed Successfully");
			}
		}
		return new GenericResponseDto("failed", "Call Not Placed");
	}

	public ResponseMessage callCustomer(String customerNumber, String token) {
		if (customerNumber==null || customerNumber.isEmpty()) {
			return ResponseMessage.builder().message("Provide Agent Contact Details").build();			
		}
		
		ResponseEntity<CeUserMobileResponse> uamResponse = uamFeignClient.getMobile(token, UsernameDto.builder().username(loginService.getLoggedInUser()).build());
		if (uamResponse.getStatusCode()== HttpStatus.OK) {
			
			if (uamResponse.getBody().getData()==null) {
				return ResponseMessage.builder().message("Ce Number Not Found").build();	
			}
			ResponseEntity<?> response = knowlarityFeignClient.makeCall(CallRequestDto.builder().agentNumber(uamResponse.getBody().getData().getMobile().startsWith("+91")?uamResponse.getBody().getData().getMobile():CountryCode.addCountryCode(uamResponse.getBody().getData().getMobile())).customerNumber(customerNumber.startsWith("+91")?customerNumber:CountryCode.addCountryCode(customerNumber)).build(),token);
			if (response.getStatusCode()== HttpStatus.OK) {
				return ResponseMessage.builder().message("Call Placed Successfully").build();
			}
		}
		return ResponseMessage.builder().message("Call Not Placed").build();
	}

}
