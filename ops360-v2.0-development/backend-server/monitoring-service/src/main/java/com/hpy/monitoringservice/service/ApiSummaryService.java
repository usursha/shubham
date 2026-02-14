package com.hpy.monitoringservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.monitoringservice.dto.ControllerInternalDTO;
import com.hpy.monitoringservice.dto.ControllersummaryDTO;
import com.hpy.monitoringservice.dto.SummaryRequest;
import com.hpy.monitoringservice.entity.ApiLogSummary;
import com.hpy.monitoringservice.repository.ApiSummaryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiSummaryService {

	@Autowired
	private ApiSummaryRepository apiLogRepository;

	@Autowired
	private ObjectMapper objectMapper;

	public List<ControllersummaryDTO> getControllerStatsByInterval(SummaryRequest request) {

		log.info("*** Inside Service Layer ***");
		List<ApiLogSummary> data = apiLogRepository
				.getControllerStatsByInterval(request.getIntervalMinutes(), request.getLimitRows());

		log.info("Response Returned from REPO:- "+ data);
		List<ControllersummaryDTO> response=data.stream().map(result -> {
			List<ControllerInternalDTO> controllerDetails = parseControllerDetails(result.getControllerDetails());
			log.info("Controller Details:- "+ controllerDetails);
			return new ControllersummaryDTO(
					result.getSrno(),
					result.getStartTime(),
					result.getEndTime(),
					result.getCallCount(),
					result.getAvgTimeTaken(),
					result.getMaxTimeTaken(),
					controllerDetails
					);
		}).toList();
		log.info("Controller Details:- "+ response);
		return response;
	}

	private List<ControllerInternalDTO> parseControllerDetails(String controllerDetailsJson) {
		try {
			if (controllerDetailsJson == null || controllerDetailsJson.isBlank()) {
				log.warn("ControllerDetails JSON is null or empty, returning empty list.");
				return List.of();
			}
			log.info("ControllerDetails JSON: {}", controllerDetailsJson);
			return objectMapper.readValue(controllerDetailsJson, new TypeReference<>() {});
		} catch (Exception e) {
			log.error("JSON Parsing Error: {}", e.getMessage());
			return List.of();
		}
	}
}