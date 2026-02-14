package com.hpy.ops360.ticketing.fiegnclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hpy.ops360.ticketing.dto.LoginRequestDto;


//@FeignClient(name = "oauth-service", url = "http://localhost:9103/oauth")
@FeignClient(name = "oauth-service", url = "${oauth-service.url}")
public interface OAuthFeignClient {

    @PostMapping("/ce_login")
    ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto);
}

