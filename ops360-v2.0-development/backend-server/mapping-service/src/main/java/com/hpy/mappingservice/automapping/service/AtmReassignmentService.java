package com.hpy.mappingservice.automapping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.automapping.repo.AutoAssignRepository;
import com.hpy.mappingservice.automapping.responseDto.AutoAssignResponseDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmReassignmentService {

	@Autowired
	private AutoAssignRepository autoAssignRepository;

	public List<AutoAssignResponseDto> getAtmAutoAssignByOriginalCEUserId(String originalCEUserId) {
		try {
			log.info("Fetching ATM auto assign data for originalCEUserId: {}", originalCEUserId);

			List<Object[]> results = autoAssignRepository.getAtmAutoAssignByOriginalCEUserId(originalCEUserId);
			List<AutoAssignResponseDto> responseList = new ArrayList<>();
			for (Object[] row : results) {
				AutoAssignResponseDto dto = new AutoAssignResponseDto();
				dto.setAtmCode(row[0] != null ? row[0].toString() : null);
				dto.setAddress(row[1] != null ? row[1].toString() : null);
				dto.setNewCeUserId(row[2] != null ? row[2].toString() : null);
				dto.setFullName(row[3] != null ? row[3].toString() : null);

				dto.setNewCeId(row[4] != null ? (Long) row[4] : null);

				dto.setDistance(row[5] != null ? Double.parseDouble(row[5].toString()) : null);

				dto.setBankName(row[6] != null ? row[6].toString() : null);
				dto.setStatus(row[7] != null ? row[7].toString() : null);
				dto.setCity(row[8] != null ? row[8].toString() : null);

				responseList.add(dto);
			}

			log.info("Successfully fetched {} records for originalCEUserId: {}", responseList.size(), originalCEUserId);
			return responseList;

		} catch (Exception e) {
			log.error("Error occurred while fetching ATM auto assign data for originalCEUserId: {}", originalCEUserId,
					e);
			throw new RuntimeException("Failed to fetch ATM auto assign data: " + e.getMessage(), e);
		}
	}
}