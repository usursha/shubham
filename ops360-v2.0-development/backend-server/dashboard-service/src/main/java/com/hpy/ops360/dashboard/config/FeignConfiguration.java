package com.hpy.ops360.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignConfiguration {
    @Bean
    public TokenRelayRequestInterceptor tokenRelayRequestInterceptor() {
        return new TokenRelayRequestInterceptor();
    }
}