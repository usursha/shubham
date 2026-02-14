package com.hpy.monitoringservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.monitoringservice.entity.ApiLog;
import com.hpy.monitoringservice.repository.ApiLogRepository;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private ApiLogRepository apiLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "apilogs", groupId = "api-logs-group")
    public void consume(String message) {
        try {
            // Deserialize the message into ApiLog object
            ApiLog apiLog = objectMapper.readValue(message, ApiLog.class);
            
            // Save the log to the database
            log.info("Received log: {}", apiLog);
            apiLogRepository.save(apiLog);
        } catch (Exception e) {
            log.error("Error consuming ticket update event: {}", e.getMessage(), e);
        }
    }
}

