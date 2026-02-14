package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.cm.entity.TicketSearchSuggestion;

public interface TicketSearchSuggestionRepository extends JpaRepository<TicketSearchSuggestion, Long> {
	
	@Query(value="EXEC ops_get_open_or_close_tickets_search_suggestion :cm_user_id,:flag",nativeQuery = true)
	public List<TicketSearchSuggestion> getTicketSearchSuggestion(@Param("cm_user_id") String cmUserId,@Param("flag") String flag);

}
