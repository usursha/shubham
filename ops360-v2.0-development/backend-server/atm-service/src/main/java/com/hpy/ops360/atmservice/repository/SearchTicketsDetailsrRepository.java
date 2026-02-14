package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.TicketDetailEntity;
import com.hpy.ops360.atmservice.entity.TicketEntity;

public interface SearchTicketsDetailsrRepository extends JpaRepository<TicketDetailEntity, String> {

//	@Query(value = "EXEC usp_get_tickets_hims_forCm_ticketDetails_list :cmUserId, :atmId",nativeQuery = true)
//	List<TicketDetailEntity> getTicketDetailsByCmUserIdAndAtmId(@Param("cmUserId") String cmUserId,
//			@Param("atmId") String atmId);

	
	
	 @Query(value = "EXEC usp_get_tickets_hims_forCm_ticketDetails_without_pagenation " +
	            ":cmUserId, :searchText, :atmId, :sort, :category, :owner, :subCallType, :status, :vendor, " +
	            ":ticketAgingHr, :ticketAgingHrFrom, :ticketAgingHrTo, :ticketAgingDay, :ticketAgingDayStart, :ticketAgingDayEnd, " +
	            ":creationDate, :creationDateFrom, :creationDateTo", 
	            nativeQuery = true)
	    List<TicketDetailEntity> getTicketDetails(
	            @Param("cmUserId") String cmUserId,
	            @Param("searchText") String searchText,
	            @Param("atmId") String atmId,
	            @Param("sort") String sort,
	            @Param("category") String category,
	            @Param("owner") String owner,
	            @Param("subCallType") String subCallType,
	            @Param("status") String status,
	            @Param("vendor") String vendor,
	            @Param("ticketAgingHr") String ticketAgingHr,
	            @Param("ticketAgingHrFrom") String ticketAgingHrFrom,
	            @Param("ticketAgingHrTo") String ticketAgingHrTo,
	            @Param("ticketAgingDay") String ticketAgingDay,
	            @Param("ticketAgingDayStart") String ticketAgingDayStart,
	            @Param("ticketAgingDayEnd") String ticketAgingDayEnd,
	            @Param("creationDate") String creationDate,
	            @Param("creationDateFrom") String creationDateFrom,
	            @Param("creationDateTo") String creationDateTo
	    );
}
