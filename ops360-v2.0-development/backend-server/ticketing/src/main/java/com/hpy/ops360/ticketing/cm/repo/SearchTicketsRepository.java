package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.cm.entity.SearchTickets;

public interface SearchTicketsRepository extends JpaRepository<SearchTickets, Long> {
	 @Query(value = "EXEC usp_get_Open_Or_close_tickets_by_search :cmUserId, :flag", nativeQuery = true)
	    List<SearchTickets> getTicketsForCmNative(
	            @Param("cmUserId") String cmUserId,
	            @Param("flag") String flag
	    );
}
