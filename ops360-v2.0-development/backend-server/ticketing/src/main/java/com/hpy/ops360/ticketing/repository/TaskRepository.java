package com.hpy.ops360.ticketing.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.Task;

@Repository
public interface TaskRepository extends IGenericRepo<Task> {

	@Query("SELECT count(t.ticketNumber) FROM Task t")
	Integer findMaxTicketNumber();

	@Query(value = "EXEC Usp_CREATE_ticket :atm_id, :comment, :created_by, :diagnosis, :document, :document_name, :reason, :ref_no, :status,:ticket_number, :username,:document1, :document1_name, :document2, :document2_name, :document3, :document3_name, :document4, :document4_name", nativeQuery = true)
	int addTask(@Param("atm_id") String atmid, @Param("comment") String comment, @Param("created_by") String createdBy,
			@Param("diagnosis") String diagnosis, @Param("document") String document,
			@Param("document_name") String documentName, @Param("reason") String reason, @Param("ref_no") String refNo,
			@Param("status") String status, @Param("ticket_number") String ticketNumber,
			@Param("username") String username, @Param("document1") String document1,
			@Param("document1_name") String document1Name, @Param("document2") String document2,
			@Param("document2_name") String document2Name, @Param("document3") String document3,
			@Param("document3_name") String document3Name, @Param("document4") String document4,
			@Param("document4_name") String document4Name);

	@Query(value = "EXEC Get_OPSClosureTicket :ticket_number,:atm_id,:comments,:closure_date", nativeQuery = true)
	int closeTicket(@Param("ticket_number") String ticketNumber, @Param("atm_id") String atmNo,
			@Param("comments") String comments, @Param("closure_date") String closureDate);

//	@Query(value = "EXEC Usp_CREATE_ticket :#{#ticketNumber}, #atmId}, :#{#comment}, :#{#createdBy}, :#{#createdDate}, :#{#diagnosis}, :#{#document}, :#{#documentName},:#{#lastModifiedBy}, :#{#lastModifiedDate}, :#{#reason}, :#{#refNo}, :#{#status}, :#{#username}", nativeQuery = true)
//	int addTask(@Param("ticket_number") String ticketNumber, @Param("atm_id") String atmid, @Param("comment") String comment, @Param("created_by") String createdBy,
//			@Param("created_date") LocalDateTime createdDate, @Param("diagnosis") String diagnosis,
//			@Param("document") String document, @Param("document_name") String documentName, @Param("last_modified_date") LocalDateTime lastModifiedDate, @Param("reason") String reason, @Param("ref_no") String refNo, @Param("status") String status,
//			@Param("username") String username);

	long countByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	@Query("SELECT COUNT(t) FROM Task t WHERE t.createdDate >= :startOfDay")
	long countCreatedToday(@Param("startOfDay") LocalDateTime startOfDay);
//
}
