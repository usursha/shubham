package com.hpy.ops360.ticketing.util;

import java.util.List;
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

@Component
public class MethodUtil {

	@Value("${keycloak.realm}")
	private String realm;

	@Autowired
	private Keycloak keycloak;

	@Autowired
	private UsersResource usersResource;

//	public MethodUtil(Keycloak keycloak) {
//		this.keycloak=keycloak;
//	}

	public String getLoggedInUserName() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getToken().getSubject();
		UserRepresentation user = keycloak.realm(realm).users().get(userid).toRepresentation();
		String username = user.getUsername();
		return username;
	}

	// testing
	public String getUserIdByUsername(String username) {
		List<UserRepresentation> listOfUsers = usersResource.search(username);
		return listOfUsers.stream().map(user -> user.getId()).collect(Collectors.toList()).get(0);

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

	public List<GroupRepresentation> getGroupsByUsername(String username) {
		RealmResource realmResource = keycloak.realm(realm);
		String userId = realmResource.users().search(username).get(0).getId();
		return realmResource.users().get(userId).groups();
	}

}