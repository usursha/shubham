package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hpy.mappingservice.entity.EventCode;

public interface EventCodeRepository extends JpaRepository<EventCode, Integer> {
}
