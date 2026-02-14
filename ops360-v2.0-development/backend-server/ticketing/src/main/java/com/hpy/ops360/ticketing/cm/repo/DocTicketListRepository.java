package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.DocTicketListEntity;

@Repository
public interface DocTicketListRepository extends JpaRepository<DocTicketListEntity, Long> {

	@Query(value = "EXEC GetTicketDocumentsByDetails :atm_id, :status, :ticket_type, :startDate, :endDate, :ticket_number, :last_modified_date_str", nativeQuery = true)
	List<DocTicketListEntity> getTicketDetailsWithDocuments(@Param("atm_id") String atm_id,
			@Param("status") String status, @Param("ticket_type") String ticket_type,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("ticket_number") String ticket_number,
			@Param("last_modified_date_str") String last_modified_date_str);
}
