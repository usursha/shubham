package com.hpy.mappingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.CityMaster;
import com.hpy.mappingservice.entity.StateMaster;

public interface CityMasterRepository extends JpaRepository<CityMaster, Long> {
    // Additional query methods can be defined here
	Optional<CityMaster> findByCityNameAndStateMaster(String cityName, StateMaster stateMaster);
	
	@Query("SELECT c.id FROM CityMaster c WHERE c.cityName = :cityName AND c.stateMaster.id = :stateId")
	Optional<Long> findCityIdByName(@Param("cityName") String cityName, @Param("stateId") Long stateId);
}
