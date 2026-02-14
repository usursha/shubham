package com.hpy.ops360.ticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
	@Bean
	public TokenRelayRequestInterceptor tokenRelayRequestInterceptor() {
		return new TokenRelayRequestInterceptor();
	}
}