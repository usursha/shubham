package com.hpy.ops360.dashboard.V2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.PortalPrivacyPolicyDto;
import com.hpy.ops360.dashboard.entity.PortalPrivacyPolicyDetails;
import com.hpy.ops360.dashboard.service.PortalPrivacyPolicyService;
import com.hpy.rest.dto.ResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v2/portal/privacy-policy")
@CrossOrigin("${app.cross-origin.allow}")

public class PortalPrivacyPolicyController {
	@Autowired
	private  PortalPrivacyPolicyService portalprivacypolicyservice;
	
	@PostMapping("/save-policy-details")
	public ResponseEntity<String> savePrivcyPolicyDetails(@RequestBody String privacypolicybody) {
		try {
			PortalPrivacyPolicyDetails savedDetails = portalprivacypolicyservice.savePrivcyPolicyDetails(privacypolicybody);
			String responseMessage = "HTML content saved with ID: " + savedDetails.getId();
			log.info(responseMessage);

			return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
		} catch (Exception e) {
			log.error("Error saving Privacy Policy content", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save Privacy Policy content.");
		}
	}
	
	
	
	@GetMapping("/get-policy-details")
	public ResponseEntity<ResponseDto<PortalPrivacyPolicyDto>>getPrivacyPolicy (){
	PortalPrivacyPolicyDto privacypolicydto = portalprivacypolicyservice.getPrivacyPolicy();
	
	ResponseDto<PortalPrivacyPolicyDto> response = new ResponseDto<>();
	response.setResponseCode(HttpStatus.OK.value());
	response.setMessage("Success");
	response.setData(privacypolicydto);
	
	if(privacypolicydto.getContent().isEmpty()) {
		log.warn("No Privacy Policy content found, returning empty DTO.");
		response.setMessage("No Privacy Policy content found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	log.info("Privacy Policy details fetched successfully");
	return ResponseEntity.ok(response);	
	
	}
	
}
