package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.Ticket;

@Repository
public interface TicketRepository extends IGenericRepo<Ticket> {

	List<Ticket> findByStatusIgnoreCase(String status);

	@Query("SELECT count(t.ticketNumber) FROM Ticket t")
	Integer findMaxTicketNumber();
//
//	@Transactional
//	void deleteByTicketId(Long ticketId);
//	Ticket getTicket(Long ticketNumber);

//	 	@Query(value = "EXEC  USP_GetCMAgainstCEDetails @user_id = :user_id", nativeQuery = true)
//		List<CmSynopsis> callGetCMSynapsisList(@Param("user_id") String userId);

//	@Query(name = "EXEC USP_GetTicketsNumberOfCEList @user_id = :user_id",nativeQuery = true)
//    List<Ticket> getTicketsNumberOfCEList(@Param("user_id") String userId);
}
