package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.TicketDetailsView;

public interface TicketDetailsViewHimsRepository extends JpaRepository<TicketDetailsView, Long>{
	
	@Query(value="EXEC usp_get_ticket_details_hims :ticket_no,:atm_id",nativeQuery = true)
	public TicketDetailsView getTicketDetailsFromHims(@Param("ticket_no") String ticketno,@Param("atm_id") String atmId); 
}
