package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.entity.ManageUpcomingLeavesBankFilterEntity;

	public interface ManageUpcomingLeavesCeBankFilterRepository extends JpaRepository<ManageUpcomingLeavesBankFilterEntity, Long> {

	    @Query(value = "EXEC primary_ce_user_Bank_TempMappedOnly :ce_user", nativeQuery = true)
	    List<ManageUpcomingLeavesBankFilterEntity> getCEBankList(@Param("ce_user") String ceUserId);
	}
