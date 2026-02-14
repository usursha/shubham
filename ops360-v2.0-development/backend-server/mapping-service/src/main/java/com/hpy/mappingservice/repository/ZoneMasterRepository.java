package com.hpy.mappingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.ZoneMaster;

public interface ZoneMasterRepository extends JpaRepository<ZoneMaster, Long> {
    // Additional query methods can be defined here
	Optional<ZoneMaster> findByZoneName(String zoneName);
	
	@Query("SELECT z.id FROM ZoneMaster z WHERE z.zoneName = :zoneName")
	Optional<Long> findZoneIdByName(@Param("zoneName") String zoneName);
}
