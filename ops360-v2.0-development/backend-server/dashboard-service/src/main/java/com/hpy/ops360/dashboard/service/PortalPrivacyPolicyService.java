package com.hpy.ops360.dashboard.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.PortalPrivacyPolicyDto;
import com.hpy.ops360.dashboard.entity.PortalPrivacyPolicyDetails;
import com.hpy.ops360.dashboard.repository.PortalPrivacyPolicyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PortalPrivacyPolicyService {
	
	@Autowired
	private PortalPrivacyPolicyRepository portalprivacypolicyrepository;
	
	public PortalPrivacyPolicyDetails savePrivcyPolicyDetails(String requestBody) {
		log.info("Saving new Privacy Policy content.");
		
		PortalPrivacyPolicyDetails details = new PortalPrivacyPolicyDetails();
		details.setContent(requestBody);
        log.info("Created new entity object with content. Now attempting to save to the database.");
		
		PortalPrivacyPolicyDetails saved = portalprivacypolicyrepository.save(details);
		log.info("Privacy Policy content saved with ID: {}", saved.getId());
		
		return saved;
	}
	
	public PortalPrivacyPolicyDto getPrivacyPolicy() {
		log.info("Fetching latest Privacy Policy content.");

		Optional<PortalPrivacyPolicyDetails> latestPrivacyPolicy = portalprivacypolicyrepository.findTopByOrderByCreatedAtDesc();
		PortalPrivacyPolicyDto response = new PortalPrivacyPolicyDto();
		
		if (latestPrivacyPolicy.isPresent()) {
			PortalPrivacyPolicyDetails privacyPolicydetails = latestPrivacyPolicy.get();
			String newPolicyContent = privacyPolicydetails.getContent();
			
			response.setContent(newPolicyContent);
			//dto.setContent(guidelineDetails.getContent());
			log.info("Latest Privacy Policy content found with ID: {}", privacyPolicydetails.getId());
		} else {
			log.warn("No Privacy Policy content found. Returning empty DTO.");
			response.setContent("");
		}
		
		return response;

	}

}
