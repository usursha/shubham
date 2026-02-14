package com.hpy.ops360.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.BankNameAndAtmUpTimeEntity;


public interface BankNameAndAtmUpTimeRepository extends JpaRepository<BankNameAndAtmUpTimeEntity, Integer> {
    @Query(value = "EXEC ops_ce_atm_wise_bank_and_atm_uptime :atmid ", nativeQuery = true)
    BankNameAndAtmUpTimeEntity getBankNameAndAtmUpTime(@Param("atmid") String atmid);
}

