package com.hpy.ops360.atmservice.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.TicketdetailsEntityHims;

//import com.hpy.ops360.ticketing.dto.TicketDetailsDto;

@Repository
public interface TickethistoryForCEHism extends JpaRepository<TicketdetailsEntityHims, String> {

	/*
	 * @Query(value = "EXEC USP_GetAtmTicketEvent :atmid", nativeQuery = true)
	 * List<AtmHistoryNTicketsResponse> getAtmTicketHistory();
	 */

//	@Query(value = "SELECT * FROM [172.16.15.36].[FLM_CRM_UAT].[dbo].[GetTicketDetails_Close] WHERE equipmentid = :atmid AND calltype='Complaint'", nativeQuery = true)
//	List<TicketdetailsEntityHims> findOpenTicketsByEquipmentId(@Param("atmid") String equipmentId);
//	
	
	@Query(value = "EXEC USP_findOpenTicketsByEquipmentId :atmid", nativeQuery = true)
	List<TicketdetailsEntityHims> findOpenTicketsByEquipmentId(@Param("atmid") String equipmentId);

}