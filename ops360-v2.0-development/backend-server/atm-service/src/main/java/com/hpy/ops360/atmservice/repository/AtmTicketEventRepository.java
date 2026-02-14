package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.AtmTicketEvent;

@Repository
public interface AtmTicketEventRepository extends JpaRepository<AtmTicketEvent, Long> {

	@Query(value = "EXEC USP_GetAtmTicketEvent :username,:atm_ticket_eventcode", nativeQuery = true)
	List<AtmTicketEvent> getAtmTicketEvent(@Param("username") String userId,
			@Param("atm_ticket_eventcode") String atmTicketEventcode);

}
