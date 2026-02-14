package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.TicketDowntimeByATMEntity;

@Repository
public interface ATMTicketDowntimeByATMRepository extends JpaRepository<TicketDowntimeByATMEntity, Long>{
	@Query(value = "EXEC usp_GetATMTicketDowntimeByATM :AtmId,:TicketNumber", nativeQuery = true) 
	public List<TicketDowntimeByATMEntity> getATMTicketDowntimeByATM(
			@Param("AtmId") String atmId, 
			@Param("TicketNumber") String  ticketNumber);
 
}