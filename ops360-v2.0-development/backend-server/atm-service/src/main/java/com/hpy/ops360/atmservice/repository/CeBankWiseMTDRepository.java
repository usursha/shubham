package com.hpy.ops360.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hpy.ops360.atmservice.entity.CeBankWiseMTDEntity;
import org.springframework.data.repository.query.Param;

//import io.lettuce.core.dynamic.annotation.Param;


public interface CeBankWiseMTDRepository extends JpaRepository<CeBankWiseMTDEntity, String> {
	@Query(value = "EXEC ops_ce_bank_wise_mtd_uptime_avg :ce_user_id , :bank_name", nativeQuery = true)
	public CeBankWiseMTDEntity getAllCeBankWiseMTDForCeUser(@Param("ce_user_id") String ceUserId,
			@Param("bank_name") String bankName);
}
