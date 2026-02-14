package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.AppScoreCardEntity;

@Repository
public interface AppScoreCardRepo extends JpaRepository<AppScoreCardEntity, Long> {
	
	@Query(value = "EXEC GetCEUserFinancialData @user_id = :userId", nativeQuery = true)
	List<AppScoreCardEntity> getCEScoreCardData(@Param("userId") String userId);

}
