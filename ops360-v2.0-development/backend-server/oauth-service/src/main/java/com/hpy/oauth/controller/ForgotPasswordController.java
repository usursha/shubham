package com.hpy.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.oauth.dto.ForgotPasswordDto;
import com.hpy.oauth.service.ForgotPasswordService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/v2/app/forgot-password-service")
@CrossOrigin("${app.cross-origin.allow}")
public class ForgotPasswordController {
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@PostMapping("/reset")
	public ResponseEntity<String> resetPasswordEmail(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
		return ResponseEntity.ok(forgotPasswordService.resetPasswordEmail(forgotPasswordDto));
		
	}
}
