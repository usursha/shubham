package com.hpy.ops360.sampatti.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.LeaderBoardSearchRespCountDto;
import com.hpy.ops360.sampatti.dto.LeaderboardRequestDto;
import com.hpy.ops360.sampatti.dto.SearchLeaderboardRequestDto;
import com.hpy.ops360.sampatti.entity.LeaderboardEntity;
import com.hpy.ops360.sampatti.repository.LeaderboardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchLeaderboardService {

    @Autowired
    private LeaderboardRepository repository;

    public List<LeaderBoardSearchRespCountDto> getLeaderboardsearchData(SearchLeaderboardRequestDto request) {

        log.info("Received leaderboard search request: userType={}, monthYear={}", request.getUserType(), request.getMonthYear());

        LeaderboardRequestDto requestDto = new LeaderboardRequestDto();
        requestDto.setUserType(request.getUserType());
        requestDto.setMonthYear(request.getMonthYear());

        requestDto.setSearchKeyword("");
        requestDto.setSortOrder("desc");
        requestDto.setAchievedMin(0);
        requestDto.setAchievedMax(1000);
        requestDto.setZone(null);
        requestDto.setState(null);
        requestDto.setCity(null);

        log.debug("Constructed LeaderboardRequestDto: {}", requestDto);

        log.info("Fetching leaderboard data from repository with filters: monthYear={}, userType={}",
                requestDto.getMonthYear(), requestDto.getUserType());

        List<LeaderboardEntity> count = repository.getLeaderboardcountData(
                requestDto.getMonthYear(),
                requestDto.getUserType(),
                requestDto.getSearchKeyword(),
                requestDto.getSortOrder(),
                requestDto.getAchievedMin(),
                requestDto.getAchievedMax(),
                requestDto.getZone(),
                requestDto.getState(),
                requestDto.getCity()
        );

        log.info("Fetched {} leaderboard records", count.size());

        List<LeaderBoardSearchRespCountDto> result = count.stream()
                .map(e -> new LeaderBoardSearchRespCountDto(e.getFullName()))
                .collect(Collectors.toList());

        log.debug("Mapped leaderboard results to DTO: {}", result);

        return result;
    }
}
