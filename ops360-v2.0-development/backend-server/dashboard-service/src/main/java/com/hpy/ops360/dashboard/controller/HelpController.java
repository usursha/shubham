package com.hpy.ops360.dashboard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.HelpDto;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.HelpService;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/help")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class HelpController {

	private final HelpService helpService;

	@GetMapping("/details")
	@Timed
	@Loggable
	public ResponseEntity<List<HelpDto>> getHelplineDetails() {
		return ResponseEntity.ok(helpService.getHelplineDetails());
	}

	@PostMapping("/add")
	@Timed
	@Loggable
	public ResponseEntity<HelpDto> addHelplineDetails(@RequestBody HelpDto helpDto) {
		return ResponseEntity.ok(helpService.addHelplineDetails(helpDto));
	}
}
