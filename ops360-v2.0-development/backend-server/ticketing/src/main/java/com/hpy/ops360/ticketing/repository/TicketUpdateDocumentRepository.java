package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.TicketUpdateDocument;

@Repository
public interface TicketUpdateDocumentRepository extends JpaRepository<TicketUpdateDocument, Long> {

	@Query(value = "EXEC Usp_get_Ticket_Update_Documents :username, :atm_id, :ticket_no", nativeQuery = true)
	TicketUpdateDocument getTicketUpdateDocuments(@Param("username") String username, @Param("atm_id") String atmId,
			@Param("ticket_no") String ticketNumber);

}
