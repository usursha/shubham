package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.entity.TicketsRaisedCount;

@Repository
public interface TicketRaisedCountRepository extends JpaRepository<TicketsRaisedCount, Integer> {

	@Query(value = "EXEC  USP_GetCETicketsRaisedCount :user_id", nativeQuery = true)
	TicketsRaisedCount getCETicketsRaisedCount(@Param("user_id") String userId);

	@Query(value = "EXEC  USP_GetTicketsRaisedCount :user_id", nativeQuery = true)
	TicketsRaisedCount getTicketsRaisedCount(@Param("user_id") String userId);

}
