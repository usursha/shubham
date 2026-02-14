package com.hpy.ops360.atmservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.BankNameAndAtmUpTimeDto;
import com.hpy.ops360.atmservice.entity.BankNameAndAtmUpTimeEntity;
import com.hpy.ops360.atmservice.repository.BankNameAndAtmUpTimeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankNameAndAtmUpTimeService {

	@Autowired
	private BankNameAndAtmUpTimeRepository bankNameAndAtmUpTimeRepository;

	public BankNameAndAtmUpTimeDto getBankNameAndAtmUpTime(String atmId) {
		BankNameAndAtmUpTimeDto response = new BankNameAndAtmUpTimeDto();
		
		if (atmId == null || atmId.trim().isEmpty()) {
			log.warn("Invalid ATM ID: {}", atmId);
			return null;
		}

		try {
			BankNameAndAtmUpTimeEntity data = bankNameAndAtmUpTimeRepository.getBankNameAndAtmUpTime(atmId);
			if (data == null) {
				log.info("No data found for ATM ID: {}", atmId);
				return null;
			}

			response.setSrno(data.getSrNo());
			response.setBankName(data.getBankName());
			response.setMtdUptime(data.getMtdUptime());
			// Add previous day's timestamp (T-1) with truncated nanoseconds
			LocalDateTime previousDay = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59)
					.withNano(0); // This line removes the nanoseconds
			response.setDateTime(previousDay);

		} catch (Exception e) {
			log.error("Error fetching ATM uptime details for ATM ID: {}", atmId, e);
			throw new RuntimeException("Failed to retrieve ATM uptime details", e);
		}

		return response;
	}

}
