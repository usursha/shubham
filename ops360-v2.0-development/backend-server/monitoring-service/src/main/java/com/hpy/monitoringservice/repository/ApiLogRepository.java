package com.hpy.monitoringservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.ApiLog;


@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
	
}