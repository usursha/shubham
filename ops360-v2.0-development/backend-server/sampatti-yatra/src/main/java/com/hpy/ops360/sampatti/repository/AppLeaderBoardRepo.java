package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.hpy.ops360.sampatti.entity.AppLeaderBoardEntity;

@Repository
public interface AppLeaderBoardRepo extends JpaRepository<AppLeaderBoardEntity, Long>{

	@Query(value = "EXEC USP_AppGetLeaderboardData " +
          "@MonthYear = :monthYear, " +
          "@UserType = :userType, " +
          "@SearchKeyword = :searchKeyword, " +
          "@SortOrder = :sortOrder, " +
          "@ZoneList = :zoneList, " +
          "@StateList = :stateList, " +
          "@CityList = :cityList, " +
          "@PageIndex = :pageIndex, " +
          "@PageSize = :pageSize",
          nativeQuery = true)
  List<AppLeaderBoardEntity> getLeaderboardData(
          @Param("monthYear") String monthYear,
          @Param("userType") String userType,
          @Param("searchKeyword") String searchKeyword,
          @Param("sortOrder") String sortOrder,
          @Param("zoneList") String zoneList,
          @Param("stateList") String stateList,
          @Param("cityList") String cityList,
          @Param("pageIndex") int pageIndex,
          @Param("pageSize") int pageSize
  );
	
	@Query(value = "EXEC USP_AppGetLeaderboardDataCount " +
	          "@MonthYear = :monthYear, " +
	          "@UserType = :userType, " +
	          "@SearchKeyword = :searchKeyword, " +
	          "@SortOrder = :sortOrder, " +
	          "@ZoneList = :zoneList, " +
	          "@StateList = :stateList, " +
	          "@CityList = :cityList", 
	          nativeQuery = true)
	  Long getLeaderboardDataCount(
	          @Param("monthYear") String monthYear,
	          @Param("userType") String userType,
	          @Param("searchKeyword") String searchKeyword,
	          @Param("sortOrder") String sortOrder,
	          @Param("zoneList") String zoneList,
	          @Param("stateList") String stateList,
	          @Param("cityList") String cityList
	  );

}
