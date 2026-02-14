package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AllAssignedAtms;

@Repository
public interface AllAssignedAtmsRepository extends JpaRepository<AllAssignedAtms, Long> {
	
	@Query(value="EXEC usp_ce_assigned_atm :ce_user_id",nativeQuery = true)
	public List<AllAssignedAtms> getAllAssignedAtms(@Param("ce_user_id") String userId);
}
