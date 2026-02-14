package com.hpy.sampatti_data_service.logapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.sampatti_data_service.entity.ApiLog;

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
			log.error("Error sending ticket update event: {}", e.getMessage(), e);
		}
	}
}