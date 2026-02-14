package com.hpy.monitoringservice.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.UserAtmDetails;

@Repository
public interface UserAtmDetailsRepository extends JpaRepository<UserAtmDetails, Long> {

	@Cacheable(value = "OPS_GetAtmDetailFromUser", key = "#user_login_id + '-user_login_id'", unless = "#result == null || #result.isEmpty()")
	@Query(value = "EXEC OPS_GetAtmDetailFromUser :user_login_id", nativeQuery = true)
	List<UserAtmDetails> getUserAtmDetails(@Param("user_login_id") String userId);
}
