package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hpy.mappingservice.entity.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
	
	List<LeaveRequest> findByUserManagerIdAndStatus(Long managerId, String status);
}