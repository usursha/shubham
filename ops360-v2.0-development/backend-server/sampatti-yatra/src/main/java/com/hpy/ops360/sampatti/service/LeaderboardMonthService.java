package com.hpy.ops360.sampatti.service;

import com.hpy.ops360.sampatti.dto.LeaderboardMonthDto;
import com.hpy.ops360.sampatti.entity.LeaderboardMonthEntity;
import com.hpy.ops360.sampatti.repository.LeaderboardMonthRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaderboardMonthService {

	@Autowired
	private LeaderboardMonthRepository repository;

	public List<LeaderboardMonthDto> getLeaderboardMonthData() {

		List<LeaderboardMonthEntity> resultList = repository.getLeaderboardMonthData();

		List<LeaderboardMonthDto> response = resultList.stream()
				.map(e -> new LeaderboardMonthDto(e.getSrno(),e.getDisplayMonth()))
				.collect(Collectors.toList());
		

		return response;
	}
}
