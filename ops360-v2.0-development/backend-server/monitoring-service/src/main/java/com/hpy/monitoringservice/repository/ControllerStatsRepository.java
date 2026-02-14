package com.hpy.monitoringservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.ControllerStats;

@Repository
public interface ControllerStatsRepository extends JpaRepository<ControllerStats, Long> {
    @Query(value = "EXEC GetControllerStatsGroupedByUser :controllerName, :fromDate, :toDate", nativeQuery = true)
    List<ControllerStats> findStatsByControllerName(@Param("controllerName") String controllerName, @Param("fromDate") String fromDate, @Param("toDate") String toDate);
}