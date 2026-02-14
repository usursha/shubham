package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketArchiveListEntity;
import com.hpy.ops360.ticketing.cm.entity.TicketHistoryEntity;

@Repository
public interface TicketArchiveListRepository extends JpaRepository<TicketArchiveListEntity, Long> {

	@Query(value = "EXEC ticket_archive_dropdown_list :userId", nativeQuery = true)
	List<TicketArchiveListEntity> getTicketArchiveListData(@Param("userId") String userId);
	
	@Query(value = "EXEC ticket_archive_dropdown_list :cm_user_id,:bankname,:atmcode,:ticket_number,:ce_user_id", nativeQuery = true)
	List<TicketArchiveListEntity> getTicketArchiveListDataPerDropDown(@Param("cm_user_id") String cmUserId,
			@Param("bankname") String bankname, @Param("atmcode") String atmcode,
			@Param("ticket_number") String ticketNumber, @Param("ce_user_id") String ceUserId);
}

