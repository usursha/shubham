package com.hpy.ops360.sampatti.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.sampatti.entity.AtmUptimeData;
import com.hpy.ops360.sampatti.repository.AtmCeMappingRepository;
import com.hpy.ops360.sampatti.repository.AtmUptimeDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtmUptimeSchedulerService {

	@Autowired
	private AtmCeMappingRepository atmCeMappingRepository;

	@Autowired
	private AtmUptimeDataRepository uptimeRepo;
	
	@Value("${synergy.base-url}")
	private String synergyBaseUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Scheduled(cron = "0 00 01 * * ?")
	public void fetchAndSaveAtmUptimeData() {
		log.info("Scheduler started for fetching ATM uptime data.");

		List<String> atmIds = atmCeMappingRepository.findAllAtmIdsNative();
		log.info("Retrieved {} ATM IDs from the database.", atmIds.size());

		List<String> failedAtms = new ArrayList<>();

		for (String atmId : atmIds) {
			try {
				fetchAndSaveUptime(atmId);
				log.info("Successfully saved uptime data for ATM: {}", atmId);
			} catch (Exception e) {
				log.warn("Error fetching uptime for ATM {}: {}", atmId, e.getMessage(), e);
				failedAtms.add(atmId);
				log.info("Added in failed atms {}", atmId);
			}
		}

		int retryCount = 0;
//		final int maxRetries = 3;

		while (!failedAtms.isEmpty()) {
			retryCount++;
			log.info("Retry attempt {} for {} failed ATM IDs.", retryCount, failedAtms.size());

			List<String> retryFails = new ArrayList<>();

			for (String atmId : failedAtms) {
				try {
					fetchAndSaveUptime(atmId);
					log.info("Retry succeeded for ATM: {}", atmId);
				} catch (Exception e) {
					log.warn("Retry error for ATM {}: {}", atmId, e.getMessage(), e);
					retryFails.add(atmId);
				}
			}

			failedAtms = retryFails;
		}

		if (!failedAtms.isEmpty()) {
			log.error("Final failure: Could not fetch uptime for {} ATM IDs after {} retries: {}", failedAtms.size(),
					retryCount, failedAtms);
		} else {
			log.info("All ATM uptime data fetched successfully after {} retries.", retryCount);
		}

		log.info("✅ Scheduler completed.");
	}

	private String fetchRequestId() {
		String url = synergyBaseUrl + "/trequest";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> body = Map.of("username", "OPS360API", "password", "Cogent@123");

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				String requestId = String.valueOf(response.getBody().get("requestid"));
				log.info("Received request ID: {}", requestId);
				return requestId;
			} else {
				log.error("Failed to fetch request ID. Response: {}", response.getStatusCode());
			}
		} catch (Exception e) {
			log.error("Exception while fetching request ID: {}", e.getMessage(), e);
		}

		return null;
	}

	private void fetchAndSaveUptime(String atmId) {
		String requestId = fetchRequestId();
		if (requestId == null) {
			log.warn("Failed to fetch request ID for ATM ID: {}", atmId);
			throw new RuntimeException("Request ID generation failed for ATM: " + atmId);
		}

		String url = synergyBaseUrl + "/getatmuptime";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> payload = Map.of("requestid", requestId, "atmid", atmId);
		HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

		log.info("📡 Sending uptime request for ATM ID: {} with Request ID: {}", atmId, requestId);

		// Step 3: Call uptime API
		ResponseEntity<Map> response;
		try {
			response = restTemplate.postForEntity(url, request, Map.class);
		} catch (Exception e) {
			log.error("Exception while calling uptime API for ATM {}: {}", atmId, e.getMessage(), e);
			throw new RuntimeException("Error calling uptime API for ATM: " + atmId, e);
		}

		// Step 4: Handle response
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Map<String, Object> body = response.getBody();
			log.debug("Uptime response received for ATM {}: {}", atmId, body);

			AtmUptimeData data = new AtmUptimeData();
			data.setDate(LocalDate.now().minusDays(1)); // Yesterday
			data.setAtmid(atmId);
			data.setMonthtotilldateuptime(String.valueOf(body.get("monthtotilldateuptime")));
			data.setLastmonthuptime(String.valueOf(body.get("lastmonthuptime")));

			uptimeRepo.save(data);
			log.info("Uptime data saved for ATM {} on date {}", atmId, data.getDate());
		} else {
			log.warn("Failed to fetch uptime data for ATM {} | Status: {} | Response: {}", atmId,
					response.getStatusCode(), response.getBody());
			throw new RuntimeException(
					"Failed to get uptime for ATM: " + atmId + " | Status: " + response.getStatusCode());
		}
	}

}
