package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.IndexOfUpDawnAtmForCm;

public interface IndexOfUpDawnAtmForCmRepository extends JpaRepository<IndexOfUpDawnAtmForCm, String> {

	@Query(value = "EXEC Usp_get_ATM_List_for_CM :cm_user_id", nativeQuery = true)
	List<IndexOfUpDawnAtmForCm> getIndexOfUpDawnAtmForCm(@Param("cm_user_id") String cmUserId);
	
	
}
