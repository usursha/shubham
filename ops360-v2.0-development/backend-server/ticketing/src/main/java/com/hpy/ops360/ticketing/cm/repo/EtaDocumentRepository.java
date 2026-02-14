package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.EtaDocumentEntity;

@Repository
public interface EtaDocumentRepository extends JpaRepository<EtaDocumentEntity, Long> {

    @Query(value = "EXEC usp_GetEtaDocumentsByTicket :ticket_number", nativeQuery = true)
    List<EtaDocumentEntity> getEtaDocuments(@Param("ticket_number") String ticketNumber);
}
