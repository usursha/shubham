package com.hpy.ops360.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hpy.rest.exception.GlobalExceptionHandler;


@SpringBootApplication
@EnableFeignClients
//@EnableScheduling
//@Import(GlobalExceptionHandler.class)
@ComponentScan(basePackages = {"com.hpy.ops360.ticketing", "com.hpy.rest.exception", "com.hpy.rest.util"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TicketingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingApplication.class, args);
	}

}
