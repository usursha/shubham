package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AllUnassignedAtms;

@Repository
public interface AllUnassignedAtmsRepository extends JpaRepository<AllUnassignedAtms, Long> {
	
	@Query(value="EXEC usp_ce_all_unassigned_list",nativeQuery = true )
	public List<AllUnassignedAtms> getAllCeUnassignedAtmList();
}
