package com.hpy.mappingservice.automapping.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.automapping.entity.AutoAssignFilterEntity;

public interface AutoAssignFilterRepository extends JpaRepository<AutoAssignFilterEntity, String> {

	@Query(value = "EXEC USP_Get_Auto_Assign_filter :originalCEUserId", nativeQuery = true)
	List<Object[]> getAutoAssignFilterData(@Param("originalCEUserId") String originalCEUserId);

}
