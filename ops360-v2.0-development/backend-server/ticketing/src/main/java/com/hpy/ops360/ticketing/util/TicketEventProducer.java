package com.hpy.ops360.ticketing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.ticketing.cm.entity.ApiLog;
import com.hpy.ops360.ticketing.dto.TicketUpdateEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TicketEventProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public void sendTicketUpdate(TicketUpdateEvent event) {
		try {
			String message = objectMapper.writeValueAsString(event);
			
			log.info("Sending ticket update event: {}", message);
			kafkaTemplate.send("email", message);
			log.info("Ticket update event sent successfully");
		} catch (Exception e) {
			log.error("Error sending ticket update event: {}", e.getMessage(), e);
		}
	}
	
	public void sendMonitoring(ApiLog apiLog) {
		try {
			String message = objectMapper.writeValueAsString(apiLog);
			
			log.info("Sending ticket update event: {}", message);
			kafkaTemplate.send("apilogs", message);
			log.info("Ticket update event sent successfully");
		} catch (Exception e) {
			log.warn("Failed to send message to Kafka, marking broker as unavailable");
		}
	}
}