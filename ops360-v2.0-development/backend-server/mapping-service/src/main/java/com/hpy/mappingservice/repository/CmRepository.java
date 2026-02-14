package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CmEntity;

@Repository
public interface CmRepository extends JpaRepository<CmEntity, Long> {
	
	@Query(value = "EXEC usp_find_cm_user :cm_user_ids ",nativeQuery = true)
	public CmEntity findCmById(@Param("cm_user_ids") String cmUsernames);
}
