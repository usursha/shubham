package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.RcmZonalHeadEntity;

@Repository
public interface ZonelHeadRepository extends JpaRepository<RcmZonalHeadEntity, Long> {
	
	@Query(value = "EXEC usp_find_rcm_user :rcm_user_ids",nativeQuery = true)
	public RcmZonalHeadEntity findRcmById(@Param("rcm_user_ids") String rcmUsernames);
}
