package com.hpy.ops360.sampatti.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.FilterdataList;
import com.hpy.ops360.sampatti.dto.LeaderBoardRespCountDto;
import com.hpy.ops360.sampatti.dto.LeaderboardRequestDto;
import com.hpy.ops360.sampatti.dto.LeaderboardResponseDto;
import com.hpy.ops360.sampatti.dto.PortalLeaderBoardRespCountDto;
import com.hpy.ops360.sampatti.entity.LeaderboardEntity;
import com.hpy.ops360.sampatti.repository.LeaderboardRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaderboardService {

	@Value("${profile-picture.url}")
	private String profilePictureUrl;

	@Autowired
	private LeaderboardRepository repository;

//	@Autowired
//	private AssetRepo assetRepo;

	public PortalLeaderBoardRespCountDto getportalLeaderboardData(HttpServletRequest httpRequest,
			LeaderboardRequestDto requestDto) {
		log.info("Fetching leaderboard data for month: {}", requestDto.getMonthYear());

		List<LeaderboardEntity> resultList = repository.getLeaderboardData(requestDto.getMonthYear(),
				requestDto.getUserType(), requestDto.getSearchKeyword(), requestDto.getSortOrder(),
				requestDto.getAchievedMin(), requestDto.getAchievedMax(), requestDto.getZone(), requestDto.getState(),
				requestDto.getCity(), requestDto.getPageIndex(), requestDto.getPageSize());

		log.info("Data fetched from repository: {} records", resultList.size());

		Map<Integer, List<LeaderboardResponseDto>> groupedByRank = new TreeMap<>();

		for (LeaderboardEntity entity : resultList) {
			LeaderboardResponseDto dto = new LeaderboardResponseDto(entity.getSrno(), entity.getFullName(),
					entity.getLoginId(), entity.getTarget(), entity.getAchieved(), entity.getDifferenceTarget(),
					entity.getIncentiveAmount(), entity.getConsistencyAmount(), entity.getReward(),
					entity.getLocation(), entity.getProfileLocation(), entity.getReportsTo(), entity.getNationalHead(),
					entity.getZonalHead(), entity.getStateHead(), entity.getChannelManager(),
					getProfilePicture(entity.getLoginId()));

			groupedByRank.computeIfAbsent(entity.getAllIndiaRank(), k -> new ArrayList<>()).add(dto);
		}

		Map<Integer, List<LeaderboardResponseDto>> sortedGroupedByRank = "1".equals(requestDto.getSortOrder())
				? new TreeMap<>(groupedByRank)
				: groupedByRank.entrySet().stream()
						.sorted(Map.Entry.<Integer, List<LeaderboardResponseDto>>comparingByKey().reversed())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap::new));

		List<FilterdataList> filterData = sortedGroupedByRank.entrySet().stream()
				.map(entry -> new FilterdataList(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		return new PortalLeaderBoardRespCountDto(filterData);
	}

	public LeaderBoardRespCountDto getLeaderboardData(HttpServletRequest httpRequest,
			LeaderboardRequestDto requestDto) {
		log.info("Fetching leaderboard data for month: {}", requestDto.getMonthYear());

		String originalSortOrder = requestDto.getSortOrder();

// Fetch total count
		List<LeaderboardEntity> countList = repository.getLeaderboardcountData(requestDto.getMonthYear(),
				requestDto.getUserType(), requestDto.getSearchKeyword(), originalSortOrder, requestDto.getAchievedMin(),
				requestDto.getAchievedMax(), requestDto.getZone(), requestDto.getState(), requestDto.getCity());
		int totalCount = countList.size();
		log.info("Total count of data is: {}", totalCount);

// Fetch paginated leaderboard data
		List<LeaderboardEntity> resultList = repository.getLeaderboardData(requestDto.getMonthYear(),
				requestDto.getUserType(), requestDto.getSearchKeyword(), originalSortOrder, requestDto.getAchievedMin(),
				requestDto.getAchievedMax(), requestDto.getZone(), requestDto.getState(), requestDto.getCity(),
				requestDto.getPageIndex(), requestDto.getPageSize());
		log.info("Fetched {} records from repository", resultList.size());

// Group by All India Rank
		Map<Integer, List<LeaderboardResponseDto>> groupedByRank = new TreeMap<>();
		for (LeaderboardEntity entity : resultList) {
			LeaderboardResponseDto dto = new LeaderboardResponseDto(entity.getSrno(), entity.getFullName(),
					entity.getLoginId(), entity.getTarget(), entity.getAchieved(), entity.getDifferenceTarget(),
					entity.getIncentiveAmount(), entity.getConsistencyAmount(), entity.getReward(),
					entity.getLocation(), entity.getProfileLocation(), entity.getReportsTo(), entity.getNationalHead(),
					entity.getZonalHead(), entity.getStateHead(), entity.getChannelManager(),
					getProfilePicture(entity.getLoginId()));
			groupedByRank.computeIfAbsent(entity.getAllIndiaRank(), k -> new ArrayList<>()).add(dto);
		}

// Sort based on original input
		Map<Integer, List<LeaderboardResponseDto>> sortedGroupedByRank = "1".equals(originalSortOrder)
				? new TreeMap<>(groupedByRank)
				: groupedByRank.entrySet().stream()
						.sorted(Map.Entry.<Integer, List<LeaderboardResponseDto>>comparingByKey().reversed())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap::new));

// Convert to response format
		List<FilterdataList> filterData = sortedGroupedByRank.entrySet().stream()
				.map(entry -> new FilterdataList(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		return new LeaderBoardRespCountDto(totalCount, filterData);
	}

	private String getProfilePicture(String username) {

		String profilepictureUrl = String.format(profilePictureUrl, username);
		String url = "";
		try {
			url = profilepictureUrl;
			log.info(url);
			return url;
		} catch (NullPointerException npe) {
			url = "";
			return npe.getMessage();
		}

	}
}
