package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.CeTaskDetails;

public interface CeTaskDetailsRepository extends JpaRepository<CeTaskDetails, Long> {
	
	@Query(value = "EXEC USP_GetCeTaskDetails @user_id = :userId,@ticket_number = :ticket_number,@atm_id = :atm_id", nativeQuery = true)
	CeTaskDetails getCeTaskDetails(@Param("userId") String userId, @Param("ticket_number") String ticketNumber,
			@Param("atm_id") String atmId);

}
