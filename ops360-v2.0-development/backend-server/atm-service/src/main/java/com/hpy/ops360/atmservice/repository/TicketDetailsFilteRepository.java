package com.hpy.ops360.atmservice.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.TicketEntity;

@Repository
public interface TicketDetailsFilteRepository extends JpaRepository<TicketEntity, String> {

	 @Query(value = "EXEC usp_get_tickets_hims_forCm_ticketDetails " +
	           "@cm_user_id = :cmUserId, " +
	           "@pageSize = :pageSize, " +
	           "@pageNumber = :pageNumber, " +
	           "@searchText = :searchText, " +
	           "@Atmid = :atmId, " +
	           "@sort = :sort, " +
	           "@Category = :category, " +
	           "@owner = :owner, " +
	           "@Subcalltype = :subCallType, " +
	           "@status = :status, " +
	           "@vendor = :vendor, " +
	           "@ticket_Aging_Hr = :ticketAgingHr, " +
	           "@ticket_Aging_Hr_from = :ticketAgingHrFrom, " +
	           "@ticket_Aging_Hr_to = :ticketAgingHrTo, " +
	           "@ticket_Aging_day = :ticketAgingDay, " +
	           "@ticket_Aging_day_start = :ticketAgingDayStart, " +
	           "@ticket_Aging_day_end = :ticketAgingDayEnd, " +
	           "@CreationDate = :creationDate, " +
	           "@CreationDate_from = :creationDateFrom, " +
	           "@CreationDate_to = :creationDateTo", 
	           nativeQuery = true)
	    List<TicketEntity> getTicketsForCM(
	        @Param("cmUserId") String cmUserId,
	        @Param("pageSize") String pageSize,
	        @Param("pageNumber") String pageNumber,
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