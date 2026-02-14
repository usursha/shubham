package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.dashboard.entity.ATMIndexDetails;

//public interface ATMIndexDetailsRepository extends JpaRepository<ATMIndexDetails, Long> {
//
//	@Query(value = "EXEC USP_GetATMIndexDetails :atmId", nativeQuery = true)
//	public ATMIndexDetails getATMIndexDetails(@Param("atmId") String atmId);
//}



public interface ATMIndexDetailsRepository extends JpaRepository<ATMIndexDetails, Long> {

    @Query(value = "EXEC USP_GetATMIndexDetails_New :atmId", nativeQuery = true)
    public ATMIndexDetails getATMIndexDetails(@Param("atmId") String atmId);
}
