package com.hpy.ops360.report_service.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class LoginUtil {
	

	@Value("${keycloak.realm}")
	private String realm;

	private Keycloak keycloak;

	public LoginUtil(Keycloak keycloak) {
		this.keycloak = keycloak;
	}

	public String getLoggedInUserName() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getToken().getSubject();
		UserRepresentation user = keycloak.realm(realm).users().get(userid).toRepresentation();
		String username = user.getUsername();
		return username;
	}
	
//	public String getLoggedInUserId() {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
//		String userid = authentication.getName();
//		return userid;
//	}
	
	//fixed and tested to get userid
	public String getLoggedInUserId() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userId = authentication.getToken().getSubject();
//		log.debug("userId: {}", userId);
		return userId;
	}
	

}
