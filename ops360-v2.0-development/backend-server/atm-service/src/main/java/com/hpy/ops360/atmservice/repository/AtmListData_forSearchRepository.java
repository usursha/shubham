package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.AtmListData_forSearch;

public interface AtmListData_forSearchRepository extends JpaRepository<AtmListData_forSearch, String> {

		@Query(value = "EXEC USP_GetIndexOf_All_ATM_Pagenation_Filtered :user_id, :bank, :grade, :status, :uptime_status", nativeQuery = true)
	    List<AtmListData_forSearch> getAllAtmAgainstFilter(
	            @Param("user_id") String userId,
	            @Param("bank") String banks, 
	            @Param("grade") String grades, 
	            @Param("status") String statuses, 
	            @Param("uptime_status") String uptimeStatuses 
	    );
}
