package com.hpy.ops360.sampatti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hpy.ops360.sampatti.dto.response.TeamLeaderBoardListDataDto;
import com.hpy.ops360.sampatti.dto.response.TeamLeaderboardDetailsDto;
import com.hpy.ops360.sampatti.dto.response.TeamLeaderboardFilterDataDto;
import com.hpy.ops360.sampatti.entity.TeamLeaderboardRankDetails;
import com.hpy.ops360.sampatti.repository.CeAgainstCmEntityRepository;
import com.hpy.ops360.sampatti.repository.SortFilterDataRepostiory;
import com.hpy.ops360.sampatti.repository.TargetRangeRepository;
import com.hpy.ops360.sampatti.repository.TeamLeaderboardRankDetailsRepostiory;
import com.hpy.ops360.sampatti.repository.UserIncentiveMonthYearRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TeamLeaderBoardService {
	
	private UserIncentiveMonthYearRepository repository;
	
	private CeAgainstCmEntityRepository ceAgainstCmEntityRepository;
	
	private SortFilterDataRepostiory sortFilterDataRepostiory;
	
	private TargetRangeRepository targetRangeRepository;
	
	private TeamLeaderboardRankDetailsRepostiory teamLeaderboardRankDetailsRepostiory;
	
	@Value("${profile-picture.url}")
	private String profilePictureUrl;
	
	
	public TeamLeaderBoardService(UserIncentiveMonthYearRepository repository,
			CeAgainstCmEntityRepository ceAgainstCmEntityRepository, SortFilterDataRepostiory sortFilterDataRepostiory,
			TargetRangeRepository targetRangeRepository,TeamLeaderboardRankDetailsRepostiory teamLeaderboardRankDetailsRepostiory) {
		super();
		this.repository = repository;
		this.ceAgainstCmEntityRepository = ceAgainstCmEntityRepository;
		this.sortFilterDataRepostiory = sortFilterDataRepostiory;
		this.targetRangeRepository = targetRangeRepository;
		this.teamLeaderboardRankDetailsRepostiory = teamLeaderboardRankDetailsRepostiory;
	}

	public TeamLeaderBoardListDataDto getTeamLeaderBoardListData(String cmUsername)
	{	
		log.info("calling TeamLeaderBoardService | getTeamLeaderBoardListData()");
		
		return new TeamLeaderBoardListDataDto(repository.getUserIncentivesMonthYear(),ceAgainstCmEntityRepository.getCeAgainstCmList(cmUsername));
	}
	
	public TeamLeaderboardFilterDataDto getTeamLeaderboardFilterData()
	{
		log.info("calling TeamLeaderBoardService | getTeamLeaderboardFilterData()");
		return new TeamLeaderboardFilterDataDto(sortFilterDataRepostiory.findAll(),targetRangeRepository.findById(1).get());
	}
	
	public List<TeamLeaderboardDetailsDto> getTeamLeaderboardDetails(String username, String paramMonthYear, Integer sortTypeId, Integer targetAchievedMin, Integer targetAchievedMax)
	{
		List<TeamLeaderboardRankDetails> list=teamLeaderboardRankDetailsRepostiory.getTeamLeaderboardRankDetails(username,paramMonthYear,sortTypeId,targetAchievedMin,targetAchievedMax);
		return list.stream().map(entity -> {
		    String profilePic = getProfilePicture(entity.getUsername());
		    return TeamLeaderboardDetailsDto.fromEntity(entity, profilePic);
		}).toList();
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
