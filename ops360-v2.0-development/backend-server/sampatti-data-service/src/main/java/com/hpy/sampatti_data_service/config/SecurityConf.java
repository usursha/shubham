package com.hpy.sampatti_data_service.config;

import org.apache.camel.spi.annotations.Component;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.hpy.keycloakbase.config.JwtAuthConverter;
import com.hpy.keycloakbase.config.SecurityConfig;

import lombok.RequiredArgsConstructor;

@Configuration
public class SecurityConf extends SecurityConfig {

	public SecurityConf(JwtAuthConverter jwtAuthConverter) {
		super(jwtAuthConverter);
		// TODO Auto-generated constructor stub
	}



}
