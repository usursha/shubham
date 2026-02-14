package com.hpy.monitoringservice.dto;

import java.util.Set;

import org.keycloak.representations.idm.UserSessionRepresentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCMUsersDetails {
	
	
	private int cmCount;
	
	private Set<UserSessionRepresentation> activeCMUsersSet;

}
