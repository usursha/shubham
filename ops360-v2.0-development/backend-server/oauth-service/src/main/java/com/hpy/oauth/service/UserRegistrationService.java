package com.hpy.oauth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.hpy.oauth.dto.AggregateUserRepresentationDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserRegistrationService {

	@Value("${keycloak.auth-server-url}")
	private String keycloakServerUrl;

	@Value("${keycloak.adminClientId}")
	private String clientId;

	@Value("${keycloak.adminClientSecret}")
	private String clientSecret;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.cli.client_id}")
	private String clientCliId;

	@Value("${keycloak.cli.username}")
	private String username;

	@Value("${keycloak.cli.password}")
	private String password;

	@Value("${keycloak.cli.grant_type}")
	private String grantType;

	@Value("${keycloak.max-session.allowed}")
	private int sessionAllowed;

	@Autowired
	private WebClient webClient;

//	public AggregateUserRepresentationDto getUserWithRolesAndGroups(String id) {
//		UserRepresentation user = methodUtil.getUsersResource().get(id).toRepresentation();
//		List<GroupRepresentation> group = keycloak.realm(realm).users().get(id).groups();
//		String groupId = group.stream().map(GroupRepresentation::getId).toList().get(0);
//		List<RoleRepresentation> realmRole = keycloak.realm(realm).groups().group(groupId).roles().realmLevel()
//				.listAll();
//		return new AggregateUserRepresentationDto(user, realmRole, group);
//			
//	}

	public AggregateUserRepresentationDto getUserWithRolesAndGroups(String id) {
		if (id == null || id.trim().isEmpty()) {
			log.error("User ID is null or empty");
			throw new IllegalArgumentException("User ID cannot be null or empty");
		}

		String adminToken = Objects.requireNonNull(getAdminToken().block(),
				"Failed to obtain admin token for Keycloak operations");

		try {
			// Fetch user details
			String userUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + id;
			log.info("Fetching user details for ID: {} at URL: {}", id, userUrl);

			Mono<UserRepresentation> userMono = webClient.get().uri(userUrl)
					.header("Authorization", "Bearer " + adminToken).retrieve()
					.onStatus(status -> status.isError(),
							response -> response.bodyToMono(String.class)
									.map(body -> new RuntimeException(
											"Keycloak error fetching user: " + response.statusCode() + " - " + body)))
					.bodyToMono(UserRepresentation.class);

			// Fetch user groups
			String groupsUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + id + "/groups";
			log.info("Fetching groups for user ID: {} at URL: {}", id, groupsUrl);

			Mono<List<GroupRepresentation>> groupsMono = webClient.get().uri(groupsUrl)
					.header("Authorization", "Bearer " + adminToken).retrieve()
					.onStatus(status -> status.isError(),
							response -> response.bodyToMono(String.class)
									.map(body -> new RuntimeException(
											"Keycloak error fetching groups: " + response.statusCode() + " - " + body)))
					.bodyToMono(JsonNode.class).map(node -> {
						List<GroupRepresentation> groups = new ArrayList<>();
						if (node.isArray()) {
							for (JsonNode groupNode : node) {
								GroupRepresentation group = new GroupRepresentation();
								group.setId(groupNode.get("id").asText(null));
								group.setName(groupNode.get("name").asText(null));
								groups.add(group);
							}
						}
						return groups;
					});

			// Combine user and groups, then fetch roles for the first group
			return userMono.zipWith(groupsMono).flatMap(tuple -> {
				UserRepresentation user = tuple.getT1();
				List<GroupRepresentation> groups = tuple.getT2();

				if (groups.isEmpty()) {
					log.warn("No groups found for user ID: {}", id);
					return Mono.just(new AggregateUserRepresentationDto(user, new ArrayList<>(), groups));
				}

				String groupId = groups.get(0).getId();
				String rolesUrl = keycloakServerUrl + "/admin/realms/" + realm + "/groups/" + groupId
						+ "/role-mappings/realm";
				log.info("Fetching realm roles for group ID: {} at URL: {}", groupId, rolesUrl);

				return webClient.get().uri(rolesUrl).header("Authorization", "Bearer " + adminToken).retrieve()
						.onStatus(status -> status.isError(), response -> response.bodyToMono(String.class)
								.map(body -> new RuntimeException(
										"Keycloak error fetching roles: " + response.statusCode() + " - " + body)))
						.bodyToMono(JsonNode.class).map(node -> {
							List<RoleRepresentation> roles = new ArrayList<>();
							if (node.isArray()) {
								for (JsonNode roleNode : node) {
									RoleRepresentation role = new RoleRepresentation();
									role.setId(roleNode.get("id").asText(null));
									role.setName(roleNode.get("name").asText(null));
									roles.add(role);
								}
							}
							return new AggregateUserRepresentationDto(user, roles, groups);
						});
			}).block();
		} catch (Exception e) {
			log.error("Failed to fetch user details, groups, or roles for user ID {}: {}", id, e.getMessage());
			throw new RuntimeException("Failed to fetch user with roles and groups: " + e.getMessage(), e);
		}
	}

	private Mono<String> getAdminToken() {
		String adminTokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";
		log.info("username admin token from URL: {}", username);
		log.info("password admin token from URL: {}", password);

		return webClient.post().uri(adminTokenUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("client_id", clientCliId).with("username", username)
						.with("password", password).with("grant_type", grantType))
				.retrieve().bodyToMono(JsonNode.class).map(response -> {
					log.info("Admin token obtained successfully");
					return response.get("access_token").asText();
				}).doOnError(error -> log.error("Error obtaining admin token: {}", error.getMessage()));
	}

}
