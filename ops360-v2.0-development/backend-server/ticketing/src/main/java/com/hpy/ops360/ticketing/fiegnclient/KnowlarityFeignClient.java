package com.hpy.ops360.ticketing.fiegnclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.ticketing.feignclient.request.dto.CallRequestDto;



@FeignClient(name = "knowlarity-service", url = "${knowlarity-service.url}")
//@FeignClient(name = "knowlarity-service", url = "http://172.16.15.192:9207/oauth")
public interface KnowlarityFeignClient {
	
//	@PostMapping("/make-call")
//    ResponseEntity<?> makeCall(@RequestBody CallRequestDto requestDto);
	
	@PostMapping("/make-call")
    ResponseEntity<?> makeCall(@RequestBody CallRequestDto requestDto,@RequestHeader("Authorization") String authorizationHeader);

}
