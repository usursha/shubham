package com.hpy.mappingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.LeaveUserEntity;
import com.hpy.mappingservice.repository.LeaveUserRepository;
import com.hpy.mappingservice.response.dto.LeaveUserDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveUserService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private LeaveUserRepository leaveUserRepository;

	public List<LeaveUserDto> getApprovedLeaveUsers(String username) {
		log.info("******* Inside getApprovedLeaveUsers service Method *********");

		String managerUsername = loginService.getLoggedInUser();
		log.info("Manager Username: {}", managerUsername);

		List<LeaveUserEntity> entities = leaveUserRepository.getApprovedLeaveUsersByManager(managerUsername);

		List<LeaveUserDto> response = entities.stream().map(entity -> {
			LeaveUserDto dto = new LeaveUserDto();
			dto.setUserId(entity.getUserId());
			dto.setEmployeeCode(entity.getEmployeeCode());
			dto.setUserName(entity.getUserName());
			dto.setLeaveId(entity.getLeaveId());
			dto.setStartRange(entity.getStartRange());
			dto.setEndRange(entity.getEndRange());
			dto.setFullName(entity.getFullName());
			dto.setEmailId(entity.getEmailId());
			dto.setAddress(entity.getAddress());
			dto.setMappedAtm(entity.getMappedAtm());
			dto.setTotalAtms(entity.getTotalAtms());
			dto.setPercentageMapped(entity.getPercentageMapped());
			dto.setProfilePic(entity.getProfilePic());
			return dto;
		}).filter(dto -> username == null || username.trim().isEmpty() || username.equalsIgnoreCase(dto.getUserName()))
				.collect(Collectors.toList());

		return response;
	}

}
