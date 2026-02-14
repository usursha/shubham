package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.ManageUpcomingLeavesCityFilterEntity;

public interface ManageUpcomingLeavesCeCityFilterRepository extends JpaRepository<ManageUpcomingLeavesCityFilterEntity, Long> {

    @Query(value = "EXEC primary_ce_user_city_TempMappedOnly :ce_user", nativeQuery = true)
    List<ManageUpcomingLeavesCityFilterEntity> getCECityList(@Param("ce_user") String ceUserId);
}

