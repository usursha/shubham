package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CESearchIndexEntity;


@Repository
public interface CESerachIndexRepository extends JpaRepository<CESearchIndexEntity, Long> {

	@Query(value = "EXEC ops_CEIndex_search :cm_user_id", nativeQuery = true)
	public List<CESearchIndexEntity> getCeSearchIndexDetails(@Param("cm_user_id") String cmUserId);

}
