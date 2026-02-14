package com.hpy.ops360.sampatti.repository;

import com.hpy.ops360.sampatti.entity.HierarchyStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HierarchyStatsRepository extends JpaRepository<HierarchyStatsEntity, Long> {

    @Query(value = "EXEC USP_GetCountHierarchyStats :username", nativeQuery = true)
    HierarchyStatsEntity getHierarchyStats(@Param("username") String username);
}
