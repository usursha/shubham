package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.LeaveRequestentity;

public interface LeaveRequestdataRepository extends JpaRepository<LeaveRequestentity, Long> {
	
	
	@Query(value = "SELECT * FROM leave_requests WHERE user_id IN (SELECT id FROM user_mapping WHERE manager_id = :managerId) AND status = :status", nativeQuery = true)
	List<LeaveRequestentity> findByManagerUsersAndStatus(@Param("managerId") Long managerId, @Param("status") String status);

}