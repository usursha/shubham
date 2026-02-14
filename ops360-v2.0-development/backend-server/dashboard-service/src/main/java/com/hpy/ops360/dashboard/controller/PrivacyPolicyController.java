package com.hpy.ops360.dashboard.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.PrivacyPolicyDto;
import com.hpy.ops360.dashboard.service.PrivacyPolicyService;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("privacy")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class PrivacyPolicyController {

	private PrivacyPolicyService privacyPolicyService;

	@GetMapping("/policy")
	@Timed
	public ResponseEntity<PrivacyPolicyDto> getNotificationDetails() {

		try {
			return ResponseEntity.ok(privacyPolicyService.getPrivacyPolicyDetails());
		} catch (IOException e) {

			return ResponseEntity.notFound().build();
		}

	}
}
