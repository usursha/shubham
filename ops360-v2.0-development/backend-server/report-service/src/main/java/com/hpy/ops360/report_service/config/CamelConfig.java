//package com.hpy.ops360.report_service.config;
//
//import org.apache.camel.CamelContext;
//import org.apache.camel.ProducerTemplate;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.direct.DirectComponent;
//import org.apache.camel.impl.DefaultCamelContext;
//import org.apache.camel.spring.boot.CamelAutoConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hpy.ops360.dashboard.dto.OpenTicketsResponse;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Configuration
//@Slf4j
//public class CamelConfig extends CamelAutoConfiguration {
//
//	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
////    @Bean
////    //@ConditionalOnProperty(name = "some.property", havingValue = "value1")
////    public CamelContext camelContext(ApplicationContext applicationContext) throws Exception {
////        CamelContext camelContext = new SpringCamelContext(applicationContext);
////        camelContext.start();
////        return camelContext;
////    }
//
////    @Bean
////    //@ConditionalOnProperty(name = "some.property", havingValue = "value2")
////    public ProducerTemplate producerTemplate(CamelContext camelContext) throws Exception {
////        return camelContext.createProducerTemplate();
////    }
//
////-----------------------original data ----------------------------------------------------
//
////	@Bean
////	public ProducerTemplate producerTemplate(CamelContext camelContext) throws Exception {
////		ProducerTemplate template = camelContext.createProducerTemplate();
////		// Start the template
////		template.start();
////		return template;
////	}
////
////	@Bean
////	public CamelContext camelContext() throws Exception {
////		CamelContext camelContext = new DefaultCamelContext();
////
////		camelContext.addComponent("direct", new DirectComponent());
////
////		camelContext.addRoutes(new RouteBuilder() {
////			@Override
////			public void configure() throws Exception {
////				from("direct:getCeOpenTicketDetails").process(exchange -> {
////					OpenTicketsWithCategoryDto response = exchange.getIn().getBody(OpenTicketsWithCategoryDto.class);
////
////					// Convert the response object to JSON string
////					String jsonString = convertObjectToJson(response);
////
////					// Call the stored procedure with the JSON string
////					saveToDatabaseUsingStoredProcedure(jsonString);
////
////					exchange.getIn().setBody("Data saved successfully");
////				}).log("Data saved to the database: ${body}");
////			}
////		});
////
////		camelContext.start();
////		return camelContext;
////	}
////
////	private String convertObjectToJson(OpenTicketsWithCategoryDto response) throws JsonProcessingException {
////		ObjectMapper objectMapper = new ObjectMapper();
////		String jsonString = objectMapper.writeValueAsString(response);
////		log.info("Generated JSON: {}", jsonString); // Log the JSON to verify its structure
////		return jsonString;
////	}
////
////	private void saveToDatabaseUsingStoredProcedure(String jsonString) {
////		try {
////			// Validate JSON data
////			if (jsonString == null || jsonString.isEmpty()) {
////				log.error("Invalid JSON data: empty or null");
////				return;
////			}
////
////			log.info("Calling stored procedure with JSON data: {}", jsonString);
////
////			String storedProcedure = "EXEC USP_InsertOpenTicketsSynergy @json_data=?";
////			int rowsAffected = jdbcTemplate.update(storedProcedure, jsonString);
////
////			if (rowsAffected > 0) {
////				log.info("Data saved successfully, rows affected: {}", rowsAffected);
////			} else {
////				log.warn("No rows were affected, please check the stored procedure and input data.");
////			}
////		} catch (Exception e) {
////			log.error("Error while saving data to the database: ", e);
////			throw new RuntimeException("Failed to save data to the database", e);
////		}
////	}
//
//	// -------------------------------------new one
//	// --------------------------------------------
//
//	@Bean
//	public ProducerTemplate producerTemplate(CamelContext camelContext) throws Exception {
//		ProducerTemplate template = camelContext.createProducerTemplate();
//		// Start the template
//		template.start();
//		return template;
//	}
//
//	@Bean
//	public CamelContext camelContext() throws Exception {
//		CamelContext camelContext = new DefaultCamelContext();
//
//		camelContext.addComponent("direct", new DirectComponent());
//
//		camelContext.addRoutes(new RouteBuilder() {
//			@Override
//			public void configure() throws Exception {
//				from("direct:getCeOpenTicketDetails").process(exchange -> {
//					OpenTicketsResponse response = exchange.getIn().getBody(OpenTicketsResponse.class);
//
//					// Convert the response object to JSON string
//					String jsonString = convertObjectToJson(response);
//
//					// Call the stored procedure with the JSON string
//					saveToDatabaseUsingStoredProcedure(jsonString);
//
//					exchange.getIn().setBody("Data saved successfully");
//				}).log("Data saved to the database: ${body}");
//			}
//		});
//
//		camelContext.start();
//		return camelContext;
//	}
//
//	private String convertObjectToJson(OpenTicketsResponse response) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		String jsonString = objectMapper.writeValueAsString(response);
//		log.info("Generated JSON: {}", jsonString); // Log the JSON to verify its structure
//		return jsonString;
//	}
//
//	private void saveToDatabaseUsingStoredProcedure(String jsonString) {
//		try {
//			// Validate JSON data
//			if (jsonString == null || jsonString.isEmpty()) {
//				log.error("Invalid JSON data: empty or null");
//				return;
//			}
//
//			log.info("Calling stored procedure with JSON data: {}", jsonString);
//
//			String storedProcedure = "EXEC USP_GetOpenTicketsSynergy @json_data=?";
//			int rowsAffected = jdbcTemplate.update(storedProcedure, jsonString);
//
//			if (rowsAffected > 0) {
//				log.info("Data saved successfully, rows affected: {}", rowsAffected);
//			} else {
//				log.warn("No rows were affected, please check the stored procedure and input data.");
//			}
//		} catch (Exception e) {
//			log.error("Error while saving data to the database: ", e);
//			throw new RuntimeException("Failed to save data to the database", e);
//		}
//	}
//
//}