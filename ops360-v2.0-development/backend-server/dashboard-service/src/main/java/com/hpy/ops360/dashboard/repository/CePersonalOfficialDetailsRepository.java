package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.CEDetails;

@Repository
public interface CePersonalOfficialDetailsRepository extends JpaRepository<CEDetails, String> {

	@Query(value = "EXEC USP_GetPersonalOfficialDetails @user_id = :user_id", nativeQuery = true)
	CEDetails getCEDetails(@Param("user_id") String userId);
}
