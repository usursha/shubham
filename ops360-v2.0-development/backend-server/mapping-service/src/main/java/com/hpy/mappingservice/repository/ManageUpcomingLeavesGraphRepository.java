package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.ManageUpcomingLeavesGraphEntity;

public interface ManageUpcomingLeavesGraphRepository extends JpaRepository<ManageUpcomingLeavesGraphEntity, Integer> {

    @Query(value = "EXEC [GetTempCEMappingDetailsFiltered] :ManagerUsername", nativeQuery = true)
    List<ManageUpcomingLeavesGraphEntity> getGraphMappingsByManager(@Param("ManagerUsername") String cmID);
}


