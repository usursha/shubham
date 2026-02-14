package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.CeWiseAtmDetailsPagnated;

public interface CeWiseAtmDetailsPagnatedRepository extends JpaRepository<CeWiseAtmDetailsPagnated, String> {

	@Query(value = "EXEC get_index_of_ce_atm_with_pagination " + "@user_id = :userId, " + "@page_number = :pageNumber, "
			+ "@page_size = :pageSize, " + "@sort_option = :sortOption, " + "@bank = :bank, " + "@grade = :grade, "
			+ "@status = :status, " + "@uptime_status = :uptimeStatus, "
			+ "@search_text = :searchText", nativeQuery = true)
	List<CeWiseAtmDetailsPagnated> getCeWiseAtmDetailsData(@Param("userId") String userId,
			@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize,
			@Param("sortOption") String sortOption, @Param("bank") String bank, @Param("grade") String grade,
			@Param("status") String status, @Param("uptimeStatus") String uptimeStatus,
			@Param("searchText") String searchText);
	
	
}
