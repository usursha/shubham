package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.sampatti.entity.TeamLeaderboardRankDetails;

public interface TeamLeaderboardRankDetailsRepostiory extends JpaRepository<TeamLeaderboardRankDetails, Long> {
	
	
	@Query(value="EXEC usp_team_leaderboard_rank :user_id,:paramMonthYear,:sort_type,:target_achieved_min,:target_achieved_max",nativeQuery = true)
	public List<TeamLeaderboardRankDetails> getTeamLeaderboardRankDetails(@Param("user_id") String username, @Param("paramMonthYear") String paramMonthYear,@Param("sort_type") Integer sortTypeId,@Param("target_achieved_min") Integer targetAchievedMin,@Param("target_achieved_max") Integer targetAchievedMax);
}
