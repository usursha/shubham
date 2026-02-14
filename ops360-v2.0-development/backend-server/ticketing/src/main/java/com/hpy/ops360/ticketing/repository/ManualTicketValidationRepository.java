package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.ManualTicketValidation;

@Repository
public interface ManualTicketValidationRepository extends JpaRepository<ManualTicketValidation, Long> {
	
	@Query(value="EXEC USP_GetManualTicketsValidation :atm_id,:reason",nativeQuery = true)
	ManualTicketValidation validateManualTicket(@Param("atm_id") String atmId,@Param("reason") String manualTicketReason);

}
