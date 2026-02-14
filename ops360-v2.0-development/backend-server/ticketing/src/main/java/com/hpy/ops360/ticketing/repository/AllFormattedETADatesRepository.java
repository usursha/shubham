package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.AllFormattedETADatesEntity;

@Repository
public interface AllFormattedETADatesRepository extends JpaRepository<AllFormattedETADatesEntity, String> {
	@Query(value = "EXEC USP_GetFormattedETADates :AtmId, :TicketNumber", nativeQuery = true)
	List<AllFormattedETADatesEntity> getAllFormattedETADates(@Param("AtmId") String AtmId,
			@Param("TicketNumber") String TicketNumber);

}
