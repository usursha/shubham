package com.hpy.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	protected static final String[] PUBLIC_URLS = { "/oauth/cm_login", "/oauth/ce_login", "/v1/oauth/ce-login",
			"/v1/oauth/cm-login", "/v2/app/oauth/ce-login", "/v2/portal/oauth/cm-login", "/oauth/refreshAccessToken",
			"/oauth/getUser/**", "/forgot-password-service/reset", "/swagger-ui/**", "/v3/api-docs", "/actuator/**",
			"/connections/**", "/feign/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html",
			"/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security",
			"/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/actuator/**", "/connections/**", "/v1/oauth/logout",
			"/v1/oauth/refresh-access-token", "/v2/oauth/refresh-access-token", "/oauth/validateToken",
			"/v1/oauth/validateToken", "/v2/oauth/validateToken", "/v2/portal/oauth/loginUpdatePassword" };

	private final JwtAuthConverter jwtAuthConverter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authz -> authz.requestMatchers(PUBLIC_URLS)
				.permitAll().anyRequest().authenticated())
				.csrf((csrf) -> csrf.disable());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
		http.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}

}