package com.hpy.ops360.synergyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class,
	    HibernateJpaAutoConfiguration.class
	})
public class SynergyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SynergyServiceApplication.class, args);
	}

}
