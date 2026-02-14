package com.hpy.languageconversionservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	public static final String[] PUBLIC_URLS = { "/oauth/login", "/oauth/userLogin", "/forgot-password-service/reset",
			"/user-service/isUserExists/{username}", "/user-service/profilePhoto", "/swagger-ui/**", "/v3/api-docs",
			"/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources",
			"/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/webjars/**",
			"/swagger-ui.html", "/actuator/**"};

	public final JwtAuthConverter jwtAuthConverter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authz -> authz.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
				.csrf((csrf) -> csrf.disable());

		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

		http.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}

}
