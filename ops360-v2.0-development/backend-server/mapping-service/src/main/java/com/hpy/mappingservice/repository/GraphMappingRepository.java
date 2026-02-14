package com.hpy.mappingservice.repository;

import com.hpy.mappingservice.entity.GraphMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GraphMappingRepository extends JpaRepository<GraphMappingEntity, Integer> {

    @Query(value = "EXEC GetGraphMappingsByManager :managerUsername", nativeQuery = true)
    List<GraphMappingEntity> getGraphMappingsByManager(@Param("managerUsername") String managerUsername);
}
