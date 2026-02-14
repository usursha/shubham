//package com.hpy.ops360.ticketing.route;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class TicketDataRoute {
//
//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
//
//	private final RestTemplate restTemplate = new RestTemplate();
//
//	public void fetchDataAndSendToKafka() {
//		String apiUrl = "https://ops360dev.hitachi-payments.com/synergy/HitachiAPI_Integration/api";
//		String response = restTemplate.postForObject(apiUrl, null, String.class);
//		kafkaTemplate.send("api-data-topic", response);
//	}
//}