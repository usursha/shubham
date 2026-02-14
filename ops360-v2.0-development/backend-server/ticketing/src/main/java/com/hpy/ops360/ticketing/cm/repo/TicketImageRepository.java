package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketWithoutDocument;

@Repository
public interface TicketImageRepository extends JpaRepository<TicketWithoutDocument, Long> {
    
    @Query(value = "EXEC GetTicketsWithoutDocuments :atmId", nativeQuery = true)
    List<TicketWithoutDocument> getTicketsByAtmId(@Param("atmId") String atmId);
}


