package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.TicketClosure;

@Repository
public interface TicketClosureRepository extends JpaRepository<TicketClosure, Long> {
	
	@Query(value="EXEC usp_check_ticket_closure :ticket_number, :atm_id",nativeQuery = true)
	public TicketClosure checkTicketClosure(@Param("ticket_number") String ticketNumber,@Param("atm_id") String atmId);

}
