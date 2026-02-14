package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.RemarksHistory;

@Repository
public interface RemarksHistoryRepository extends JpaRepository<RemarksHistory, Long> {

	@Query(value = "EXEC USP_GetRemarksHistory :user_id,:ticket_number,:atm_id", nativeQuery = true)
	List<RemarksHistory> getRemarksHistory(@Param("user_id") String userId, @Param("ticket_number") String ticketNumber,
			@Param("atm_id") String atmId);

}
