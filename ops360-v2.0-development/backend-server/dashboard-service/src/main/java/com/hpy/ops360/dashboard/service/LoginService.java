package com.hpy.ops360.dashboard.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	@Value("${keycloak.realm}")
	private String realm;

//	@Autowired
	private Keycloak keycloak;

//
	public LoginService(Keycloak keycloak) {
		this.keycloak = keycloak;
	}

	public String getLoggedInUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getToken().getSubject();
		UserRepresentation user = keycloak.realm(realm).users().get(userid).toRepresentation();
		String username = user.getUsername();
		return username;
	}

}
