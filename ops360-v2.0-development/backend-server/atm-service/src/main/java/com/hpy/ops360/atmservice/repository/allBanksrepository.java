package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.allBanksentity;


public interface allBanksrepository extends JpaRepository<allBanksentity, String> {
	  @Query(value = "EXEC ops_cm_wise_bank_list :cm_user_id", nativeQuery = true)
	  List<allBanksentity> getAllBanksListForCmUser(@Param("cm_user_id") String cmUserId);
}
