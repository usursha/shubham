package com.hpy.mappingservice.service;

import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService {

	@Value("${keycloak.realm}")
	private String realm;

 	private Keycloak keycloak;

	public LoginService(Keycloak keycloak) {
		this.keycloak = keycloak;
	}
    public String getLoggedInUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();

    
        String principalName = authentication.getName();
        
        String userIdFromAuth = principalName; 

        UserRepresentation user = null;
        try {
           
            user = keycloak.realm(realm).users().get(userIdFromAuth).toRepresentation();
            log.info("2. Successfully fetched UserRepresentation using ID: {}", userIdFromAuth);
        } catch (Exception e) {
            log.info("Error fetching user from Keycloak using ID '{}': {}", userIdFromAuth, e.getMessage());
            log.info("Attempting to search user by username (fallback): {}", userIdFromAuth);
            List<UserRepresentation> users = keycloak.realm(realm).users().search(userIdFromAuth);
            if (!users.isEmpty()) {
                user = users.get(0); 
                log.info("3. Successfully fetched UserRepresentation by searching for username: {}", user.getUsername());
            } else {
                log.info("4. User not found by ID or username search for: {}", userIdFromAuth);
                return null; 
            }
        }

        if (user != null) {
            String username = user.getUsername();
            return username;
        } else {
            return null; 
        }
    }

}
