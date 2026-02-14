package com.hpy.monitoringservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.ApiLogSummary;

import java.util.List;

@Repository
public interface ApiSummaryRepository extends JpaRepository<ApiLogSummary, Long> {

    @Query(value = "EXEC [dbo].[GetControllerStatsWithDetails] :intervalMinutes, :limitRows", nativeQuery = true)
    List<ApiLogSummary> getControllerStatsByInterval(@Param("intervalMinutes") int intervalMinutes, @Param("limitRows") int limitRows);
}