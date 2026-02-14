package com.hpy.ops360.location.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.hpy.keycloakbase.config.JwtAuthConverter;
import com.hpy.keycloakbase.config.SecurityConfig;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigImpl extends SecurityConfig{

	public SecurityConfigImpl(JwtAuthConverter jwtAuthConverter) {
		super(jwtAuthConverter);
	}
	

}
