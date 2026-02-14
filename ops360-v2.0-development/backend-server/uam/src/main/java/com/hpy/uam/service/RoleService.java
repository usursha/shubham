package com.hpy.uam.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.uam.dto.RoleRepresentationResponseDto;
import com.hpy.uam.dto.Roles;
import com.hpy.uam.util.MethodUtil;

@Service
public class RoleService {

	@Value("${keycloak.realm}")
	private String realm;

	private Keycloak keycloak;
	private MethodUtil methodUtil;

	public RoleService(Keycloak keycloak, MethodUtil methodUtil) {
		this.keycloak = keycloak;
		this.methodUtil = methodUtil;
	}

	public RoleRepresentationResponseDto list() {
		RoleRepresentationResponseDto response=new RoleRepresentationResponseDto();
		response.setList(methodUtil.getRolesResource().list());
		return response;
	}

	public Roles createRole(Roles roleName) {
		RoleRepresentation roleRepresentation = new RoleRepresentation();
		roleRepresentation.setName(roleName.getRoleName());
		roleRepresentation.setDescription(roleName.getDescription());
		keycloak.realm(realm).roles().create(roleRepresentation);
		return roleName;
	}

	public RoleRepresentation getRoleByName(String roleName) {
		return keycloak.realm(realm).roles().get(roleName).toRepresentation();
	}

	public String roleActivation(String roleName) {
		var realmResource = keycloak.realm(realm);
		var rolesResource = realmResource.roles();
		rolesResource.get(roleName).toRepresentation().setComposite(true);
		return "Role is Activated";
	}

	public String roleDeactivation(String roleName) {
		var realmResource = keycloak.realm(realm);
		var rolesResource = realmResource.roles();
		rolesResource.get(roleName).toRepresentation().setComposite(false);
		return "Role is deactivated";
	}

	//working code tested
	public RoleRepresentationResponseDto getClientRoles(String clientId) {
		RoleRepresentationResponseDto response=new RoleRepresentationResponseDto();
		ClientRepresentation client = methodUtil.getClientsResource().findByClientId(clientId).get(0);
		response.setList(methodUtil.getClientsResource().get(client.getId()).roles().list());
		return response;
	}
	
}
