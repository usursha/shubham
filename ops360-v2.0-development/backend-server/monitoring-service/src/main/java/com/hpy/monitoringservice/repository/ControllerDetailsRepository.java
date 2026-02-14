package com.hpy.monitoringservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.ControllerDetails;

@Repository
public interface ControllerDetailsRepository extends JpaRepository<ControllerDetails, Long> {
    @Query(value = "EXEC GetControllerDetailsByUser :controllerName, :userName, :fromDate, :toDate", nativeQuery = true)
    List<ControllerDetails> findDetailsByControllerNameAndUserName(@Param("controllerName") String controllerName, @Param("userName") String userName, @Param("fromDate") String fromDate, @Param("toDate") String toDate);
}