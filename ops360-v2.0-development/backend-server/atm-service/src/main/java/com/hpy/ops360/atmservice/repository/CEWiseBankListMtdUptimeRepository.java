package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.CEWiseBankListMtdUptimeEntity;

	@Repository
	public interface CEWiseBankListMtdUptimeRepository extends JpaRepository<CEWiseBankListMtdUptimeEntity, Long>{
		@Query(value ="EXEC ops_ce_bank_list_with_mtd_uptime :ce_user_id ", nativeQuery = true)
	    List<CEWiseBankListMtdUptimeEntity> getBankListDirect(@Param("ce_user_id") String ceUserId);
	    
	}
