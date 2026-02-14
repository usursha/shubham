package com.hpy.ops360.ticketing.fiegnclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.ticketing.feignclient.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.ticketing.response.SynergyResponse;
import com.hpy.ops360.ticketing.response.UpdateSubcallTypeResp;
import com.hpy.rest.dto.ResponseDto;

@FeignClient(name = "synergy-service", url = "${synergy-service.url}")
public interface SynergyFeignClient {
	
	@PostMapping("/updateSubcallType")
//	ResponseEntity<ResponseDto<SynergyResponse>> updateSubcallType(@RequestHeader("Authorization") String authorizationHeader,@RequestBody UpdateSubcallTypeDto updateSubcallTypeDto);
	ResponseEntity<UpdateSubcallTypeResp> updateSubcallType(@RequestHeader("Authorization") String authorizationHeader,@RequestBody UpdateSubcallTypeDto updateSubcallTypeDto);

}
