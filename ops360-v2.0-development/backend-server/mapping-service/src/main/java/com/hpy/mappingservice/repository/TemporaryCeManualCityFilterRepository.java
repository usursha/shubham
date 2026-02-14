package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.TemporaryCeManualCityFilterEntity;
	
	@Repository
	public interface TemporaryCeManualCityFilterRepository extends JpaRepository<TemporaryCeManualCityFilterEntity, Long> {

	    @Query(value = "EXEC primary_ce_user_city :ce_id", nativeQuery = true)
	    List<TemporaryCeManualCityFilterEntity> getCECityList(@Param("ce_id") String ce_id);
	}

