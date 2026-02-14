package com.hpy.ops360.atmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hpy.ops360.atmservice", "com.hpy.rest.exception", "com.hpy.rest.util"})//, "com.hpy.keycloakbase"})
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AtmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmServiceApplication.class, args);
	}

}
