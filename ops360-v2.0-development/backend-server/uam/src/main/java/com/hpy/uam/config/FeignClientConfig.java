package com.hpy.uam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
class UserProfileFeignConfig {
    
	@Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}