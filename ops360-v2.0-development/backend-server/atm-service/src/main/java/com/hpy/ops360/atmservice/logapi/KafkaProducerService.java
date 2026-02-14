package com.hpy.ops360.atmservice.logapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.atmservice.entity.ApiLog;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

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