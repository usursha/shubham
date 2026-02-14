package com.hpy.ops360.ticketing.fiegnclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.ticketing.feignclient.request.dto.UsernameDto;
import com.hpy.ops360.ticketing.response.CeUserMobileResponse;

@FeignClient(name = "uam-service", url = "${uam-service.url}")
public interface UamFeignClient {
	
	@PostMapping("/userMobile")
//	ResponseEntity<ResponseDto<UserMobileDto>> getMobile(@RequestHeader("Authorization") String authorizationHeader,@RequestBody UsernameDto usernameDto);
	ResponseEntity<CeUserMobileResponse> getMobile(@RequestHeader("Authorization") String authorizationHeader,@RequestBody UsernameDto usernameDto);

}
