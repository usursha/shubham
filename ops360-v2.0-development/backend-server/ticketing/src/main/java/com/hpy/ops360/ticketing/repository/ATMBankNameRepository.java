package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.ATMBankName;

public interface ATMBankNameRepository extends JpaRepository<ATMBankName, Long> {

	@Query(value = "EXEC USP_GetATMBankName :atm_id, :ticket_number", nativeQuery = true)
	public ATMBankName getATMBankName(@Param("atm_id") String atmId, @Param("ticket_number") String ticketNumber);

}
