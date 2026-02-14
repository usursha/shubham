package com.MapPUC.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MapPUC.dto.UserdistLocationDto;
import com.MapPUC.dto.UserdistLocationRequest;
import com.MapPUC.entity.UserDistLocation;
import com.MapPUC.repo.UserDistLocationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DistanceCalculateService {

	@Autowired
	private UserDistLocationRepository distanceCalcRepository;

	public List<UserdistLocationDto> getLatLongList(UserdistLocationRequest request) {

		log.info("Inside getLatLongList Service");
		log.info("Request Recieved in service:- "+ request);
		List<UserDistLocation> data = distanceCalcRepository.findByUsernameAndDate(request.getUsername(),
				request.getCreated_on());
		log.info("Response Recieved from repo:- "+ data);
		List<UserdistLocationDto> response = data.stream().map(result -> new UserdistLocationDto(result.getSrno(),
				result.getUsername(), result.getLatitude(), result.getLongitude(), result.getCreatedOn())).toList();

		log.info("Response returned to Controller:- "+ response);
		return response;

	}

}