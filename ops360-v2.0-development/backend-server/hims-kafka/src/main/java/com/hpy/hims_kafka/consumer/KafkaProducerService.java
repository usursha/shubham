package com.hpy.hims_kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.hims_kafka.dto.TicketDetailsOpen;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOPIC = "ops360ticketdetails";

    public void sendTicketUpdate(TicketDetailsOpen ticket) {
        try {
            String message = objectMapper.writeValueAsString(ticket);
            kafkaTemplate.send(TOPIC, ticket.getSrno(), message);
            log.info("Sent ticket update for srno {} to topic '{}'", ticket.getSrno(), TOPIC);
        } catch (Exception e) {
            log.error("Failed to send ticket update for srno {}: {}", ticket.getSrno(), e.getMessage(), e);
        }
    }
}
