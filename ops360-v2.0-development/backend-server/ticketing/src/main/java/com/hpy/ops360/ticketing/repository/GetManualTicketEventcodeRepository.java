package com.hpy.ops360.ticketing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.GetManualTicketEventcode;

public interface GetManualTicketEventcodeRepository extends JpaRepository<GetManualTicketEventcode, Long> {
	
	@Query(value="EXEC ops_get_eventcode_manual_ticket :ticket_number,:atmid", nativeQuery = true)
	public Optional<GetManualTicketEventcode> getManualTicketEventcode(@Param("ticket_number") String manualTicketNumber,@Param("atmid") String atmid);

}
