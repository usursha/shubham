package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.TicketAtmLocationDetailsEntity;

@Repository
public interface TicketAtmLocationDetailsRepository extends JpaRepository<TicketAtmLocationDetailsEntity, Long>{
	@Query(value = "EXEC get_ticket_atm_location_details_portal :username, :ticket_number, :atm_id", nativeQuery = true) 
	public List<TicketAtmLocationDetailsEntity> getTicketAtmLocationDetails(
			@Param("username") String userName, 
			@Param("ticket_number") String ticketNumber, 
			@Param("atm_id") String atmId);
 
}