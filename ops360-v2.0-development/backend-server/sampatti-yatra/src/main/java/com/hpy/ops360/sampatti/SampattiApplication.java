package com.hpy.ops360.sampatti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hpy.ops360.sampatti", "com.hpy.keycloakbase.*", "com.hpy.rest.*", "com.hpy.swagger_app.*"})
@EnableFeignClients
@EnableCaching
@EnableScheduling
public class SampattiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampattiApplication.class, args);
	}

}
