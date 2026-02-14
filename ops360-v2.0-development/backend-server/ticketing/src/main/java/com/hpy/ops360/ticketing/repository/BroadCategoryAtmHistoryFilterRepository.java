package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.BroadCategoryEntityForAtmHistoryFilter;

public interface BroadCategoryAtmHistoryFilterRepository
		extends JpaRepository<BroadCategoryEntityForAtmHistoryFilter, Long> {

	@Query(value = "EXEC dbo.Usp_Get_Broad_Category_filter :username", nativeQuery = true)
	List<Object[]> getBroadCategoryByUsername(@Param("username") String username);

	
}
