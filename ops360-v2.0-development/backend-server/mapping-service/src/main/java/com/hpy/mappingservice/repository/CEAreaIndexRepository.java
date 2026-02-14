package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CEAreaIndexEntity;


@Repository
public interface CEAreaIndexRepository extends JpaRepository<CEAreaIndexEntity, Long> {
   @Query(value = "EXEC ops_CEIndex_area :cm_user_id", nativeQuery = true)
	public List<CEAreaIndexEntity> getCEAreaIndexDetails(@Param("cm_user_id") String cmUserId);
}
