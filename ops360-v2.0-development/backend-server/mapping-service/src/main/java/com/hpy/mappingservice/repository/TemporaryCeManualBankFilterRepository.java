package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.TemporaryCeManualBankFilterEntity;
	
	@Repository
	public interface TemporaryCeManualBankFilterRepository extends JpaRepository<TemporaryCeManualBankFilterEntity, Long> {

	    @Query(value = "EXEC primary_ce_user_Bank :ce_id", nativeQuery = true)
	    List<TemporaryCeManualBankFilterEntity> getCEBankList(@Param("ce_id") String ce_id);
	}


