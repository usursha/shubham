package com.hpy.ops360.ticketing.route;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
import com.hpy.ops360.ticketing.dto.TicketCategoryCountDto;
import com.hpy.ops360.ticketing.response.OpenTicketsResponse;

@Component
public class KafkaToDbRoute extends RouteBuilder {

//	@Autowired
//	private OpenTicketsRepository openTicketsRepository;

//	@Override
//	public void configure() throws Exception {
//		from("kafka:ticket-details-topic").unmarshal().json(JsonLibrary.Jackson, TicketShortDetails.class)
//				.to("com.hpy.ops360.ticketing.route.TicketShortDetails");
//	}

//	@Override
//    public void configure() throws Exception {
//        from("kafka:ticket-details-topic")
//            .unmarshal().json(JsonLibrary.Jackson, TicketShortDetails.class)
//            .process(exchange -> {
//                TicketShortDetails details = exchange.getIn().getBody(TicketShortDetails.class);
//                openTicketsRepository.save(details);  // Save the details to the database
//            });
//    }

	// ----------------------------original------------------------------------

//	@Override
//    public void configure() throws Exception {
//        from("kafka:ticket-details-topic?brokers=localhost:9092")
//           .unmarshal().json(JsonLibrary.Jackson, OpenTicketsResponse.class)
//        .process(exchange -> {
//            String json = exchange.getIn().getBody(String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            OpenTicketsResponse response = mapper.readValue(json, OpenTicketsResponse.class);
//            exchange.getIn().setBody(response);
//            
//            
//        })
//        .to( )
//            .log("Data saved to database: ${body}");
//    }

	@Override
	public void configure() throws Exception {
		from("kafka:ticket-details-topic??brokers=localhost:9092")
				// .unmarshal().json(JsonLibrary.Jackson, OpenTicketsResponse.class)
				.process(exchange -> {
					// get input message given by the source
					Message input = exchange.getIn();

					// read body as string from input message
					String data = input.getBody(String.class);

					// Operation

					// OpenTicketsWithCategoryDto details =
					// exchange.getIn().getBody(OpenTicketsWithCategoryDto.class);
					OpenTicketsWithCategoryDto datamodified = exchange.getMessage(OpenTicketsWithCategoryDto.class);
					Message output = exchange.getMessage();
					// set data to output
					output.setBody(datamodified);
				}).to("file:D:/sourse/data.txt").log("Data saved to database: ${body}");
	}

//	@Override
//	public void configure() throws Exception {
//		from("kafka:ticket-details-topic?brokers=localhost:9092&groupId=ticketGroup").unmarshal()
//				.json(JsonLibrary.Jackson, OpenTicketsWithCategoryDto.class).process(exchange -> {
//					OpenTicketsWithCategoryDto details = exchange.getIn().getBody(OpenTicketsWithCategoryDto.class);
//					// Convert OpenTicketsWithCategoryDto to TicketShortDetails and save
//					List<TicketShortDetails> ticketDetails = convertToTicketShortDetails(details);
//					openTicketsRepository.saveAll(ticketDetails);
//				}).log("Data saved to database: ${body}");
//	}
//
//	private List<TicketShortDetails> convertToTicketShortDetails(OpenTicketsWithCategoryDto dto) {
//		if (dto == null || dto.getTicketShortDetails() == null) {
//			return Collections.emptyList();
//		}
//
//		return dto.getTicketShortDetails().stream().map(this::convertSingleTicket).collect(Collectors.toList());
//	}
//
//	private TicketShortDetails convertSingleTicket(AtmShortDetailsDto atmDto) {
//		TicketShortDetails ticketDetails = new TicketShortDetails();
//
//		ticketDetails.setAtmId(atmDto.getAtmId());
//		ticketDetails.setTicketNumber(atmDto.getTicketNumber());
//		ticketDetails.setBank(atmDto.getBank());
//		ticketDetails.setSiteName(atmDto.getSiteName());
//		ticketDetails.setOwner(atmDto.getOwner());
//		ticketDetails.setSubcall(atmDto.getSubcall());
//		ticketDetails.setVendor(atmDto.getVendor());
//		ticketDetails.setError(atmDto.getError());
//		ticketDetails.setDownTime(atmDto.getDownTime());
//		ticketDetails.setPriorityScore(BigDecimal.valueOf(atmDto.getPriorityScore()));
//		ticketDetails.setEventGroup(atmDto.getEventGroup());
//		ticketDetails.setIsBreakdown(atmDto.getIsBreakdown() == 1);
//		ticketDetails.setIsUpdated(atmDto.getIsUpdated() == 1);
//		ticketDetails.setIsTimedOut(atmDto.getIsTimedOut() == 1);
//		ticketDetails.setIsTravelling(atmDto.getIsTravelling() == 1);
//		// ticketDetails.setTravelTime(atmDto.getTravelTime());
//		ticketDetails.setTravelEta(BigDecimal.valueOf(atmDto.getTravelEta()));
//		ticketDetails.setDownCall(atmDto.getDownCall() == 1);
//
//		// Parse etaDateTime if it's not empty
//		if (atmDto.getEtaDateTime() != null && !atmDto.getEtaDateTime().isEmpty()) {
//			try {
//				ticketDetails.setEtaDateTime(
//						LocalDateTime.parse(atmDto.getEtaDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//			} catch (DateTimeParseException e) {
//				// Log the error and set etaDateTime to null
//				System.err.println("Error parsing etaDateTime: " + e.getMessage());
//				ticketDetails.setEtaDateTime(null);
//			}
//		}
//
//		ticketDetails.setEtaTimeout(atmDto.getEtaTimeout());
//
//		// Set color information
//		if (atmDto.getColor() != null) {
//			ticketDetails.setColorBorder(atmDto.getColor().getBorder());
//			ticketDetails.setColorFill(atmDto.getColor().getFill());
//		}
//
//		// Set current timestamp for createdDate
//		ticketDetails.setCreatedDate(LocalDateTime.now());
//
//		return ticketDetails;
//	}

}