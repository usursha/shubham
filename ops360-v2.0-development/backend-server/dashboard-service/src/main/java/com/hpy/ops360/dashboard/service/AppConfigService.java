package com.hpy.ops360.dashboard.service;

import com.hpy.ops360.dashboard.dto.AppConfigDto;
import com.hpy.ops360.dashboard.entity.AppConfigEntity;
import com.hpy.ops360.dashboard.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppConfigService {

	@Autowired
	private AppConfigRepository appConfigRepository;

	public AppConfigDto getAppConfig() {
		Optional<AppConfigEntity> appConfigEntity = appConfigRepository.findById(1);
		if (appConfigEntity.isPresent()) {
			AppConfigEntity entity = appConfigEntity.get();
			AppConfigDto dto = new AppConfigDto();
			dto.setVersion(entity.getVersion());
			dto.setApkUrl(entity.getApkUrl());
			dto.setLocationFrequency(entity.getLocationFrequency());
			dto.setApiFrequency(entity.getApiFrequency());
			dto.setEnableLog(entity.isEnableLog());
			dto.setEnableScreenshot(entity.isEnableScreenshot());
			dto.setEnableDeveloperMode(entity.isEnableDeveloperMode());
			dto.setMaximumFileSize(entity.getMaximumFileSize());
			dto.setTicketMaximumFileUpload(entity.getTicketMaximumFileUpload());
			return dto;
		}
		return null; // Or handle it appropriately
	}

	public void updateAppConfig(AppConfigDto dto) {
		// Fetch the existing configuration where ID is 1
		AppConfigEntity appConfig = appConfigRepository.findById(1).orElse(null);

		if (appConfig != null) {
			// Update the fields with the new values from the DTO
			appConfig.setVersion(dto.getVersion());
			appConfig.setApkUrl(dto.getApkUrl());
			appConfig.setLocationFrequency(dto.getLocationFrequency());
			appConfig.setApiFrequency(dto.getApiFrequency());
			appConfig.setEnableLog(dto.isEnableLog());
			appConfig.setEnableScreenshot(dto.isEnableScreenshot());
			appConfig.setEnableDeveloperMode(dto.isEnableDeveloperMode());
			appConfig.setMaximumFileSize(dto.getMaximumFileSize());
			appConfig.setTicketMaximumFileUpload(dto.getTicketMaximumFileUpload());

			// Save the updated entity back to the database
			appConfigRepository.save(appConfig);
		}
	}
}
