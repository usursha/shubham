package com.hpy.ops360.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.AppConfigNewDto;
import com.hpy.ops360.dashboard.dto.AppConfigRequestDto;
import com.hpy.ops360.dashboard.service.AppConfigNewService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/app")
@Slf4j
public class AppConfigController {

	@Autowired
	private AppConfigNewService service;

	@Autowired
	private RestUtils restUtils;

	@PostMapping("/app-config/create")
	public ResponseEntity<IResponseDto> updateConfig(@RequestBody AppConfigRequestDto dto) {
		log.info("***** Inside updateAppConfig **********");
		log.info("Response Received from updateAppConfig service" + dto);
		AppConfigNewDto updatedDto = service.createNewConfig(dto);
		return ResponseEntity.ok(restUtils.wrapResponse(updatedDto, "App Configurations Updated Sucesssfully"));
	}

	@GetMapping("/app-config")
	public ResponseEntity<IResponseDto> getAppConfig_new() {

		log.info("***** Inside getAppConfig **********");
		AppConfigNewDto response = service.getNewConfig();
		log.info("Response Recieved from appConfigService" + response);
		return ResponseEntity.ok(restUtils.wrapResponse(response, "APP config data fetched Successfully"));

	}
}