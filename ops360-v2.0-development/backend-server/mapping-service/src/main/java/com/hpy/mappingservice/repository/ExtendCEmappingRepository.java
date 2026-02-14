package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.request.dto.ExtendCEmapping;

@Repository
public interface ExtendCEmappingRepository extends JpaRepository<ExtendCEmapping, Long> {
}