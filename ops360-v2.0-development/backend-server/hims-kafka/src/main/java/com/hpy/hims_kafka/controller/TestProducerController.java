package com.hpy.hims_kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.hims_kafka.consumer.KafkaProducerService;
import com.hpy.hims_kafka.dto.TicketDetailsOpen;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/hims/kafka")
public class TestProducerController {

    @Autowired
    private KafkaProducerService producerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendTestMessage(@RequestBody TicketDetailsOpen ticket) {
    	log.info("Received test message request for srno: {}", ticket.getSrno());
    	
        producerService.sendTicketUpdate(ticket);
        return ResponseEntity.ok("Message sent");
    }
}
