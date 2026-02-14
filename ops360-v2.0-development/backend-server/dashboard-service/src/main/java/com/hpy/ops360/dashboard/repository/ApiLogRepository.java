package com.hpy.ops360.dashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.ApiLog;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}