package com.hpy.ops360.ticketing.cm.repo;

import com.hpy.ops360.ticketing.cm.entity.TicketWithDocumentsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface TicketWithDocumentsRepo extends CrudRepository<TicketWithDocumentsEntity, Long> {

	@Query(value = "EXEC GetTicketsWithDocuments :atm_id, :status, :ticket_type, :startDate, :endDate", nativeQuery = true)
	List<TicketWithDocumentsEntity> getTicketsWithDocuments(@Param("atm_id") String atmId,
			@Param("status") String status, @Param("ticket_type") String ticketType,
			@Param("startDate") String startdate, @Param("endDate") String endDate);
}
