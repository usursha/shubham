package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.CewiseallBanksEntity;
import com.hpy.ops360.atmservice.entity.allBanksentity;

public interface CewiseallBanksRepository extends JpaRepository<CewiseallBanksEntity, String> {
		  @Query(value = "exec ops_ce_wise_bank_list :ce_user_id", nativeQuery = true)
		  List<CewiseallBanksEntity> getallBanksListForCeUser(@Param("ce_user_id") String ceUserId);
}
