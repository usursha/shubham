package com.hpy.ops360.dashboard.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.dashboard.dto.PrivacyPolicyDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrivacyPolicyService {

	public PrivacyPolicyDto getPrivacyPolicyDetails() throws IOException {
		try (InputStreamReader reader = new InputStreamReader(
				new ClassPathResource("Privacy_Terms_Conditions.json").getInputStream())) {
			ObjectMapper mapper = new ObjectMapper();
			PrivacyPolicyDto privacyPolicyDto = mapper.readValue(reader, PrivacyPolicyDto.class);
			log.debug("readValue = " + privacyPolicyDto);
			return privacyPolicyDto;
		} catch (IOException e) {
			log.error("Failed to read ATM details from JSON file", e);
			throw e;
		}
	}

}
