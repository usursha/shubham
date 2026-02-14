package com.hpy.ops360.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dto.request.CreatePromptsRequest;
import com.hpy.ops360.dto.response.GetPromptResponse;
import com.hpy.ops360.entity.Prompt;
import com.hpy.ops360.entity.PromptMessage;
import com.hpy.ops360.repository.PromptMessageRepository;
import com.hpy.ops360.repository.PromptRepository;

@Service
public class PromptService {

	@Autowired
	private PromptRepository promptRepository;

	@Autowired
	private PromptMessageRepository promptMessageRepository;

	@Value("${keycloak.realm}")
	private String realm;

	@Autowired
	private Keycloak keycloak;
	
	
	public List<Prompt> getPrompts(CreatePromptsRequest request) {
		String username = getLoggedInUser();
		return promptRepository.callGetPromptsStoredProcedure(username, request.getEventCode());
	}

//	public List<GetPromptResponse> getPrompts(CreatePromptsRequest request) {
//	    String username = getLoggedInUser();
//	    List<Prompt> prompts = promptRepository.callGetPromptsStoredProcedure(username, request.getEventCode());
//	    return prompts.stream()
//	        .map(this::convertToGetPromptResponse)
//	        .collect(Collectors.toList());
//	}
//
//	private GetPromptResponse convertToGetPromptResponse(Prompt prompt) {
//	    GetPromptResponse response = new GetPromptResponse();
//	    response.setEventCode(prompt.getEventCode());
//	    response.setPromptNo(prompt.getPromptNo());
//	    response.setPromptDescription(prompt.getPromptDescription());
//	    return response;
//	}
//	
	

	public List<PromptMessage> getPromptMessages(String subCallType) {

		return promptMessageRepository.getPromptMessages(subCallType).isEmpty() ? Collections.emptyList()
				: promptMessageRepository.getPromptMessages(subCallType);

	}

	public String getLoggedInUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) securityContext.getAuthentication();
		String userid = authentication.getName();
		UserRepresentation user = keycloak.realm(realm).users().get(userid).toRepresentation();
		String username = user.getUsername();
		return username;
	}
}
