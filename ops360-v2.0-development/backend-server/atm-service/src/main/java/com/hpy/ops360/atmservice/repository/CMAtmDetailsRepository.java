package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.CMAtmDetails;


@Repository
public interface CMAtmDetailsRepository extends JpaRepository<CMAtmDetails, Long> {

	@Query(value = "EXEC USP_GetCMAtmDetails @user_id = :user_id", nativeQuery = true)
	List<CMAtmDetails> getCMAtmDetails(@Param("user_id") String userId);
}