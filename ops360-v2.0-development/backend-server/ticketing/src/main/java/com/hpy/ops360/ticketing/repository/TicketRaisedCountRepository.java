package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.dto.TicketCountDTO;
import com.hpy.ops360.ticketing.entity.TicketsRaisedCount;
@Repository
public interface TicketRaisedCountRepository extends JpaRepository<TicketsRaisedCount, Integer>{

	@Query(value = "EXEC  USP_GetTicketsRaisedCount :user_id", nativeQuery = true)
	TicketsRaisedCount getTicketsRaisedCount(@Param("user_id") String userId);

}
