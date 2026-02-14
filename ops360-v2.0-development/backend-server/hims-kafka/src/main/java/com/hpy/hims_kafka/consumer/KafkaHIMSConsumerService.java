package com.hpy.hims_kafka.consumer;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.hims_kafka.dto.TicketDetailsOpen;
import com.hpy.hims_kafka.entity.GetTicketDetailsClose;
import com.hpy.hims_kafka.entity.GetTicketDetailsOpen;
import com.hpy.hims_kafka.repo.GetTicketDetailsCloseRepository;
import com.hpy.hims_kafka.repo.GetTicketDetailsOpenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaHIMSConsumerService {

    @Autowired
    private GetTicketDetailsOpenRepository openRepository;

    @Autowired
    private GetTicketDetailsCloseRepository closeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "ops360ticketdetails", groupId = "ops360-ticket-details-group")
    public void consume(String message, Acknowledgment acknowledgment) {
        log.trace("Kafka listener triggered with raw message: {}", message);

        try {
            TicketDetailsOpen incoming = objectMapper.readValue(message, TicketDetailsOpen.class);
            String srno = incoming.getSrno();

            log.debug("Deserialized message into TicketDetailsOpen: {}", incoming);
            log.info("Deserialized message into TicketDetailsOpen: {}", incoming);
            log.info("Processing ticket update for srno: {}", srno);

            Optional<GetTicketDetailsOpen> existingOpen = openRepository.findById(srno);
            boolean isCloseStatus = "Close".equalsIgnoreCase(incoming.getStatus());

            if (isCloseStatus) {
                log.debug("Ticket status is 'Close' for srno: {}", srno);

                if (existingOpen.isPresent()) {
                    log.debug("Existing open ticket found for srno: {}, proceeding to delete", srno);
                    openRepository.deleteById(srno);
                    log.info("Deleted srno {} from Open table", srno);
                } else {
                    log.debug("No open ticket found for srno: {}, skipping deletion", srno);
                }

                GetTicketDetailsClose closedTicket = new GetTicketDetailsClose(incoming);
                closeRepository.save(closedTicket);
                log.info("Inserted srno {} into Close table", srno);
            } else {
                log.debug("Ticket status is not 'Close' for srno: {}", srno);

                if (existingOpen.isPresent()) {
                    log.debug("Existing open ticket found for srno: {}, updating record", srno);
                    GetTicketDetailsOpen updated = existingOpen.get();
                    BeanUtils.copyProperties(incoming, updated);
                    openRepository.save(updated);
                    log.info("Updated srno {} in Open table", srno);
                } else {
                    log.debug("No open ticket found for srno: {}, inserting new record", srno);
                    GetTicketDetailsOpen newTicket = new GetTicketDetailsOpen(incoming);
                    openRepository.save(newTicket);
                    log.info("Inserted srno {} into Open table", srno);
                }
            }

            acknowledgment.acknowledge();
            log.info("Finished processing ticket with acknowledge for srno: {}", srno);
            log.trace("Finished processing ticket with acknowledge for srno: {}", srno);

        } catch (Exception e) {
            log.error("Error processing ticket update. Raw message: {}", message, e);
        }
    }
}
