package com.hpy.mappingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.hpy.mappingservice", "com.hpy.rest.exception", "com.hpy.rest.util"})
public class MappingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MappingServiceApplication.class, args);
	}

}
