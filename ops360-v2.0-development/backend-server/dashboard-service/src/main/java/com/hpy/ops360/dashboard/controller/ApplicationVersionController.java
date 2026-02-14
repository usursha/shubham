package com.hpy.ops360.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.entity.ApplicationVersion;
import com.hpy.ops360.dashboard.service.ApplicationVersionService;

@RestController
@RequestMapping("/application")
@CrossOrigin("${app.cross-origin.allow}")
public class ApplicationVersionController {

	@Autowired
	private ApplicationVersionService applicationVersionService;

	@PostMapping("/check-update/{appPlatform}/{appVersion}")
	public ResponseEntity<ApplicationVersion> checkApplicationUpdate(@PathVariable("appPlatform") String appPlatform,
			@PathVariable("appVersion") String appVersion) {
		return ResponseEntity.ok(applicationVersionService.checkUpdateAvailable(appPlatform, appVersion));
	}

}
