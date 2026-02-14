package com.hpy.ops360.dashboard.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.AppConfigDto;
import com.hpy.ops360.dashboard.dto.UserMastDto;
import com.hpy.ops360.dashboard.entity.AppConfigEntity;
import com.hpy.ops360.dashboard.entity.AppFlagStatus;
import com.hpy.ops360.dashboard.entity.UserMast;
import com.hpy.ops360.dashboard.repository.AppFlagStatusRepository;
import com.hpy.ops360.dashboard.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LoginService loginservice;
    
    @Autowired
	private AppFlagStatusRepository appFlagStatusRepository;

    public UserMastDto getUserMast() {
    	String username=loginservice.getLoggedInUser();
    	log.info("****** Inside getUserDetails Service ******");
        log.info("Got User from LoginService:- "+ username);
    	
        Optional<UserMast> userEntity = userRepository.findByUsername(username);
		List<AppFlagStatus> appFlagStatuses = appFlagStatusRepository.getAppFlagStatus(username);
		UserMastDto dto = new UserMastDto();
        if (userEntity.isPresent()) {
            UserMast entity = userEntity.get();
            
            dto.setUsername(entity.getUsername() != null && !entity.getUsername().isEmpty() ? entity.getUsername() : "");
            dto.setMobileNo(entity.getMobileNo() != null && !entity.getMobileNo().isEmpty() ? entity.getMobileNo() : "");
            dto.setFirstName(entity.getFirstName() != null && !entity.getFirstName().isEmpty() ? entity.getFirstName() : "");
            dto.setLastName(entity.getLastName() != null && !entity.getLastName().isEmpty() ? entity.getLastName() : "");
            dto.setEmployeeCode(entity.getEmployeeCode() != null && !entity.getEmployeeCode().isEmpty() ? entity.getEmployeeCode() : "");
            dto.setCircleArea(entity.getCircleArea() != null && !entity.getCircleArea().isEmpty() ? entity.getCircleArea() : "");
            dto.setUserEmail(entity.getUserEmail() != null && !entity.getUserEmail().isEmpty() ? entity.getUserEmail() : "");
            dto.setZone(entity.getZone() != null && !entity.getZone().isEmpty() ? entity.getZone() : "");
//            log.info("Returned UserMastDto from service:- "+ dto);    
        }
        else {
        	dto.setUsername("");
            dto.setMobileNo("");
            dto.setFirstName("");
            dto.setLastName("");
            dto.setEmployeeCode("");
            dto.setCircleArea("");
            dto.setUserEmail("");
            dto.setZone("");
        }
        dto.setIsScreenEnable(appFlagStatuses.isEmpty()?0:appFlagStatuses.get(0).getValueColumn());
        dto.setIsLogEnable(appFlagStatuses.isEmpty()?0:appFlagStatuses.get(1).getValueColumn());
        return dto; // Or handle it appropriately
    }
}
