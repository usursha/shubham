package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.ParentChildTicketNo;

@Repository
public interface ParentChildTicketNoRepository extends JpaRepository<ParentChildTicketNo, Long> {

	@Query(value = "EXEC Ust_get_parent_child_ticket_no :username,:atmid,:ticketid", nativeQuery = true)
	public List<ParentChildTicketNo> getParentChildTicketNo(@Param("username") String userId,
			@Param("atmid") String atmId, @Param("ticketid") String ticketNumber);
	
	@Query(value = "EXEC Ust_get_parent_child_ticket_no_hims :username,:atmid,:ticketid", nativeQuery = true)
	public List<ParentChildTicketNo> getParentChildTicketNoHims(@Param("username") String userId,
			@Param("atmid") String atmId, @Param("ticketid") String ticketNumber);

	
	@Query(value = "EXEC Ust_get_parent_child_ticket_no_Hims&Synergy :username,:atmid,:ticketid", nativeQuery = true)
	public List<ParentChildTicketNo> getParentChildTicketNoHimsOrSynergy(@Param("username") String userId,
			@Param("atmid") String atmId, @Param("ticketid") String ticketNumber);
	
	//added new for all_open_ticket_views and all_close_ticket_views 
	
	@Query(value = "SELECT source FROM all_open_ticket_views WHERE srno = :ticketId AND equipmentid = :atmId", nativeQuery = true)
	String getTicketSource(@Param("ticketId") String ticketId, @Param("atmId") String atmId);

	@Query(value = "SELECT source FROM atm_master WHERE atm_code = :atmId", nativeQuery = true)
	String getTicketSource(@Param("atmId") String atmId);
	
	@Query(value = "SELECT source FROM all_close_ticket_views WHERE ticket_number = :ticketId AND atm_id = :atmId", nativeQuery = true)
	String getClosedTicketSource(@Param("ticketId") String ticketId, @Param("atmId") String atmId);

}
