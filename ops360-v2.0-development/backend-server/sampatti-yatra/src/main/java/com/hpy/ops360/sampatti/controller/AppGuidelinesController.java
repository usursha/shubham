package com.hpy.ops360.sampatti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.GuidelinesDto;
import com.hpy.ops360.sampatti.entity.GuidelineDetails;
import com.hpy.ops360.sampatti.service.GuidelineService;
import com.hpy.rest.dto.ResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/app/guideline")
@CrossOrigin("${app.cross-origin.allow}")
public class AppGuidelinesController {

	@Autowired
	private GuidelineService guidelineService;
	

	@PostMapping("/save-guidelines-details")
	public ResponseEntity<String> saveGuidelinesDetails(@RequestBody String guidelineBody) {
		try {
			GuidelineDetails savedDetails = guidelineService.saveGuidelinesDetails(guidelineBody);
			String responseMessage = "HTML content saved with ID: " + savedDetails.getId();
			log.info(responseMessage);
			return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
		} catch (Exception e) {
			log.error("Error saving guideline content", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save guideline content.");
		}
	}

	@GetMapping("/get-guidelines-details")
	public ResponseEntity<ResponseDto<GuidelinesDto>> getGuidelinesDetails() {
		GuidelinesDto guidelineDto = guidelineService.getGuidelinesDetails();

		ResponseDto<GuidelinesDto> response = new ResponseDto<>();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage("Success");
		response.setData(guidelineDto);

		if (guidelineDto.getContent().isEmpty()) {
			log.warn("No guideline content found, returning empty DTO.");
			response.setMessage("No guideline content found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		log.info("Guideline details fetched successfully");
		return ResponseEntity.ok(response);
	}



}
