package com.hpy.monitoringservice.dto;

import java.util.Set;

import org.keycloak.representations.idm.UserSessionRepresentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCEUsersDetails {

	
	private int ceCount;
	private Set<UserSessionRepresentation> activeCEUsersSet;
}
