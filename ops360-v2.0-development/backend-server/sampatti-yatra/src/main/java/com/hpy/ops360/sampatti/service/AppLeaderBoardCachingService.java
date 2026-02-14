package com.hpy.ops360.sampatti.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.AppLeaderBoardDTO;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardRequestDto;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardResponseListDto;
import com.hpy.ops360.sampatti.dto.IResponseDtoImpl;
import com.hpy.ops360.sampatti.dto.RankChildrenDto;
import com.hpy.ops360.sampatti.dto.UserProfilePictureDto;
import com.hpy.ops360.sampatti.entity.AppLeaderBoardEntity;
import com.hpy.ops360.sampatti.repository.AppLeaderBoardRepo;
import com.hpy.ops360.sampatti.repository.AssetRepo;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;
import com.hpy.rest.exception.RequestMismatchExceptionDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppLeaderBoardCachingService {
	
	private String sortingOrder;
	
	@Value("${profile-picture.url}")
	private String profilePictureUrl;
	
	@Autowired
	private AppLeaderBoardRepo repo;
	
	@Cacheable(value = "leaderboardData", key = "#request.hashCode()")
	public AppLeaderBoardResponseListDto getCeLeaderBoardDataList(AppLeaderBoardRequestDto request) throws MalformedURLException, IOException {
		if(request.getSortOrder().equals("Rank (Low - High)")) { //low to high means rank will start from max rank to 0  
				sortingOrder="desc";
			}else if(request.getSortOrder().equals("Rank (High - Low)")) { //high to low means rank will start from 0 to max rank
				sortingOrder="asc";
			}else {
				throw new RequestMismatchExceptionDto();
			}
			AppLeaderBoardResponseListDto list=new AppLeaderBoardResponseListDto();
			String month=request.getMonthYear();
			String userType=request.getUserType();
			String keyword=request.getSearchKeyword();
			String order=sortingOrder;
			String zoneList=request.getZoneList();
			String stateList=request.getStateList();
			String cityList=request.getCityList();
			int pageIndex=request.getPageIndex();
			int pageSize=request.getPageSize();
			list.setCount(repo.getLeaderboardDataCount(month,userType,keyword,order,zoneList,stateList,cityList));
			List<AppLeaderBoardEntity> response=repo.getLeaderboardData(month,userType,keyword,order,zoneList,stateList,cityList,pageIndex,pageSize);
			Map<String, AppLeaderBoardDTO> groupedMap = new LinkedHashMap<>();

			for (AppLeaderBoardEntity entity : response) {
				RankChildrenDto children = new RankChildrenDto(
						entity.getSrno(),
				        entity.getFullName(),
				        entity.getTarget(),
				        entity.getAchieved(),
				        entity.getDifferenceTarget(),
				        entity.getIncentiveAmount(),
				        entity.getConsistencyAmount(),
				        entity.getReward(),
				        entity.getLocation(),
				        entity.getReportsTo(),
				        getProfilePicture(entity.getUsername())
				    );

				String groupKey = String.valueOf(entity.getAllIndiaRank());
			    if (!groupedMap.containsKey(groupKey)) {
			    	AppLeaderBoardDTO group = new AppLeaderBoardDTO();
			        group.setAllIndiaRank(entity.getAllIndiaRank());
			        group.setRankChildren(new ArrayList<>());
			        groupedMap.put(groupKey, group);
			    }
			    groupedMap.get(groupKey).getRankChildren().add(children);

			}
			// Final list of grouped leaderboard entries
			List<AppLeaderBoardDTO> transformedData = new ArrayList<>(groupedMap.values());
			list.setRecords(transformedData);
			return list;
	}

	private String getProfilePicture(String username) {
		String profilepictureUrl = String.format(profilePictureUrl, username);
		String url="";
        try {
        	url=profilepictureUrl;
        	log.info(url);
        	return url;
        }catch(NullPointerException npe) {
        	url="";
        	return npe.getMessage();
        }   
	}

}
