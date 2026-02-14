package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.LeaderBoardIncentiveDownloadEntity;

@Repository
public interface LeaderBoardIncentiveDownloadRepo extends JpaRepository<LeaderBoardIncentiveDownloadEntity, Long> {
	
	@Query(value = "EXEC [USP_GetLeaderboardData_Download] @MonthYear = :monthYear, @UserType = :userType", nativeQuery = true)
	List<LeaderBoardIncentiveDownloadEntity> getCmIncentiveMonthlyData(
	    @Param("monthYear") String monthYear, 
	    @Param("userType") String userType 
	);

}
