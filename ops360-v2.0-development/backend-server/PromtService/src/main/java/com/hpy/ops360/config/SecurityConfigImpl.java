package com.hpy.ops360.config;

import org.springframework.stereotype.Component;

import com.hpy.keycloakbase.config.JwtAuthConverter;
import com.hpy.keycloakbase.config.SecurityConfig;

@Component
public class SecurityConfigImpl extends SecurityConfig{

	public SecurityConfigImpl(JwtAuthConverter jwtAuthConverter) {
		super(jwtAuthConverter);
		
	}

}
