package com.hpy.ops360.atmservice.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Component;

import com.hpy.keycloakbase.util.LoginUtil;

@Component
public class KeycloakHelper extends LoginUtil{

	public KeycloakHelper(Keycloak keycloak) {
		super(keycloak);
		// TODO Auto-generated constructor stub
	}

}
