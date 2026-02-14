package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.ATMStatusFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.BankFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.GradeFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.IndexOfAtmFilterResponseDto;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.dto.OwnerFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.SortFilterResponseDto;
import com.hpy.ops360.atmservice.dto.UptimeStatusFilterResponseDTO;
import com.hpy.ops360.atmservice.repository.Index_Of_AtmFilterRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IndexOfAtmFilterService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private Index_Of_AtmFilterRepository atmFilterRepository;

	public IndexOfAtmFilterResponseDto getAllFilterCounts() {
		String userId = loginService.getLoggedInUser();
		try {
			return atmFilterRepository.getAllFilterCounts(userId);
		} catch (Exception e) {
			throw new RuntimeException("Error fetching ATM filter counts for user: " + userId, e);
		}
	}

}
