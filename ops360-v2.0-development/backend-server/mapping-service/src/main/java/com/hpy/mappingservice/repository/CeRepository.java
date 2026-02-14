package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CeEntity;

@Repository
public interface CeRepository extends JpaRepository<CeEntity, Long> {
	
	@Query(value = "EXEC usp_find_ce_user :ce_user_ids ",nativeQuery = true)
	public CeEntity findCeById(@Param("ce_user_ids") String ceUsernames);
}
