package com.hpy.mappingservice.repository;

import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.FilteredItemCEEntity;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FilterUserMasterCERepository extends JpaRepository<FilteredItemCEEntity, Long> {

    @Query(value = "EXEC index_of_ce_filter :loggedInUser", nativeQuery = true)
    List<FilteredItemCEEntity> getCEfilteredUserMaster(@Param("loggedInUser") String loggedInUser);
}
