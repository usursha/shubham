package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketHistorySearchEntity;

@Repository
public interface TicketHistorySearchRepository extends JpaRepository<TicketHistorySearchEntity, String> {

	@Query(value = "EXEC ticket_history_search :cmUserId", nativeQuery = true)
    List<TicketHistorySearchEntity> getTicketHistoryDetailsByCmUserId(@Param("cmUserId") String cmUserId);
}

