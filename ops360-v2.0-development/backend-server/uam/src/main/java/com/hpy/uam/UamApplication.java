package com.hpy.uam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class UamApplication {

	@Value("${keycloak.auth-server-url}")
	private String keycloakServerUrl;

	@Value("${keycloak.adminClientId}")
	private String clientId;

	@Value("${keycloak.adminClientSecret}")
	private String clientSecret;

	@Value("${keycloak.realm}")
	private String realm;

	@PostConstruct
	public void logKeycloakConfig() {
		String tokenEndpointUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
		log.info("Keycloak Token Endpoint URL: {}", tokenEndpointUrl);
		log.info(
				"Keycloak Config - Realm: {}, Admin Client ID: {}, CLI Client ID: {}, Grant Type: {}, Max Sessions Allowed: {}",
				realm, clientId);
		log.info("clientSecret - " + clientSecret);
	}

	public static void main(String[] args) {
		SpringApplication.run(UamApplication.class, args);
	}

}
