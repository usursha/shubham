package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.CMAtmDetails;

@Repository
public interface CMAtmDetailsRepository extends JpaRepository<CMAtmDetails, Long> {

	@Query(value = "EXEC USP_GetCMAtmDetails @user_id = :user_id", nativeQuery = true)
	List<CMAtmDetails> getCMAtmDetails(@Param("user_id") String userId);
	
	@Query(value = "EXEC USP_GetIndexOf_All_ATM_Execl_CSV @user_id = :user_id", nativeQuery = true)
	List<CMAtmDetails> getCMAtmDetailsExcelAndCSV(@Param("user_id") String userId);
}