package com.hpy.mappingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.StateMaster;
import com.hpy.mappingservice.entity.ZoneMaster;

public interface StateMasterRepository extends JpaRepository<StateMaster, Long> {
    // Additional query methods can be defined here
	Optional<StateMaster> findByStateNameAndZoneMaster(String stateName, ZoneMaster zoneMaster);
	
	@Query("SELECT s.id FROM StateMaster s WHERE s.stateName = :stateName AND s.zoneMaster.id = :zoneId")
	Optional<Long> findStateIdByName(@Param("stateName") String stateName, @Param("zoneId") Long zoneId);
}