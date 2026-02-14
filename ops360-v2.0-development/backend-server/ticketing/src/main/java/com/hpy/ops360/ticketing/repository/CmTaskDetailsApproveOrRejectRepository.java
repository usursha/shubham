package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.service.CMTaskDetailsApproveOrRejectTicketByKafka;

@Repository
public interface CmTaskDetailsApproveOrRejectRepository
		extends JpaRepository<CMTaskDetailsApproveOrRejectTicketByKafka, Long> {

	@Query(value = "EXEC USP_GetTicketsNumberDetails @user_id = :userId,@ticket_number = :ticket_number,@atm_id = :atm_id", nativeQuery = true)
	CMTaskDetailsApproveOrRejectTicketByKafka getTicketsNumberDetailsWithSource(@Param("userId") String userId,
			@Param("ticket_number") String ticketNumber, @Param("atm_id") String atmId);

}
