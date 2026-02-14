package com.hpy.ops360.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.TicketRaisedCategoriesByAtm;

public interface TicketRaisedCategoriesByAtmRepository extends JpaRepository<TicketRaisedCategoriesByAtm, String> {

	@Query(value = "EXEC [dbo].[USP_GetCmTicketsRaisedCategoriesByAtmId] :userId, :atmId", nativeQuery = true)
	TicketRaisedCategoriesByAtm getTicketsByUserAndAtm(@Param("userId") String userId, @Param("atmId") String atmId);
}
