package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.CeAssignedAtmEntity;

@Repository
public interface CeAssignedAtmRepository  extends JpaRepository<CeAssignedAtmEntity, Long>{

	@Query(value = "EXEC usp_ce_assigned_atm :ce_user_id", nativeQuery = true)
	public List<CeAssignedAtmEntity> getCeAssignedAtmList(@Param("ce_user_id") String ceUserId);
}
