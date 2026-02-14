package com.hpy.uam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.uam.dto.RegistrationEmailDTO;
import com.hpy.uam.service.EmailService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notification-service")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
@Slf4j
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendMailToUser")
	public ResponseEntity<String> sendMailToUser(@RequestBody RegistrationEmailDTO registrationEmailDTO) {

		log.info("Email method called");

		String response = emailService.sendMailToUser(registrationEmailDTO);

		return ResponseEntity.ok(response);

	}
}
