package com.hpy.uam.service;

import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.uam.dto.GenericMessageResponseDto;
import com.hpy.uam.dto.GroupPresentationRequestDto;
import com.hpy.uam.dto.GroupRegistrationRequestDto;
import com.hpy.uam.dto.GroupRepresentationListResponseDto;
import com.hpy.uam.dto.GroupRepresentationResponseDto;
import com.hpy.uam.dto.StatusCodeResponseDto;
import com.hpy.uam.util.MethodUtil;

import jakarta.ws.rs.core.Response;

@Service
public class GroupService {

	@Value("${keycloak.realm}")
	private String realm;

	private Keycloak keycloak;
	private MethodUtil methodUtil;

	public GroupService(Keycloak keycloak, MethodUtil methodUtil) {
		this.keycloak = keycloak;
		this.methodUtil = methodUtil;
	}

	public StatusCodeResponseDto createGroup(GroupRegistrationRequestDto groupRegistration) {
		StatusCodeResponseDto status =new StatusCodeResponseDto();
		GroupRepresentation group = new GroupRepresentation();
		group.setName(groupRegistration.getName());
		Response response = methodUtil.getGroupsResource().add(group);
		if(response.getStatus()==201) {
			status.setCode(response.getStatus());
			status.setMessage("Group Created Successfully..");
		}return status;
	}

	public GroupRepresentationListResponseDto getAllGroup() {
		GroupRepresentationListResponseDto response=new GroupRepresentationListResponseDto(); 
		response.setList(methodUtil.getGroupsResource().groups(0, 100));
		return response;
	}

	public GenericMessageResponseDto updateGroup(String groupId, GroupRepresentation group) {
		GenericMessageResponseDto responseDto=new GenericMessageResponseDto();
		GroupRepresentation existGroup=methodUtil.getGroupsResource().group(groupId).toRepresentation();
		if(existGroup!=null) {
		methodUtil.getGroupsResource().group(groupId).update(group);
		responseDto.setMessage("Group Updated Successfully..");
		}else {
			responseDto.setMessage("Could not find any group with this name!!");
		}
		return responseDto;
	}

	public GroupRepresentationResponseDto getGroupById(String id) {
		GroupRepresentationResponseDto response=new GroupRepresentationResponseDto();
		response.setGroupRepresentation(methodUtil.getGroupsResource().group(id).toRepresentation());
		return response;
	}

	public GenericMessageResponseDto deleteGroup(String id) {
		GenericMessageResponseDto responseDto=new GenericMessageResponseDto();
		methodUtil.getGroupsResource().group(id).remove();
		responseDto.setMessage("Group deleted successfully..");
		return responseDto;
	}
	
	public GenericMessageResponseDto addingUserToGroup(String userId, String groupId) {
		GenericMessageResponseDto response=new GenericMessageResponseDto();
		UserRepresentation user=keycloak.realm(realm).users().get(userId).toRepresentation();
		GroupRepresentation group=keycloak.realm(realm).groups().group(groupId).toRepresentation();
		if (user != null && group != null) {
			keycloak.realm(realm).users().get(userId).joinGroup(groupId);
			response.setMessage("user added successfully");
		} return response;
	}

	public GroupPresentationRequestDto getGroupDetails(String parentId) {
		List<GroupRepresentation> subGroup = keycloak.realm(realm).groups().group(parentId).getSubGroups(0, 100, true);
		List<RoleRepresentation> roles = keycloak.realm(realm).groups().group(parentId).roles().realmLevel().listAll();
		GroupPresentationRequestDto groupPresentation = new GroupPresentationRequestDto(subGroup, roles);
		return groupPresentation;
	}
	
//	public int getTotalCEUserCount() {
//		int totalCEUser = 0;
//		List<GroupRepresentation> groupsList = methodUtil.getGroupsResource().groups(0, 1000);
//		for (GroupRepresentation list : groupsList) {
//			if (list.getName().equals("CE_USER")) {
//				String groupId = list.getId();
//				totalCEUser = keycloak.realm(realm).groups().group(groupId).members().size();
//			}
//		}
//		return totalCEUser;
//	}
//	
//	public int getTotalCMUserCount() {
//		int totalCMUser = 0;
//		List<GroupRepresentation> groupsList = methodUtil.getGroupsResource().groups(0, 1000);
//		for (GroupRepresentation list : groupsList) {
//			if (list.getName().equals("CM_USER")) {
//				String groupId = list.getId();
//				totalCMUser = keycloak.realm(realm).groups().group(groupId).members().size();
//			}
//		}
//		return totalCMUser;
//	}
		
}
