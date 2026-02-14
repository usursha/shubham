package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.ManuallyTickets;

public interface ManuallyTicketRepository extends JpaRepository<ManuallyTickets, Long> {

	@Query(value = "EXEC dbo.USP_Get_ManuallyTickets :username", nativeQuery = true)
	List<ManuallyTickets> getManuallyTickets(@Param("username") String username);

}
