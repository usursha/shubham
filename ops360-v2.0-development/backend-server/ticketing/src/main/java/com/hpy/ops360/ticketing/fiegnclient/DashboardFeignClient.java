package com.hpy.ops360.ticketing.fiegnclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.hpy.ops360.ticketing.config.FeignConfiguration;

@FeignClient(name = "dashboard", url = "http://localhost:9200/cm-dashboard", configuration = FeignConfiguration.class)
public interface DashboardFeignClient {

	@GetMapping("/getCmSynopsisDetails")
	public ResponseEntity<List<?>> fetchAllCm();

}
