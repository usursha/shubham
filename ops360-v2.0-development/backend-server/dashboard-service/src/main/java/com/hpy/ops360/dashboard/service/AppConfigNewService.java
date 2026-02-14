package com.hpy.ops360.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.AppConfigNewDto;
import com.hpy.ops360.dashboard.dto.AppConfigRequestDto;
import com.hpy.ops360.dashboard.entity.AppConfigNew;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.AppConfigNewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppConfigNewService {

	@Autowired
	private AppConfigNewRepository repository;

	public AppConfigNewDto createNewConfig(AppConfigRequestDto dto) {
		AppConfigNew config = new AppConfigNew();
		config.setVersion(dto.getVersion());
		config.setApkUrl(dto.getApkUrl());
		config.setLocationFrequency(dto.getLocationFrequency());
		config.setApiFrequency(dto.getApiFrequency());
		config.setEnableLog(dto.getEnableLog());
		config.setEnableScreenshot(dto.getEnableScreenshot());
		config.setEnableDeveloperMode(dto.getEnableDeveloperMode());
		config.setMaximumFileSize(dto.getMaximumFileSize());
		config.setTicketMaximumFileUpload(dto.getTicketMaximumFileUpload());
		config.setCreatedDate(LocalDateTime.now());

		// Save entity
		AppConfigNew savedConfig = repository.save(config);

		// Map back to DTO
		return mapToDto(savedConfig);
	}

	private AppConfigNewDto mapToDto(AppConfigNew config) {
		AppConfigNewDto dto = new AppConfigNewDto();
		dto.setVersion(config.getVersion());
		dto.setApkUrl(config.getApkUrl());
		dto.setLocationFrequency(config.getLocationFrequency());
		dto.setApiFrequency(config.getApiFrequency());
		dto.setEnableLog(config.getEnableLog());
		dto.setEnableScreenshot(config.getEnableScreenshot());
		dto.setEnableDeveloperMode(config.getEnableDeveloperMode());
		dto.setMaximumFileSize(config.getMaximumFileSize());
		dto.setTicketMaximumFileUpload(config.getTicketMaximumFileUpload());
		dto.setCreatedDate(config.getCreatedDate());

		return dto;
	}

	public AppConfigNewDto getNewConfig() {
		AppConfigNew data = repository.findLatestConfig();

		AppConfigNewDto response = new AppConfigNewDto();
		response.setVersion(data.getVersion());
		response.setApkUrl(data.getApkUrl());
		response.setLocationFrequency(data.getLocationFrequency());
		response.setApiFrequency(data.getApiFrequency());
		response.setEnableLog(data.getEnableLog());
		response.setEnableScreenshot(data.getEnableScreenshot());
		response.setEnableDeveloperMode(data.getEnableDeveloperMode());
		response.setMaximumFileSize(data.getMaximumFileSize());
		response.setTicketMaximumFileUpload(data.getTicketMaximumFileUpload());
		response.setCreatedDate(data.getCreatedDate());

		return response;
	}

}
