package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.BankWiseMTDEntity;


public interface BankWiseMTDRepository extends JpaRepository<BankWiseMTDEntity, String> {
	  @Query(value = "EXEC ops_cm_bank_wise_mtd_uptime_avg :cm_user_id , :bank_name", nativeQuery = true)
	  BankWiseMTDEntity getAllBankWiseMTDForCmUser(@Param("cm_user_id") String cmUserId, @Param("bank_name") String bankName);
}

