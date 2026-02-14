package com.hpy.ops360.ticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	protected static final String[] PUBLIC_URLS = { "/Controllerfeign/ce_login", "/ticket/close" ,"/swagger-ui/**", "/v3/api-docs",
			"/actuator/**", "/connections/**", "/ticket/close", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
			"/swagger-ui.html", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/download/**"};

	private JwtAuthConverter jwtAuthConverter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authz -> authz.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
				.csrf((csrf) -> csrf.disable());

		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

		http.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//		http.addFilterAfter(new CustomSecurityFilter(), BasicAuthenticationFilter.class);

		return http.build();

	}

}
