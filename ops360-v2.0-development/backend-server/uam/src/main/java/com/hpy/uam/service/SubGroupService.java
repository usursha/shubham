package com.hpy.uam.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.uam.dto.GenericMessageResponseDto;
import com.hpy.uam.dto.GroupRepresentationListResponseDto;
import com.hpy.uam.dto.StatusCodeResponseDto;
import com.hpy.uam.util.MethodUtil;

import jakarta.ws.rs.core.Response;

@Service
public class SubGroupService {

	@Value("${keycloak.realm}")
	private String realm;

	private Keycloak keycloak;
	private MethodUtil methodUtil;

	public SubGroupService(Keycloak keycloak,MethodUtil methodUtil) {
		this.keycloak=keycloak;
		this.methodUtil=methodUtil;
	}
	

	public StatusCodeResponseDto createSubgroup(String parentId, String subgroupName) {
		StatusCodeResponseDto responseDto=new StatusCodeResponseDto();
		GroupRepresentation subgroup = new GroupRepresentation();
		subgroup.setName(subgroupName);
		Response response = methodUtil.getGroupsResource().group(parentId).subGroup(subgroup);
		if(response.getStatus()==200) {
			responseDto.setCode(response.getStatus());
			responseDto.setMessage("Group Created Successfully..");
		}return responseDto;

	}

	public GroupRepresentationListResponseDto getAllSubgroup(String parentId) {
		GroupRepresentationListResponseDto response=new GroupRepresentationListResponseDto();
		response.setList(methodUtil.getGroupsResource().group(parentId).getSubGroups(0, 100, true));
		return response;
	}

	public GenericMessageResponseDto deleteSubGroup(String id) {
		GenericMessageResponseDto response=new GenericMessageResponseDto();
		methodUtil.getGroupsResource().group(id).remove();
		response.setMessage("Group Deleted Successfully..");
		return response;
	}


	public GenericMessageResponseDto assignRole(String groupId, List<String> roles) throws Exception {
		GenericMessageResponseDto responseDto=new GenericMessageResponseDto();
		GroupResource groupResource = methodUtil.getGroupsResource().group(groupId);
		validateRoles(roles);
		List<RoleRepresentation> existingRoles = groupResource.roles().realmLevel().listAll();

		groupResource.roles().realmLevel().remove(existingRoles);
		List<RoleRepresentation> roleList = new ArrayList<>();
		for (String role : roles) {
			RoleRepresentation representation = keycloak.realm(realm).roles().get(role).toRepresentation();
			roleList.add(representation);
		}
		groupResource.roles().realmLevel().add(roleList);
		responseDto.setMessage("role assigned..");
		return responseDto;

	}

	private void validateRoles(List<String> roles) throws Exception {
		List<String> invalidRolesList = new ArrayList<>();
		for (String role : roles) {
			try {
				keycloak.realm(realm).roles().get(role).toRepresentation();
			} catch (Exception e) {
				invalidRolesList.add(role);
			}
		}
		if (!invalidRolesList.isEmpty()) {
			throw new Exception("Invalid Roles: " + StringUtils.join(invalidRolesList));
		}
	}
	
}
