package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketHistoryArchiveEntity;

@Repository
public interface TicketHistoryArchiveRepository extends JpaRepository<TicketHistoryArchiveEntity, Long> {

	
	@Query(value = "EXEC ticket_archive_data_list :startdate, :enddate, :bankName, :atmId , :ticketNo, :userId", nativeQuery = true)
	List<TicketHistoryArchiveEntity> getTicketHistoryArchiveData(@Param("startdate") String startdate,
			@Param("enddate") String enddate, @Param("bankName") String bankname,
			@Param("atmId") String atmid, @Param("ticketNo") String ticketno, @Param("userId") String userid);

	
	
}
