//package com.hpy.ops360.ticketing.route;
//
//
//
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.model.dataformat.JsonLibrary;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
//import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
//import com.hpy.ops360.ticketing.service.CmTaskService;
//import com.hpy.ops360.ticketing.service.TaskService;
//
//@Component
//public class TicketProcessingRoute extends RouteBuilder {
//
//	@Autowired
//	private CmTaskService taskService;
//
//    //private TaskService taskService;
//
////    @Override
////    public void configure() throws Exception {
////        from("direct:getCeOpenTicketDetails")
////        .log("Processing request for CE Open Ticket Details--------------------------------------------------")
////            .process(exchange -> {
////                Object[] params = exchange.getIn().getBody(Object[].class);
////                String ceId = (String) params[0];
////                String status = (String) params[1];
////                OpenTicketsWithCategoryDto result = taskService.getTicketDetailsByCEAndStatus(ceId, status);
////                exchange.getMessage().setBody(result);
////            })
////            .marshal().json(JsonLibrary.Jackson)
////            .to("kafka:ticket-details-topic");
////    }
//	 @Override
//	    public void configure() throws Exception {
//	        from("direct:getCeOpenTicketDetails")
//	            .log("Processing request for CE Open Ticket Details-------------------------------------")
//	            .process(exchange -> {
//	                // Retrieve parameters passed from the controller
//	                Object[] params = exchange.getIn().getBody(Object[].class);
//	                String ceId = (String) params[1];
//	                String status = (String) params[1];	              
//	                // Perform some processing, e.g., fetch ticket details
//	                // This is where you would call your service or business logic
//	                OpenTicketsWithCategoryDto result = taskService.getTicketDetailsByCEAndStatus(ceId, status);
//	                
//	                // Set the result as the message body
//	                exchange.getMessage().setBody(result);
//	            })
//	            //.marshal().json(JsonLibrary.Jackson) // Convert the result to JSON
//	            .to("kafka:ticket-details-topic"); // Send the result to Kafka, for example
//	    }
//	
//
//
//}
