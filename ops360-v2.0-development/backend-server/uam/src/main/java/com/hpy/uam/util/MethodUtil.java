package com.hpy.uam.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.hpy.keycloakbase.util.LoginUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MethodUtil extends LoginUtil{

	public MethodUtil(Keycloak keycloak) {
		super(keycloak);
	}

	@Value("${keycloak.realm}")
	private String realm;
	@Autowired
	private Keycloak keycloak;
	

	// testing
	public String getUserIdByUsername(String username) {
		List<UserRepresentation> listOfUsers = getUsersResource().search(username);
		return listOfUsers.stream().map(user -> user.getId()).collect(Collectors.toList()).get(0);
	}
	
	public String getGroupIdByGroupName(String groupname) {
		List<GroupRepresentation> groups= getGroupsResource().groups();
		Optional<GroupRepresentation> group=groups.stream().filter(grp->grp.getName().equals(groupname)).findFirst();
		String grpId=group.map(GroupRepresentation:: getId).orElse(null);
		return grpId;
	}

	public UsersResource getUsersResource() {
		RealmResource realmResource = keycloak.realm(realm);
		return realmResource.users();
	}
	
	public RolesResource getRolesResource() {
		RealmResource realmResource = keycloak.realm(realm);
		return realmResource.roles();
	}
	
	public GroupsResource getGroupsResource() {
		RealmResource realmResource = keycloak.realm(realm);
		return realmResource.groups();
	}
	
	public ClientsResource getClientsResource() {
		RealmResource realmResource = keycloak.realm(realm);
		return realmResource.clients();
	}

	public String getLoggedInUserId() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getToken().getSubject();
		return userid;
	}	
	
	public String getLoggedInUserName() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getToken().getSubject();
		UserRepresentation user = getUsersResource().get(userid).toRepresentation();
		String username = user.getUsername();
		return username;
	}
	

	
	
}
