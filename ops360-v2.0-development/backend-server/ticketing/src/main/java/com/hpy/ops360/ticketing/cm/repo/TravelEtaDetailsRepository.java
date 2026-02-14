package com.hpy.ops360.ticketing.cm.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TravelEtaDetails;

@Repository
public interface TravelEtaDetailsRepository extends JpaRepository<TravelEtaDetails, Long> {



	@Query(value = "EXEC USP_GetTicketETA_TravelDetails :atmId, :ticketNumber", nativeQuery = true)
	Optional<TravelEtaDetails> getTravelDetails(@Param("atmId") String atmId,
			@Param("ticketNumber") String ticketNumber);
}
