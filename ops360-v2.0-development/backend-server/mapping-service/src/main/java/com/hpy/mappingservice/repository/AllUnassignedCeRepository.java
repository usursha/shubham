package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AllUnassignedCe;

@Repository
public interface AllUnassignedCeRepository extends JpaRepository<AllUnassignedCe, Long> {
	
	@Query(value="EXEC usp_cm_all_unassigned_list",nativeQuery = true)
	public List<AllUnassignedCe> getAllUnassignedCe();

}
