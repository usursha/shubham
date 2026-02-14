package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.ExitedCeMappingEntity;


@Repository
public interface ExitedCeMappingRepository extends JpaRepository<ExitedCeMappingEntity, Long> {

	@Query(value = "EXEC initiate_ce_mapping :cm_user_id ", nativeQuery = true) 
	public List<ExitedCeMappingEntity> getFindByCeExitedNameContaining(@Param("cm_user_id") String cmUserId); 
	

	@Query(value = "EXEC usp_primary_ce_details :ceUserId ", nativeQuery = true) 
	public ExitedCeMappingEntity getPrimaryCeDetailsByCeId(@Param("ceUserId") String ceUserId); 
	
}
