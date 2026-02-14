package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.CeWiseAtmSearchDetails;

public interface CeWiseAtmSearchDetailsRepository extends JpaRepository<CeWiseAtmSearchDetails, String> {

	@Query(value = "EXEC get_index_of_ce_atm  " + "@user_id = :userId, " + "@sort_option = :sortOption, "
			+ "@bank = :bank, " + "@grade = :grade, " + "@status = :status, "
			+ "@uptime_status = :uptimeStatus", nativeQuery = true)
	List<CeWiseAtmSearchDetails> getCeWiseAtmSearchData(@Param("userId") String userId,
			@Param("sortOption") String sortOption, @Param("bank") String bank, @Param("grade") String grade,
			@Param("status") String status, @Param("uptimeStatus") String uptimeStatus);
	
	
}
