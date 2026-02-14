package com.hpy.ops360.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.entity.ApplicationVersion;
import com.hpy.ops360.dashboard.repository.ApplicationVersionRepository;

@Service
public class ApplicationVersionService {

	@Autowired
	private ApplicationVersionRepository applicationVersionRepository;

	public ApplicationVersion checkUpdateAvailable(String appPlateform, String applicationVersion) {
		ApplicationVersion appVersionResponse = applicationVersionRepository.checkUpdateAvailable(appPlateform,
				applicationVersion);
		if (applicationVersion == null) {
			return new ApplicationVersion(0L, 1);
		}
		return appVersionResponse;
	}

}
