package com.hpy.ops360.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.entity.HelpDeskHandlerDetails;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.HelpDeskHandlerService;

@RestController
@RequestMapping("/helpdesk")
@CrossOrigin("${app.cross-origin.allow}")
public class HelpDeskController {

	@Autowired
	private HelpDeskHandlerService helpDeskHandlerService;

	@GetMapping("/details")
	@Loggable
	public ResponseEntity<HelpDeskHandlerDetails> getHelpDeskHandlerDetails() {
		return ResponseEntity.ok(helpDeskHandlerService.getHelpDeskHandlerDetails());
	}

}
