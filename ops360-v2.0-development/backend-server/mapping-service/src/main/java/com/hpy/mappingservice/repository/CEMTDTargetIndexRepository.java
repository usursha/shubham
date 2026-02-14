package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CEMTDTargetIndexEntity;



@Repository
public interface CEMTDTargetIndexRepository extends JpaRepository<CEMTDTargetIndexEntity, Long>{

	@Query(value = "EXEC ops_CEIndex_mtdtarget :cm_user_id", nativeQuery = true)
	public List<CEMTDTargetIndexEntity> getCEMTDTargetIndexDetails(@Param("cm_user_id") String cmUserId);

}
