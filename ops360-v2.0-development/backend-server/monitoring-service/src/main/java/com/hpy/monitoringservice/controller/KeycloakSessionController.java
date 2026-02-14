package com.hpy.monitoringservice.controller;

import java.util.List;
import java.util.Set;

import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.monitoringservice.dto.ActiveCEUsersDetails;
import com.hpy.monitoringservice.dto.ActiveCMUsersDetails;
import com.hpy.monitoringservice.dto.CountDto;
import com.hpy.monitoringservice.dto.GenericMessageDto;
import com.hpy.monitoringservice.dto.RepresentationDto;
import com.hpy.monitoringservice.dto.SevenDaysResponseData;
import com.hpy.monitoringservice.dto.TimeRequestDto;
import com.hpy.monitoringservice.dto.UserDataDto;
import com.hpy.monitoringservice.entity.UserSessionsEntity;
import com.hpy.monitoringservice.service.KeycloakSessionService;

@RestController
@RequestMapping("/session")
@CrossOrigin("${app.cross-origin.allow}")
public class KeycloakSessionController {

	@Autowired
	private KeycloakSessionService service;

	
	@PostMapping("/seven-days-list")
	public ResponseEntity<Set<UserSessionsEntity>> getUsersActiveSessions(@RequestBody TimeRequestDto request) {
		Set<UserSessionsEntity> set = service.getUserLoggedInLast7DaysList(request);
		return ResponseEntity.ok(set);
	}

	@GetMapping("/get-seven-days-data")
	public ResponseEntity<List<SevenDaysResponseData>> getSevenDaysData() {
		return ResponseEntity.ok(service.getSevenDaysData());
	}

	@PostMapping("/store-active-sessions")
	public ResponseEntity<GenericMessageDto> storeAllActiveSessions() {
		return ResponseEntity.ok(service.storeActiveSessions());
	}

	@GetMapping("/total-session-count")
	public ResponseEntity<RepresentationDto> sessionCount() {
		return ResponseEntity.ok(service.totalSessionCount());
	}

	@GetMapping("/all-count")
	public ResponseEntity<CountDto> totalCEUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		CountDto allcount = service.allCount(token);
		return ResponseEntity.ok(allcount);
	}

	@GetMapping("/current-active-session-list")
	public ResponseEntity<Set<UserSessionRepresentation>> currentActiveUsers() {
		return ResponseEntity.ok(service.currentActiveUsersSet());
	}

	@GetMapping("/active-cm-users")
	public ResponseEntity<ActiveCMUsersDetails> activeCmUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return ResponseEntity.ok(service.activeCMUserDetails(token));

	}

	@GetMapping("/active-ce-users")
	public ResponseEntity<ActiveCEUsersDetails> activeCeUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		return ResponseEntity.ok(service.activeCEUserDetails(token));
	}

	@GetMapping("/user-data")
	public ResponseEntity<UserDataDto> allUserData(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		UserDataDto allcount = service.getUserData(token);
		return ResponseEntity.ok(allcount);
	}

	@PostMapping("/seven-days-data")
	public ResponseEntity<SevenDaysResponseData> getSevenDaysData(@RequestBody TimeRequestDto request) {
		SevenDaysResponseData response = service.getSevenDaysUserData(request);
		return ResponseEntity.ok(response);
	}

}
