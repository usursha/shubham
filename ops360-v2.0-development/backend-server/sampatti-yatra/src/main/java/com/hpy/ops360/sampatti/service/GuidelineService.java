package com.hpy.ops360.sampatti.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.GuidelinesDto;
import com.hpy.ops360.sampatti.entity.GuidelineDetails;
import com.hpy.ops360.sampatti.repository.GuidelineRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GuidelineService {

	@Autowired
	private GuidelineRepository guidelineRepo;
	

	public GuidelineDetails saveGuidelinesDetails(String requestBody) {
		log.info("Saving new guideline content.");

		GuidelineDetails details = new GuidelineDetails();
		details.setContent(requestBody);

		GuidelineDetails saved = guidelineRepo.save(details);
		log.info("Guideline content saved with ID: {}", saved.getId());

		return saved;
	}
	
	
		public GuidelinesDto getGuidelinesDetails() {
			log.info("Fetching latest guideline content.");

			Optional<GuidelineDetails> latestGuideline = guidelineRepo.findTopByOrderByCreatedAtDesc();

			GuidelinesDto response = new GuidelinesDto();

			if (latestGuideline.isPresent()) {
				GuidelineDetails guidelineDetails = latestGuideline.get();
				String cleanedContent = guidelineDetails.getContent();
//				String cleanedContentAll = cleanedContent.replaceAll("\\\\", "").replaceAll("[\\r\\n]", "");
				response.setContent(cleanedContent);
				//dto.setContent(guidelineDetails.getContent());
				log.info("Latest guideline content found with ID: {}", guidelineDetails.getId());
			} else {
				log.warn("No guideline content found. Returning empty DTO.");
				response.setContent("");
			}

			return response;
		}


	
	

}
