package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.CMTask;
import com.hpy.ops360.ticketing.entity.CMTaskDetails;

public interface CmTaskRepository extends JpaRepository<CMTask, Long> {

//	@Query(value = "EXEC USP_GetTicketsNumberDetails @user_id = :userId, @ticket_number = :ticketNumber", nativeQuery = true)
//	CMTask getTicketsNumberDetails(String userId, String ticketNumber);

	@Query(value = "EXEC USP_GetTicketsNumberDetails @user_id = :userId,@ticket_number = :ticket_number,@atm_id = :atm_id", nativeQuery = true)
	CMTaskDetails getTicketsNumberDetails(@Param("userId") String userId, @Param("ticket_number") String ticketNumber,
			@Param("atm_id") String atmId);

	@Query(value = "EXEC USP_GetTicketsNumberOfCEList @user_id=:userId", nativeQuery = true)
	List<CMTask> getTicketsNumberOfCEList(@Param("userId") String userId);

//	@Query(value = "EXEC USP_GetCETicketsRaisedCount @user_id=:userId", nativeQuery = true)
//	List<CMTask> findTicketsNumberOfCEList(@Param("userId") String userId);

	// USP_GetCETicketsRaisedCount

	// @Modifying
	@Query(value = "EXECUTE dbo.USP_UpdateTicketsNumberData :ce_user_id, :atm_id, :ref_no, :status, :checker_name, :checker_reject_reason, :checker_comment, :crm_status,:ticket_number", nativeQuery = true)
	// @Transactional
	Integer updateTicketsNumberData(@Param("ce_user_id") String ceUserId, @Param("atm_id") String atmId,
			@Param("ref_no") String refNo, @Param("status") String status, @Param("checker_name") String checkerName,
			@Param("checker_reject_reason") String checkerRejectReason, @Param("checker_comment") String checkerComment,
			@Param("crm_status") String crmStatus, @Param("ticket_number") String ticketNumber);

}
