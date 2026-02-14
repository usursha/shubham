package com.hpy.ops360.sampatti.service;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.UserIncentiveMonthYearDto;
import com.hpy.ops360.sampatti.entity.UserIncentiveMonthYearEntity;
import com.hpy.ops360.sampatti.repository.UserIncentiveMonthYearRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserIncentiveMonthYearService {

    private UserIncentiveMonthYearRepository repository;
    
	public UserIncentiveMonthYearService(UserIncentiveMonthYearRepository repository) {
		super();
		this.repository = repository;
	}
	
	public List<UserIncentiveMonthYearDto> getIncentivesMonthYear() {
		try {
			log.info("Enter inside getIncentivesMonthYear Method.");
			List<UserIncentiveMonthYearEntity> entities = repository.getUserIncentivesMonthYear();

			if (entities == null || entities.isEmpty()) {
				log.info("No incentive month-year data found.");
				return Collections.emptyList();
			}

			// Only return the month year
			List<UserIncentiveMonthYearDto> response = entities.stream()
					.map(entity -> new UserIncentiveMonthYearDto(entity.getMonthYear())).toList();

			log.info("Fetched {} unique month-year values.", response.size());
			return response;

		} catch (Exception ex) {
			log.error("Error fetching month-year incentive data", ex);
			throw new RuntimeException("Failed to fetch incentive month-year data");
		}

	}




}
