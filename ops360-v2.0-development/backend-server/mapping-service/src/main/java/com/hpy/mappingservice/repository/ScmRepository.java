package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.ScmEntity;

@Repository
public interface ScmRepository extends JpaRepository<ScmEntity, Long> {
	
	@Query(value = "EXEC usp_find_scm_user :scm_user_ids ",nativeQuery = true)
	public ScmEntity findScmById(@Param("scm_user_ids") String scmUsernames);
}
