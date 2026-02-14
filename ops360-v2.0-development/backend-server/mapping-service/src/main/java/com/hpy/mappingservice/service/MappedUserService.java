package com.hpy.mappingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.MappedUserEntity;
import com.hpy.mappingservice.repository.MappedUserRepository;
import com.hpy.mappingservice.request.CE_userId;
import com.hpy.mappingservice.response.dto.MappedUserDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MappedUserService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private MappedUserRepository mappedUserRepository;

	public List<MappedUserDto> getMappedUsers(CE_userId ceUserRequest) {

		log.info("******* Inside getMappedUsers service Method *********");
		String managerUsername = loginService.getLoggedInUser();
		String ceUserToExclude = ceUserRequest.getExcludeCEUsername();
		log.info("Manager Username and CE name: {}", managerUsername, ceUserRequest.getExcludeCEUsername());
		List<MappedUserEntity> entities = mappedUserRepository.getMappedUsersByManager(managerUsername,
				ceUserToExclude);

		List<MappedUserDto> response = entities.stream().map(entity -> {
			MappedUserDto dto = new MappedUserDto();
			dto.setUserId(entity.getUserId());
			dto.setEmployeeCode(entity.getEmployeeCode());
			dto.setUserName(entity.getUserName());
			dto.setFullName(entity.getFullName());
			dto.setAddress(entity.getAddress());
			dto.setCity(entity.getCity());
			dto.setMappedAtm(entity.getMappedAtm());
			dto.setAssignedAtms(entity.getAssignedAtms());
			dto.setRemainingAtms(entity.getRemainingAtms());
			dto.setProfilepic(entity.getProfilepic());
			return dto;
		}).collect(Collectors.toList());

		return response;
	}

	public MappedUserDto getSecondaryMappedUsers(CE_userId ceUserRequest) {

		log.info("******* Inside getSecondaryMappedUsers service Method *********");
		String managerUsername = loginService.getLoggedInUser();
		String ceUserToExclude = ceUserRequest.getExcludeCEUsername();
		log.info("Manager Username and CE name: {}", managerUsername, ceUserRequest.getExcludeCEUsername());
		MappedUserDto response = null;

		MappedUserEntity entities = mappedUserRepository.getMappedSecondaryUsersByManager(managerUsername,
				ceUserToExclude);

		if (entities != null) {
			response = new MappedUserDto();

			response.setUserId(entities.getUserId());
			response.setEmployeeCode(entities.getEmployeeCode());
			response.setUserName(entities.getUserName());
			response.setFullName(entities.getFullName());
			response.setAddress(entities.getAddress());
			response.setCity(entities.getCity());
			response.setMappedAtm(entities.getMappedAtm());
			response.setAssignedAtms(entities.getAssignedAtms());
			response.setRemainingAtms(entities.getRemainingAtms());
			response.setProfilepic(entities.getProfilepic());
		}

		return response;
	}
}
