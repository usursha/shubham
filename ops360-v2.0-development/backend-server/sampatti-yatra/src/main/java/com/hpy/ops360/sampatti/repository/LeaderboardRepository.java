package com.hpy.ops360.sampatti.repository;

import com.hpy.ops360.sampatti.entity.LeaderboardEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntity, Long> {

	@Query(value = "EXEC USP_GetLeaderboardData :MonthYear, :UserType, :SearchKeyword, :SortOrder, :AchievedMin, :AchievedMax, :Zone, :State, :City, :PageIndex, :PageSize", nativeQuery = true)
	List<LeaderboardEntity> getLeaderboardData(@Param("MonthYear") String monthYear, @Param("UserType") String userType,
			@Param("SearchKeyword") String searchKeyword, @Param("SortOrder") String sortOrder,
			@Param("AchievedMin") Integer achievedMin, @Param("AchievedMax") Integer achievedMax,
			@Param("Zone") String zone, @Param("State") String state, @Param("City") String city,
			@Param("PageIndex") Integer pageIndex, @Param("PageSize") Integer pageSize);

	@Query(value = "EXEC USP_GetLeaderboardData :MonthYear, :UserType, :SearchKeyword, :SortOrder, :AchievedMin, :AchievedMax, :Zone, :State, :City", nativeQuery = true)
	List<LeaderboardEntity> getLeaderboardcountData(@Param("MonthYear") String monthYear,
			@Param("UserType") String userType, @Param("SearchKeyword") String searchKeyword,
			@Param("SortOrder") String sortOrder, @Param("AchievedMin") Integer achievedMin,
			@Param("AchievedMax") Integer achievedMax, @Param("Zone") String zone, @Param("State") String state,
			@Param("City") String city);
}
