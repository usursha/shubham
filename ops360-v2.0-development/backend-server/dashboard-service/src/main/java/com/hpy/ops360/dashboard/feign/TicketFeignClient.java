package com.hpy.ops360.dashboard.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ticketing", url = "http://localhost:9202/owner")
public interface TicketFeignClient {

	@GetMapping("/list")
	public ResponseEntity<List<?>> fetchAllTickets(@RequestHeader("Authorization") HttpHeaders headers);

}