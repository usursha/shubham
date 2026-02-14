package com.hpy.ops360.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dto.request.TelemetryRequest;
import com.hpy.ops360.dto.request.TelemetryRequestWrapper;
import com.hpy.ops360.dto.response.TelemetryAllResponse;
import com.hpy.ops360.dto.response.TelemetryResponse;
import com.hpy.ops360.service.TelemetryService;
import com.hpy.ops360.util.RestUtils;
import com.hpy.rest.dto.IResponseDto;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

	@Autowired
	private TelemetryService telemetryService;

	@Autowired
	private RestUtils restUtils;

	@PostMapping("/create")
	public ResponseEntity<TelemetryResponse> createTelemetry(@RequestBody TelemetryRequest request) {
		TelemetryResponse response = telemetryService.saveTelemetry(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/createAll")
	public ResponseEntity<IResponseDto> createAllTelemetry(@RequestBody TelemetryRequestWrapper request) {
		TelemetryResponse response = telemetryService.saveAllTelemetry(request.getData());
		return ResponseEntity.ok(restUtils.wrapResponse(response, "Telemetry data saved successfully"));
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<TelemetryAllResponse>> getAllTelemetry() {
		List<TelemetryAllResponse> responses = telemetryService.getAllTelemetry();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{journeyId}")
	public ResponseEntity<TelemetryAllResponse> getTelemetryByJourneyId(@PathVariable String journeyId) {
		TelemetryAllResponse telemetry = telemetryService.getTelemetryByJourneyId(journeyId);
		return ResponseEntity.ok(telemetry);
	}
}
