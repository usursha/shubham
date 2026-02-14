package com.hpy.uam.dto;

import java.util.List;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import com.hpy.generic.impl.GenericDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class GroupPresentationRequestDto extends GenericDto {

	@NotEmpty(message="subGroup cannot be blank!!")
	private List<GroupRepresentation> subGroup;
    
	@NotEmpty(message="roles cannot be blank!!")
	private List<RoleRepresentation> roles;
}
