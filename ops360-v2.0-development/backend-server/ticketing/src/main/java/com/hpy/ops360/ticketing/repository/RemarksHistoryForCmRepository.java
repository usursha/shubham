package com.hpy.ops360.ticketing.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.RemarksHistory;
import com.hpy.ops360.ticketing.entity.RemarksHistoryforCm;

@Repository
public interface RemarksHistoryForCmRepository extends JpaRepository<RemarksHistoryforCm, Long> {

	@Query(value = "EXEC USP_GetRemarksHistory_cm :user_id,:ticket_number,:atm_id", nativeQuery = true)
	List<RemarksHistoryforCm> getRemarksHistoryforCm(@Param("user_id") String userId, @Param("ticket_number") String ticketNumber,
			@Param("atm_id") String atmId);

}

