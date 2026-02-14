package com.hpy.ops360.report_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

//	@Bean
//	public ExecutorService executorService() {
//		int poolSize = 10; // Set your desired pool size here
//		return Executors.newFixedThreadPool(poolSize);
//	}
}