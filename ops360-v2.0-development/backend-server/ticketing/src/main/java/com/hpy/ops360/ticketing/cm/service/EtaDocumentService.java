package com.hpy.ops360.ticketing.cm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.Document;
import com.hpy.ops360.ticketing.cm.dto.DocumentResponseDTO;
import com.hpy.ops360.ticketing.cm.entity.EtaDocumentEntity;
import com.hpy.ops360.ticketing.cm.repo.EtaDocumentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EtaDocumentService {

    @Autowired
    private EtaDocumentRepository repository;

    public DocumentResponseDTO getDocuments(String ticketNumber) {
        log.info("Fetching documents for ticket: {}", ticketNumber);

        List<EtaDocumentEntity> results = repository.getEtaDocuments(ticketNumber);
        if (results == null || results.isEmpty()) {
            log.warn("No documents found for ticket: {}", ticketNumber);
            return null;
        }
        List<Document> documentList = new ArrayList<>();
        DocumentResponseDTO response = new DocumentResponseDTO();
        for (int i = 0; i < results.size(); i++) {
			
		

        EtaDocumentEntity entity = results.get(i); // Assuming one row per ticket
        

        addIfValid(documentList, entity.getDocument(), entity.getDocumentName());
        addIfValid(documentList, entity.getDocument1(), entity.getDocument1Name());
        addIfValid(documentList, entity.getDocument2(), entity.getDocument2Name());
        addIfValid(documentList, entity.getDocument3(), entity.getDocument3Name());
        addIfValid(documentList, entity.getDocument4(), entity.getDocument4Name());
        
        response.setDocumentList(documentList);
        }
        response.setStatus(results.get(0).getStatus());
        response.setTicketType(results.get(0).getTicketType());
        response.setIsFlagged(results.get(0).getIsFlagged());
        response.setHoursPassed(results.get(0).getHoursPassed());
        return response;
        
    }

    private void addIfValid(List<Document> list, String doc, String name) {
        if (doc != null && !doc.isEmpty()) {
            list.add(new Document(doc, name));
        }
    }
}
