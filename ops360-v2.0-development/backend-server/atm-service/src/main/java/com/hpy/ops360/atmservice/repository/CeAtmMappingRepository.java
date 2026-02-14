package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.CeAtmMappingEntity;

@Repository
public interface CeAtmMappingRepository extends JpaRepository<CeAtmMappingEntity, Long>{

	@Query(value = "EXEC usp_cm_ce_mapping_user :cm_user_id", nativeQuery = true)
	public List<CeAtmMappingEntity> getCeAtmMappingList(@Param("cm_user_id") String cmUserId);

	
}
