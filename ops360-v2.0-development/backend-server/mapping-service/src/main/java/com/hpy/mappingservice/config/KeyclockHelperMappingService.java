package com.hpy.mappingservice.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Component;

import com.hpy.keycloakbase.util.LoginUtil;

@Component
public class KeyclockHelperMappingService extends LoginUtil {

	public KeyclockHelperMappingService(Keycloak keycloak) {
		super(keycloak);
		
	}

}
