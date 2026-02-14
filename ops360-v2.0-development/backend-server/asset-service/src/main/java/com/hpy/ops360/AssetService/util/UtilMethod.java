package com.hpy.ops360.AssetService.util;

import java.util.List;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.keycloakbase.util.LoginUtil;


@Component
public class UtilMethod extends LoginUtil{

	@Value("${keycloak.realm}")
	private String realm;
	@Autowired
	private Keycloak keycloak;
	
	
	public UtilMethod(Keycloak keycloak) {
		super(keycloak);
		
	}
	
	public String getUserId() {
		return super.getLoggedInUserId();
	}
	
	public String getUserName() {
		return super.getLoggedInUserName();
	}
//	
	public UsersResource getUsersResource() {
		RealmResource realmResource = keycloak.realm(realm);
		return realmResource.users();
	}
	
	public String getUserIdByUsername(String username) {
		List<UserRepresentation> listOfUsers = getUsersResource().search(username);
		return listOfUsers.stream().map(user -> user.getId()).collect(Collectors.toList()).get(0);
	}
	

}
