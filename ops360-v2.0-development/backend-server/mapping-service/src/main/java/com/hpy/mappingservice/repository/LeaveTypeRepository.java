package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hpy.mappingservice.entity.LeaveType;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {
    List<LeaveType> findByIsActiveTrue();
}