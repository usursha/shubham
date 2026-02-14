package com.hpy.oauth.dto;

import java.util.List;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AggregateUserRepresentationDto {

	
	private UserRepresentation userDetails;
	
	
	private List<RoleRepresentation> realmRoles;
	
	
	private List<GroupRepresentation> groups;


}
