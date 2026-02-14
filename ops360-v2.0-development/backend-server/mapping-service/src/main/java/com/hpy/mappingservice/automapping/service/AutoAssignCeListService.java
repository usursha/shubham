package com.hpy.mappingservice.automapping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.automapping.repo.AutoAssignCeListRepository;
import com.hpy.mappingservice.automapping.responseDto.AutoAssignCeListResponseDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutoAssignCeListService {

	@Autowired
	private AutoAssignCeListRepository autoAssignRepository;

	public List<AutoAssignCeListResponseDto> getAutoAssignRemainingCE(String cmUserId, String excludedCeUserId) {
		try {
			List<Object[]> results = autoAssignRepository.executeAutoAssignRemainingCE(cmUserId, excludedCeUserId);

			return results.stream().map(this::mapToDTO).collect(Collectors.toList());

		} catch (Exception e) {
			throw new RuntimeException("Error executing stored procedure: " + e.getMessage(), e);
		}
	}

	private AutoAssignCeListResponseDto mapToDTO(Object[] row) {
		return new AutoAssignCeListResponseDto(null, (String) row[0], // ce_user_id
				 (String) row[1], //ce_user_name
				(String) row[2], // city
				(String) row[3], // employee_code
				(String) row[4], // home_address
				(Integer) row[5], // atm_count
				(Integer) row[6], // mapped_atm
				(Integer) row[7] // remaining
		);
	}
}
