package com.hpy.ops360.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dto.request.CreatePromptsRequest;
import com.hpy.ops360.dto.request.PromptMessageRequest;
import com.hpy.ops360.dto.response.GetPromptResponse;
import com.hpy.ops360.entity.Prompt;
import com.hpy.ops360.entity.PromptMessage;
import com.hpy.ops360.service.PromptService;
import com.hpy.ops360.util.RestUtils;
import com.hpy.rest.dto.IResponseDto;

@RestController
@RequestMapping("/prompts")
@CrossOrigin("${app.cross-origin.allow}")
public class PromptController {

	@Autowired
	private PromptService promptService;

	@Autowired
	private RestUtils restUtils;

//	@PostMapping("/getPrompts")
//	public ResponseEntity<IResponseDto> getPrompts(@RequestBody CreatePromptsRequest request) {
//		List<GetPromptResponse> prompts = promptService.getPrompts(request);
//		return ResponseEntity.ok(restUtils.wrapResponse(prompts, "Prompts retrieved successfully"));
//
//	}
	@PostMapping("/getPrompts")
	public List<Prompt> getPrompts(@RequestBody CreatePromptsRequest request) {
		return promptService.getPrompts(request);
	}

	@PostMapping("/getPromptMessages")
	public ResponseEntity<List<PromptMessage>> getPromptMessages(@RequestBody PromptMessageRequest request) {
		return ResponseEntity.ok(promptService.getPromptMessages(request.getSubCallType()));
	}
}
