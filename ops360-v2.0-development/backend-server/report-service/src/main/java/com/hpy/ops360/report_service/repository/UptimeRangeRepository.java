package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.UptimeRange;

@Repository
public interface UptimeRangeRepository extends JpaRepository<UptimeRange, Integer> {

    List<UptimeRange> findAll();
}
