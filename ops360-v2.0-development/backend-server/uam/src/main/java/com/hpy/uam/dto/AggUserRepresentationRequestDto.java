package com.hpy.uam.dto;

import java.util.List;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.hpy.generic.impl.GenericDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AggUserRepresentationRequestDto extends GenericDto{

	@NotEmpty(message="UserDetails cannot be blank")
	private UserRepresentation userDetails;
	
	@NotEmpty(message="realmRoles cannot be blank")
	private List<RoleRepresentation> realmRoles;
	
	@NotEmpty(message="groups cannot be blank")
	private List<GroupRepresentation> groups;


}
