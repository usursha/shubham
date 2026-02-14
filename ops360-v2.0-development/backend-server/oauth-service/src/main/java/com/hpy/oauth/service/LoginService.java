package com.hpy.oauth.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.hpy.oauth.dto.AccessTokenDto;
import com.hpy.oauth.dto.AccessTokenRequestedDto;
import com.hpy.oauth.dto.CELoginRequestDto;
import com.hpy.oauth.dto.ChangePasswordDto;
import com.hpy.oauth.dto.ChangePasswordRequest;
import com.hpy.oauth.dto.LoginRequestDto;
import com.hpy.oauth.dto.LoginResponse2Dto;
import com.hpy.oauth.dto.LoginResponseDto;
import com.hpy.oauth.dto.LogoutResponseDto;
import com.hpy.oauth.dto.PortalChangePasswordDto;
import com.hpy.oauth.dto.ResponseMessageDto;
import com.hpy.oauth.dto.UserDetailsDto;
import com.hpy.oauth.entity.TokenMetadata;
import com.hpy.oauth.enums.TokenStatus;
import com.hpy.oauth.enums.TokenType;
import com.hpy.oauth.repository.TokenMetadataRepository;
import com.hpy.oauth.util.MethodUtil;
import com.hpy.rest.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LoginService {

	private static final String UPDATE_PASSWORD = "UPDATE_PASSWORD";

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

	private RestTemplate restTemplate;

	private UserRegistrationService userRegistrationService;

	private MethodUtil methodUtil;

	private WebClient webClient;
//	@Autowired
	private Keycloak keycloak;

//	@Autowired
	private UsersResource usersResource;

	@Autowired
	private TokenExpiryService tokenExpiryService;

	@Autowired
	private TokenMetadataRepository tokenMetadataRepository;

	public LoginService(UserRegistrationService userRegistrationService, MethodUtil methodUtil, WebClient webClient,
			RestTemplate restTemplate) {
		this.userRegistrationService = userRegistrationService;
		this.methodUtil = methodUtil;
		this.webClient = webClient;
		this.restTemplate = restTemplate;
	}

	private void saveTokenMetadata(String username, String accessToken, String refreshToken, LocalDateTime issuedAt,
			LocalDateTime expiresAt) {
		TokenMetadata accessTokenMetadata = new TokenMetadata();
		accessTokenMetadata.setTokenId(accessToken);
		accessTokenMetadata.setUserId(username);
		accessTokenMetadata.setTokenType(TokenType.ACCESS);
		accessTokenMetadata.setStatus(TokenStatus.ACTIVE);
		accessTokenMetadata.setIssuedAt(issuedAt);
		accessTokenMetadata.setExpiresAt(expiresAt);
		accessTokenMetadata.setRefreshToken(refreshToken);
		tokenMetadataRepository.save(accessTokenMetadata);

	}

	public Mono<Void> checkActiveSessions(String userId) {
		List<TokenMetadata> activeTokens = tokenMetadataRepository.findByUserIdAndStatus(userId, TokenStatus.ACTIVE);
		if (!activeTokens.isEmpty()) {
			return Mono.error(
					new RuntimeException("Your session is already active. Please logout from the other device."));
		}
		return Mono.empty();
	}

	public Mono<LoginResponse2Dto> loginWithoutToken(CELoginRequestDto loginRequestDto) {
		// First, check existing sessions
		return checkExistingUserSessions(loginRequestDto.getUsername()).flatMap(existingSessions -> {
			if (existingSessions >= sessionAllowed) {
				// Option 1: Return error if session exists

				return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
						"User already has an active session. Please logout from other devices first."));
				// Option 2: Terminate existing sessions before creating new one
				// return terminateExistingSessions(loginRequestDto.getUsername())
				// .then(performLogin(loginRequestDto));
			}
			return performLogin(loginRequestDto);
		});
	}

	private Mono<LoginResponse2Dto> performLogin(CELoginRequestDto loginRequestDto) {
		List<GroupRepresentation> list = methodUtil.getGroupsByUsername(loginRequestDto.getUsername());
		GroupRepresentation groupRepresentation = list.get(0);
		String group = groupRepresentation.getName();
		if ("CE_USER".equals(group)) {
			LoginResponse2Dto loginResponse = new LoginResponse2Dto();

			return webClient.post().uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(BodyInserters.fromFormData("client_id", clientId).with("client_secret", clientSecret)
							.with("username", loginRequestDto.getUsername())
							.with("password", loginRequestDto.getPassword()).with("grant_type", "password"))
					.retrieve().bodyToMono(JsonNode.class).flatMap(response -> {
						try {
							// Extract tokens and expiration details
							String accessToken = response.get("access_token").asText();
							String refreshToken = response.get("refresh_token").asText();

							long expiresIn = response.get("expires_in").asLong();

							// Calculate issued and expiration times
							LocalDateTime issuedAt = LocalDateTime.now();
							LocalDateTime expiresAt = issuedAt.plusSeconds(expiresIn);

							tokenExpiryService.scheduleTokenCleanup(accessToken, expiresAt);

							// Save token metadata for user
							saveTokenMetadata(loginRequestDto.getUsername(), accessToken, refreshToken, issuedAt,
									expiresAt);

							// Populate login response object
							loginResponse.setAccessToken(accessToken);
							loginResponse.setRefreshToken(refreshToken);

							// Example condition to check scan availability
							boolean isScanAvailable = true;
							if (isScanAvailable) {
								return Mono.just(loginResponse);
							} else {
								return Mono.error(new RuntimeException("Scan not available"));
							}
						} catch (Exception e) {
							return Mono.error(new RuntimeException("Error processing login response", e));
						}
					});
		} else {
			return Mono.error(new RuntimeException("Unauthorized Access"));
		}
	}

	private Mono<Integer> checkExistingUserSessions(String username) {
		// First get admin token
		return getAdminToken().flatMap(adminToken -> webClient.get()
				.uri(keycloakServerUrl + "/admin/realms/" + realm + "/users?username=" + username)
				.headers(headers -> headers.setBearerAuth(adminToken)).retrieve().bodyToMono(JsonNode.class)
				.flatMap(users -> {
					if (users.size() > 0) {
						String userId = users.get(0).get("id").asText();
						return webClient.get()
								.uri(keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/sessions")
								.headers(headers -> headers.setBearerAuth(adminToken)).retrieve()
								.bodyToMono(JsonNode.class).map(sessions -> sessions.size());
					}
					return Mono.just(0);
				}));
	}

	private Mono<String> getAdminToken() {
		return webClient.post().uri(keycloakServerUrl + "/realms/master/protocol/openid-connect/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("client_id", clientCliId).with("username", username)
						.with("password", password).with("grant_type", grantType))
				.retrieve().bodyToMono(JsonNode.class).map(response -> response.get("access_token").asText());
	}

	public Mono<LoginResponse2Dto> loginWithToken(LoginRequestDto loginRequestDto) {
		// First, check existing sessions
		return checkExistingUserSessions(loginRequestDto.getUsername()).flatMap(existingSessions -> {
			if (existingSessions >= sessionAllowed) {
				// Return a Conflict status code if a session exists
				return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
						"User already has an active session. Please logout from other devices first."));
			}
			return performCmLogin(loginRequestDto);
		});
	}

	private Mono<LoginResponse2Dto> performCmLogin(LoginRequestDto loginRequestDto) {
		List<GroupRepresentation> list = methodUtil.getGroupsByUsername(loginRequestDto.getUsername());
		GroupRepresentation groupRepresentation = list.get(0);
		String group = groupRepresentation.getName();
		if ("CM_USER".equals(group)) {
			LoginResponse2Dto login = new LoginResponse2Dto();
			return webClient.post().uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(BodyInserters.fromFormData("client_id", clientId).with("client_secret", clientSecret)
							.with("username", loginRequestDto.getUsername())
							.with("password", loginRequestDto.getPassword()).with("otp", loginRequestDto.getToken()) // Include
																														// OTP
																														// required
							.with("grant_type", "password"))
					.retrieve().bodyToMono(JsonNode.class).flatMap(response -> {

						boolean scanAvailable = true; // Adjust based on your logic

						String accessToken = response.get("access_token").asText();
						String refreshToken = response.get("refresh_token").asText();

						long expiresIn = response.get("expires_in").asLong();

						// Calculate issued and expiration times
						LocalDateTime issuedAt = LocalDateTime.now();
						LocalDateTime expiresAt = issuedAt.plusSeconds(expiresIn);

						tokenExpiryService.scheduleTokenCleanup(accessToken, expiresAt);

						// Save token metadata for user
						saveTokenMetadata(loginRequestDto.getUsername(), accessToken, refreshToken, issuedAt,
								expiresAt);

						login.setAccessToken(accessToken);
						login.setRefreshToken(refreshToken);

						if (scanAvailable) {
							return Mono.just(login);
						} else {
							return Mono.error(new RuntimeException("scan not available"));
						}
					});
		} else {
			return Mono.error(new ResourceNotFoundException("Unauthorized Access"));
		}
	}

	public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
		LoginResponse2Dto token = loginWithToken(loginRequestDto).block();
		LoginResponseDto response = new LoginResponseDto();
		response.setAccessToken(token.getAccessToken());
		response.setRefreshToken(token.getRefreshToken());
		String userId = methodUtil.getUserIdByUsername(loginRequestDto.getUsername());
		response.setRepresentation(userRegistrationService.getUserWithRolesAndGroups(userId));
		return response;
	}

	public LoginResponseDto loginUpdatePassword(LoginRequestDto loginRequestDto) {
		LoginResponse2Dto token = loginWithToken(loginRequestDto).block();
		LoginResponseDto response = new LoginResponseDto();
		response.setAccessToken(token.getAccessToken());
		response.setRefreshToken(token.getRefreshToken());
		return response;
	}

	public Mono<AccessTokenDto> accessTokenRequestedDto(AccessTokenRequestedDto accessTokenRequestedDto) {
		return Mono.justOrEmpty(tokenMetadataRepository.findByRefreshToken(accessTokenRequestedDto.getRefreshToken()))
				.flatMap(tokenMetadata -> {
					// Extract the old access token for cleanup
					String oldAccessToken = tokenMetadata.getTokenId();

					// Blacklist all tokens for the user
					blacklistTokensForUser(tokenMetadata.getUserId());

					// Request a new token using the refresh token
					return requestNewAccessToken(accessTokenRequestedDto, tokenMetadata.getUserId(), oldAccessToken);
				}).switchIfEmpty(
						// No entry found in the database
						requestNewAccessToken(accessTokenRequestedDto, null, null));
	}

	private Mono<AccessTokenDto> requestNewAccessToken(AccessTokenRequestedDto accessTokenRequestedDto, String userId,
			String oldAccessToken) {
		return webClient.post().uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("client_id", clientId).with("client_secret", clientSecret)
						.with("refresh_token", accessTokenRequestedDto.getRefreshToken())
						.with("grant_type", "refresh_token"))
				.retrieve().bodyToMono(JsonNode.class).flatMap(response -> {
					// Extract new token details
					String newAccessToken = response.get("access_token").asText();
					String newRefreshToken = response.get("refresh_token").asText();
					LocalDateTime issuedAt = LocalDateTime.now();
					LocalDateTime expiresAt = issuedAt.plusSeconds(response.get("expires_in").asLong());

					// Save new token metadata if userId is available
					if (userId != null) {
						saveTokenMetadata(userId, newAccessToken, newRefreshToken, issuedAt, expiresAt);

						// Schedule cleanup for the old access token
						if (oldAccessToken != null) {
							tokenExpiryService.scheduleTokenCleanup(oldAccessToken, expiresAt);
							tokenExpiryService.scheduleTokenCleanup(newAccessToken, expiresAt);
						}
					}

					// Build the response DTO
					AccessTokenDto tokenDto = new AccessTokenDto();
					tokenDto.setAccessToken(newAccessToken);
					tokenDto.setRefreshToken(newRefreshToken);

					return Mono.just(tokenDto);
				});
	}

//	new functionality: password is combination of one uppercase letter, one lowercase letter, one digit, one special character.
//	public ResponseMessageDto changePasswordSplChar(ChangePasswordDto changePasswordDto)
//			throws InputMismatchException, WebClientResponseException {
//		ResponseMessageDto response = new ResponseMessageDto();
//		if (changePasswordDto.getConfirmPassword() == null || changePasswordDto.getConfirmPassword().equals("")
//				|| !changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
//			throw new InputMismatchException("New Password doesn't meet criteria!!!");
//		}
//		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
//		if (!changePasswordDto.getNewPassword().matches(passwordPattern)) {
//			throw new InputMismatchException(
//					"Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.");
//		}
//		LoginRequestDto loginRequest = new LoginRequestDto(getLoggedInUser(), changePasswordDto.getCurrentPassword(),
//				changePasswordDto.getToken());
//		loginWithToken(loginRequest).block();
//		UsersResource usersResource = methodUtil.getUsersResource();
//		UserRepresentation userRepresentation = usersResource.search(loginRequest.getUsername()).get(0);
//		changeUserPassword(userRepresentation, changePasswordDto.getNewPassword(), Boolean.FALSE);
//		response.setMessage("password updated");
//		return response;
//	}

	public ResponseMessageDto changePasswordSplChar(ChangePasswordDto changePasswordDto)
			throws InputMismatchException, WebClientResponseException {
		ResponseMessageDto response = new ResponseMessageDto();
		if (changePasswordDto.getConfirmPassword() == null || changePasswordDto.getConfirmPassword().equals("")
				|| !changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
			throw new InputMismatchException("New Password doesn't meet criteria!!!");
		}
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		if (!changePasswordDto.getNewPassword().matches(passwordPattern)) {
			throw new InputMismatchException(
					"Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.");
		}
		Boolean status = validateCredentials(methodUtil.getLoggedInUserName(), changePasswordDto.getCurrentPassword());
		if (status == true) {
			log.info("Current password matched");
			UsersResource usersResource = methodUtil.getUsersResource();
			UserRepresentation userRepresentation = usersResource.search(methodUtil.getLoggedInUserName()).get(0);
			if (changeUserPassword(userRepresentation, changePasswordDto.getNewPassword(), Boolean.FALSE))
				response.setMessage("password updated");
			else {
				log.info("Error updating password");
				throw new RuntimeException("Error occure while updating the password");
			}
		} else {
			throw new RuntimeException("Current Password did not match.");
		}
		return response;
	}

// new method	
	public ResponseMessageDto changePasswordSplCharWithOutOTP(ChangePasswordRequest changePasswordRequest)
			throws InputMismatchException, WebClientResponseException {
		ResponseMessageDto response = new ResponseMessageDto();
		if (changePasswordRequest.getConfirmPassword() == null || changePasswordRequest.getConfirmPassword().equals("")
				|| !changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
			throw new InputMismatchException("New Password doesn't meet criteria!!!");
		}
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		if (!changePasswordRequest.getNewPassword().matches(passwordPattern)) {
			throw new InputMismatchException(
					"Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.");
		}

		UsersResource usersResource = methodUtil.getUsersResource();
		UserRepresentation userRepresentation = usersResource.search(methodUtil.getLoggedInUserName()).get(0);
		if (changeUserPassword(userRepresentation, changePasswordRequest.getNewPassword(), Boolean.FALSE))
			response.setMessage("password updated");

		return response;
	}

	public ResponseMessageDto changePasswordSplCharWithOTP(PortalChangePasswordDto changePasswordDto)
			throws InputMismatchException, WebClientResponseException {
		ResponseMessageDto response = new ResponseMessageDto();
		if (changePasswordDto.getConfirmPassword() == null || changePasswordDto.getConfirmPassword().equals("")
				|| !changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
			throw new InputMismatchException("New Password doesn't meet criteria!!!");
		}
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		if (!changePasswordDto.getNewPassword().matches(passwordPattern)) {
			throw new InputMismatchException(
					"Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.");
		}
		Boolean status = validateCredentialsWithOTP(methodUtil.getLoggedInUserName(),
				changePasswordDto.getCurrentPassword(), changePasswordDto.getToken());
		if (status == true) {
			log.info("Current password matched");
			UsersResource usersResource = methodUtil.getUsersResource();
			UserRepresentation userRepresentation = usersResource.search(methodUtil.getLoggedInUserName()).get(0);
			if (changeUserPassword(userRepresentation, changePasswordDto.getNewPassword(), Boolean.FALSE))
				response.setMessage("password updated");
			else {
				log.info("Error updating password");
				throw new RuntimeException("Error occure while updating the password");
			}
		} else {
			throw new RuntimeException("Current Password did not match.");
		}
		return response;
	}

	public long getSessionCount(String username) {
		String userId = methodUtil.getUserIdByUsername(username);
		return methodUtil.getUsersResource().get(userId).getUserSessions().stream().count();
	}

//	public MessageDto changePassword(ChangePasswordDto changePasswordDto)
//			throws InputMismatchException, WebClientResponseException {
//		MessageDto response = new MessageDto();
//		if (changePasswordDto.getConfirmPassword() == null || changePasswordDto.getConfirmPassword().equals("")
//				|| !changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
//			throw new InputMismatchException();
//		}
//		LoginRequestDto loginRequest = new LoginRequestDto(getLoggedInUser(), changePasswordDto.getCurrentPassword(),
//				changePasswordDto.getToken());
//		long count=getSessionCount(username);
//		if(count<1) {
//
//		UsersResource usersResource = methodUtil.getUsersResource();
//		UserRepresentation userRepresentation = usersResource.search(loginRequest.getUsername()).get(0);
//		changeUserPassword(userRepresentation, changePasswordDto.getNewPassword(), Boolean.FALSE);
//		response.setMessage("password updated");
//		}else {
//			response.setMessage("password could not updated!!");
//		}
//		return response;
//	}

	public boolean changeUserPassword(UserRepresentation user, String pwd, Boolean isTemporary) {
		try {
			CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
			credentialRepresentation.setValue(pwd);
			credentialRepresentation.setTemporary(isTemporary);
			credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
			List<CredentialRepresentation> list = new ArrayList<>();
			list.add(credentialRepresentation);
			user.setCredentials(list);
			methodUtil.getUsersResource().get(user.getId()).update(user);
			log.info("Password upated on keycloak");
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage());
			log.info(e.getMessage());
		}
		return false;

	}

//	public void changeUserPassword(UserRepresentation user, String pwd, Boolean isTemporary) {
//		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
//		credentialRepresentation.setValue(pwd);
//		credentialRepresentation.setTemporary(isTemporary);
//		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
//		List<CredentialRepresentation> list = new ArrayList<>();
//		list.add(credentialRepresentation);
//		user.setCredentials(list);
//		methodUtil.getUsersResource().get(user.getId()).update(user);
//
//	}

	public String getLoggedInUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getToken().getSubject();
		UserRepresentation user = methodUtil.getUsersResource().get(userid).toRepresentation();
		String username = user.getUsername();
		return username;
	}

	public LogoutResponseDto logout(AccessTokenRequestedDto accessTokenRequestedDto) {
		LogoutResponseDto response = new LogoutResponseDto();

		try {
			// Step 1: Retrieve the associated token metadata from the database
			Optional<TokenMetadata> tokenMetadataOptional = tokenMetadataRepository
					.findByRefreshToken(accessTokenRequestedDto.getRefreshToken());

			if (tokenMetadataOptional.isPresent()) {
				// User exists in the database, proceed to blacklist and logout
				TokenMetadata tokenMetadata = tokenMetadataOptional.get();
				String userId = tokenMetadata.getUserId();

				// Blacklist all tokens for the user
				blacklistTokensForUser(userId);

				// Mark the current token as blacklisted
				tokenMetadata.setStatus(TokenStatus.BLACKLISTED);
				tokenMetadata.setUpdatedAt(LocalDateTime.now());
				tokenMetadataRepository.save(tokenMetadata);

			} else {
				// User does not exist in the database
				response.setMessage("User not found in database. Proceeding with logout only.");
			}

			// Step 2: Call Keycloak to invalidate the refresh token
			String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("client_secret", clientSecret);
			params.add("client_id", clientId);
			params.add("refresh_token", accessTokenRequestedDto.getRefreshToken());

			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
			int code = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getStatusCodeValue();

			// Set response code and message
			response.setCode(code);
			response.setMessage("logout successful.");

		} catch (Exception e) {
			response.setCode(500);
			response.setMessage("Error during logout: " + e.getMessage());
		}

		return response;
	}

	private void blacklistTokensForUser(String userId) {
		List<TokenMetadata> userTokens = tokenMetadataRepository.findAllByUserIdAndStatus(userId, TokenStatus.ACTIVE);
		for (TokenMetadata token : userTokens) {
			token.setStatus(TokenStatus.BLACKLISTED);
			token.setUpdatedAt(LocalDateTime.now());
		}
		tokenMetadataRepository.saveAll(userTokens);
	}

	private String extractAccessToken(String refreshToken) {
		try {
			// Token endpoint URL
			String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

			// Build headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			// Build parameters
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("client_id", clientId);
			params.add("client_secret", clientSecret);
			params.add("grant_type", "refresh_token");
			params.add("refresh_token", refreshToken);

			// Make the request
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
			ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

			// Extract access token from response
			if (response.getBody() != null && response.getBody().get("access_token") != null) {
				return response.getBody().get("access_token").toString();
			}

			throw new RuntimeException("Failed to extract access token from response");

		} catch (Exception e) {
			throw new RuntimeException("Error extracting access token: " + e.getMessage());
		}
	}

//	public UserDetailsDto getUserDetails() {
//		log.info("Inside GetUserDetails");
//		UserDetailsDto userDetails = new UserDetailsDto();
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
//		String userid = authentication.getName();
//		log.info("Got UserId:- "+ userid);
//		UserRepresentation user = methodUtil.getUsersResource().get(userid).toRepresentation();
//		String username = user.getUsername();
//		userDetails.setFirstName(user.getFirstName());
//		userDetails.setLastName(user.getLastName());
//		userDetails.setEmail(user.getEmail());
//		userDetails.setLoggedInUsername(username);
//		return userDetails;
//	}
	
	public UserDetailsDto getUserDetails() {

		UserDetailsDto userDetails = new UserDetailsDto();

		SecurityContext securityContext = SecurityContextHolder.getContext();

		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();

		String userid = authentication.getToken().getClaim("sub");

		log.info("userid : " + userid);
		UserRepresentation user = methodUtil.getUsersResource().get(userid).toRepresentation();

		String username = user.getUsername();

		userDetails.setFirstName(user.getFirstName());

		userDetails.setLastName(user.getLastName());

		userDetails.setEmail(user.getEmail());

		userDetails.setLoggedInUsername(username);

		return userDetails;

	}

	private boolean validateCredentialsWithOTP(String username, String password, String otp) {
		try {
			Mono<ResponseEntity<String>> response = webClient.post()
					.uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(BodyInserters.fromFormData("client_id", clientId).with("client_secret", clientSecret)
							.with("username", username).with("password", password).with("grant_type", "password")
							.with("otp", otp != null && !otp.isEmpty() ? otp : ""))
					.retrieve().toEntity(String.class);
			log.info("Ceredentials validated");
			return response.block().getStatusCode().is2xxSuccessful();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean validateCredentials(String username, String password) {
		try {
			Mono<ResponseEntity<String>> response = webClient.post()
					.uri(keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(BodyInserters.fromFormData("client_id", clientId).with("client_secret", clientSecret)
							.with("username", username).with("password", password).with("grant_type", "password"))
					.retrieve().toEntity(String.class);
			log.info("Ceredentials validated");
			return response.block().getStatusCode().is2xxSuccessful();
			// AccessTokenResponse response = userKeycloak.tokenManager().getAccessToken();
			// return true;
		} catch (Exception e) {
			return false;
		}
	}

}