package com.hpy.ops360.sampatti.repository;

import com.hpy.ops360.sampatti.entity.LeaderboardMonthEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface LeaderboardMonthRepository extends JpaRepository<LeaderboardMonthEntity, Long> {

	@Query(value = "EXEC SP_GetIncentiveMonthList ", nativeQuery = true)
	List<LeaderboardMonthEntity> getLeaderboardMonthData();
}
