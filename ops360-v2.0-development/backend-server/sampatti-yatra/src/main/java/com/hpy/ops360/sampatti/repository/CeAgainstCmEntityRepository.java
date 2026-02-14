package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.sampatti.entity.CeAgainstCmEntity;

public interface CeAgainstCmEntityRepository extends JpaRepository<CeAgainstCmEntity, Long>{
	
	@Query(value="EXEC USP_TeamLeaderboardCEagainst_CM :cm_user_id",nativeQuery = true)
	public List<CeAgainstCmEntity> getCeAgainstCmList(@Param("cm_user_id") String cmUserId);

}
