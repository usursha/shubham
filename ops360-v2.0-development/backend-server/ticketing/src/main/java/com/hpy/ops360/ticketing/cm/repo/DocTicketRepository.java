package com.hpy.ops360.ticketing.cm.repo;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.DocTicketDetailsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface DocTicketRepository extends JpaRepository<DocTicketDetailsEntity, Long>{
    
    @Query(value = "EXEC USP_GetTicketDetailsByAtmId :atm_id", nativeQuery = true)
    List<DocTicketDetailsEntity> getTicketDetailsByAtmId(@Param("atm_id") String atm_id);
}
