package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.TemporaryMappingLeaveListEntity;

public interface TemporaryMappingLeaveListRepository extends JpaRepository<TemporaryMappingLeaveListEntity, String> {

	 @Query(value = "EXEC sp_GetUserLeaveDetails_ce_mapping :cmUserId", nativeQuery = true)
	    List<TemporaryMappingLeaveListEntity> getUserLeaveDetailsBySP(@Param("cmUserId") String cmUserId);
}
