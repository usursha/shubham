package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpy.ops360.ticketing.entity.TicketFlagStatus;

public interface TicketFlagStatusRepository extends JpaRepository<TicketFlagStatus, String> {

	 @Query(value = "EXEC Get_Ticket_flag_status :ceUserId", nativeQuery = true)
	List<TicketFlagStatus> getTicketFlagStatusByCeUserId(String ceUserId);

	

	    @Query(value = "EXEC USP_InsertUpdateTicket_flag_status :ceUserId, :ticketNumber, :flagStatus", nativeQuery = true)
	    Integer insertUpdateFlagStatus(
	        @Param("ceUserId") String ceUserId,
	        @Param("ticketNumber") String ticketNumber,
	        @Param("flagStatus") int flagStatus
	    );
}
