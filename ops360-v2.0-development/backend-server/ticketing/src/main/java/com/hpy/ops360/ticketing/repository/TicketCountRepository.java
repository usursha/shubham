package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketCount;

@Repository
public interface TicketCountRepository extends JpaRepository<TicketCount, Long> {

	@Query(value = "EXEC USP_GetTicketsNumberOfCEListCount :userId", nativeQuery = true)
	TicketCount getTicketsNumberOfCEListCount(@Param("userId") String userId);
}