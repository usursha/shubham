package com.hpy.ops360.synergyservice.config;

import org.springframework.context.annotation.Configuration;

import com.hpy.keycloakbase.config.JwtAuthConverter;
import com.hpy.keycloakbase.config.SecurityConfig;

@Configuration
public class SecurityConfigImpl extends SecurityConfig {

	public SecurityConfigImpl(JwtAuthConverter jwtAuthConverter) {
		super(jwtAuthConverter);
	}



}
