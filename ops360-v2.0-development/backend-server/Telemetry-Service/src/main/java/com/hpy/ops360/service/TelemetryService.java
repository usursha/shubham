package com.hpy.ops360.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hpy.ops360.dto.request.TelemetryRequest;
import com.hpy.ops360.dto.response.TelemetryAllResponse;
import com.hpy.ops360.dto.response.TelemetryResponse;
import com.hpy.ops360.entity.UserTelemetry;
import com.hpy.ops360.repository.TelemetryRepository;

@Service
public class TelemetryService {

	@Autowired
    private TelemetryRepository telemetryRepository;
	
	
	public TelemetryResponse saveTelemetry(TelemetryRequest request) {
        UserTelemetry telemetry = new UserTelemetry();
        telemetry.setJourneyId(request.getJourneyId());
        telemetry.setUserName(request.getUserName());
        telemetry.setScreen(request.getScreen());
        telemetry.setAction(request.getAction());
        telemetry.setDeviceModel(request.getDeviceModel());
        telemetry.setOsVersion(request.getOsVersion());
        telemetry.setInsertDateTime(request.getInsertDateTime());
        
        telemetryRepository.save(telemetry);
        
        return TelemetryResponse.builder()
                //.journeyId(request.getJourneyId())
                .successMessage("Telemetry data saved successfully")
                .build();
    }
	
	public TelemetryResponse saveAllTelemetry(List<TelemetryRequest> request) {
		List<UserTelemetry> telemetries = request.stream()
				.map(requests->{
					
				UserTelemetry telemetry = new UserTelemetry();
				telemetry.setJourneyId(requests.getJourneyId());
				telemetry.setUserName(requests.getUserName());
				telemetry.setScreen(requests.getScreen());
				telemetry.setAction(requests.getAction());
				telemetry.setDeviceModel(requests.getDeviceModel());
		        telemetry.setOsVersion(requests.getOsVersion());
				telemetry.setInsertDateTime(requests.getInsertDateTime());
				return telemetry;
				}
		
		).collect(Collectors.toList());
		telemetryRepository.saveAll(telemetries);
		
		return TelemetryResponse.builder()
				.successMessage("Telemetry data saved successfully")
				.build();
	}
	
	
	 public List<TelemetryAllResponse> getAllTelemetry() {
	        return telemetryRepository.findAll().stream()
	            .map(telemetry -> new TelemetryAllResponse(
	                telemetry.getJourneyId(),
	                telemetry.getUserName(),
	                telemetry.getScreen(),
	                telemetry.getAction(),
	                telemetry.getDeviceModel(),
	                telemetry.getOsVersion(),
	                telemetry.getInsertDateTime()
	            ))
	            .collect(Collectors.toList());
	    }

	 public TelemetryAllResponse getTelemetryByJourneyId(String journeyId) {
	        List<UserTelemetry> telemetryList = telemetryRepository.findByJourneyId(journeyId);
	        
	        if (!telemetryList.isEmpty()) {
	            UserTelemetry telemetry = telemetryList.get(0);
	            return new TelemetryAllResponse(
	                telemetry.getJourneyId(),
	                telemetry.getUserName(),
	                telemetry.getScreen(),
	                telemetry.getAction(),
	                telemetry.getDeviceModel(),
	                telemetry.getOsVersion(),
	                telemetry.getInsertDateTime()
	            );
	        }
	        return null;
	    }
}
