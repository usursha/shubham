package com.hpy.ops360.sampatti.repository;

import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.FilteredItemEntity;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FilterUserMasterRepository extends JpaRepository<FilteredItemEntity, Long> {

    @Query(value = "EXEC USP_Filter_UserMasterNEWCount :zoneList, :stateList, :cityList, :designation", nativeQuery = true)
    List<FilteredItemEntity> getFilteredUserMaster(@Param("zoneList") String zoneList,
                                         @Param("stateList") String stateList,
                                         @Param("cityList") String cityList,
    									 @Param("designation") String designation);
}
