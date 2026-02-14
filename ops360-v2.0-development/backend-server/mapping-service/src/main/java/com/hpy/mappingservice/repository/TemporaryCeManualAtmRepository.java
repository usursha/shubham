package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.TemporaryCeManualAtmEntity;
@Repository
public interface TemporaryCeManualAtmRepository extends JpaRepository<TemporaryCeManualAtmEntity, Long> {
			@Query(value ="EXEC primary_Ce_user_atm_details :ce_user", nativeQuery = true)
		    List<TemporaryCeManualAtmEntity> getCeMappingDateRange(@Param("ce_user") String ce_user);
		    
		}


