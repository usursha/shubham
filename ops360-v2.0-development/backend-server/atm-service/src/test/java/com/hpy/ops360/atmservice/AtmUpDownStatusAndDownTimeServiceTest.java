package com.hpy.ops360.atmservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.atmservice.dto.AtmUpDownStatusAndDownTimeDto;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.AtmTicketEvent;
import com.hpy.ops360.atmservice.response.AtmDetailsWithTickets;
import com.hpy.ops360.atmservice.response.OpenTicketsResponse;
import com.hpy.ops360.atmservice.response.TicketDetailsDto;
import com.hpy.ops360.atmservice.service.AtmUpDownStatusAndDownTimeService;
import com.hpy.ops360.atmservice.service.SynergyService;
import com.hpy.ops360.atmservice.service.UserAtmDetailsService;

@ExtendWith(MockitoExtension.class)
public class AtmUpDownStatusAndDownTimeServiceTest {

	@Mock
	private SynergyService synergyService;

	@Mock
	private UserAtmDetailsService userAtmDetailsService;

	@Mock
	private LoginService loginService;

	@InjectMocks
	private AtmUpDownStatusAndDownTimeService atmUpDownStatusAndDownTimeService;

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	@BeforeEach
	void setUp() {
		when(loginService.getLoggedInUser()).thenReturn("testUser");
	}

	@Test
	void fetchAtmUpdownStatus_openTicketsNoBreakdown_shouldReturnOperational() {
		String atmId = "ATM002";

		TicketDetailsDto ticket1 = new TicketDetailsDto();
		ticket1.setEquipmentid(atmId);
		ticket1.setSrno("SR001");
		ticket1.setEventcode("NON_BREAKDOWN_EVENT");
		ticket1.setCreateddate(LocalDateTime.now().minusHours(1).format(DATE_TIME_FORMATTER));

		AtmDetailsWithTickets atmDetails = new AtmDetailsWithTickets();
		atmDetails.setAtmId(atmId);
		atmDetails.setOpenticketdetails(List.of(ticket1));

		OpenTicketsResponse mockResponse = new OpenTicketsResponse();
		mockResponse.setAtmdetails(List.of(atmDetails));

		when(synergyService.getOpenTicketDetails(anyList())).thenReturn(mockResponse);

		AtmTicketEvent atmTicketEvent = new AtmTicketEvent();
		atmTicketEvent.setIsBreakdown(0); // Not a breakdown
		when(userAtmDetailsService.getAtmTicketEvent(eq("testUser"), any(String.class)))
				.thenReturn(List.of(atmTicketEvent));

		AtmUpDownStatusAndDownTimeDto result = atmUpDownStatusAndDownTimeService.fetchAtmUpdownStatus(atmId);

		assertEquals("Operational", result.getStatus());
		assertEquals("00h 00m", result.getDownTime());
	}

	@Test
	void fetchAtmUpdownStatus_openTicketsWithBreakdown_shouldReturnDownAndCalculateDowntime() {
		String atmId = "ATM003";
		LocalDateTime createdTime = LocalDateTime.now().minusHours(2).minusMinutes(30);

		TicketDetailsDto ticket1 = new TicketDetailsDto();
		ticket1.setEquipmentid(atmId);
		ticket1.setSrno("SR001");
		ticket1.setEventcode("BREAKDOWN_EVENT");
		ticket1.setCreateddate(createdTime.format(DATE_TIME_FORMATTER));

		TicketDetailsDto ticket2 = new TicketDetailsDto();
		ticket2.setEquipmentid(atmId);
		ticket2.setSrno("SR002");
		ticket2.setEventcode("ANOTHER_BREAKDOWN_EVENT");
		ticket2.setCreateddate(createdTime.plusMinutes(10).format(DATE_TIME_FORMATTER)); // Later ticket

		AtmDetailsWithTickets atmDetails = new AtmDetailsWithTickets();
		atmDetails.setAtmId(atmId);
		atmDetails.setOpenticketdetails(List.of(ticket1, ticket2));

		OpenTicketsResponse mockResponse = new OpenTicketsResponse();
		mockResponse.setAtmdetails(List.of(atmDetails));

		when(synergyService.getOpenTicketDetails(anyList())).thenReturn(mockResponse);

		AtmTicketEvent breakdownEvent1 = new AtmTicketEvent();
		breakdownEvent1.setIsBreakdown(1);
		AtmTicketEvent breakdownEvent2 = new AtmTicketEvent();
		breakdownEvent2.setIsBreakdown(1);

		when(userAtmDetailsService.getAtmTicketEvent(eq("testUser"), any(String.class)))
				.thenReturn(List.of(breakdownEvent1, breakdownEvent2));

		AtmUpDownStatusAndDownTimeDto result = atmUpDownStatusAndDownTimeService.fetchAtmUpdownStatus(atmId);

		assertEquals("Down", result.getStatus());
		// Calculate expected downtime based on `createdTime` and `LocalDateTime.now()`
		long expectedTotalMinutes = java.time.Duration.between(createdTime, LocalDateTime.now()).toMinutes();
		long expectedHours = expectedTotalMinutes / 60;
		long expectedMinutes = expectedTotalMinutes % 60;
		String expectedDowntime = String.format("%02dh %02dm", expectedHours, expectedMinutes);

		assertEquals(expectedDowntime, result.getDownTime());
	}

	@Test
	void fetchAtmUpdownStatus_openTicketsWithMixOfBreakdownAndNonBreakdown_shouldReturnDownIfAnyBreakdown() {
		String atmId = "ATM004";
		LocalDateTime createdTime = LocalDateTime.now().minusHours(1).minusMinutes(15);

		TicketDetailsDto ticket1 = new TicketDetailsDto();
		ticket1.setEquipmentid(atmId);
		ticket1.setSrno("SR001");
		ticket1.setEventcode("NON_BREAKDOWN_EVENT");
		ticket1.setCreateddate(createdTime.plusMinutes(30).format(DATE_TIME_FORMATTER));

		TicketDetailsDto ticket2 = new TicketDetailsDto();
		ticket2.setEquipmentid(atmId);
		ticket2.setSrno("SR002");
		ticket2.setEventcode("BREAKDOWN_EVENT");
		ticket2.setCreateddate(createdTime.format(DATE_TIME_FORMATTER)); // This should be the earliest breakdown ticket

		AtmDetailsWithTickets atmDetails = new AtmDetailsWithTickets();
		atmDetails.setAtmId(atmId);
		atmDetails.setOpenticketdetails(List.of(ticket1, ticket2));

		OpenTicketsResponse mockResponse = new OpenTicketsResponse();
		mockResponse.setAtmdetails(List.of(atmDetails));

		when(synergyService.getOpenTicketDetails(anyList())).thenReturn(mockResponse);

		AtmTicketEvent nonBreakdownEvent = new AtmTicketEvent();
		nonBreakdownEvent.setIsBreakdown(0);
		AtmTicketEvent breakdownEvent = new AtmTicketEvent();
		breakdownEvent.setIsBreakdown(1);

		when(userAtmDetailsService.getAtmTicketEvent(eq("testUser"), any(String.class)))
				.thenReturn(List.of(nonBreakdownEvent, breakdownEvent));

		AtmUpDownStatusAndDownTimeDto result = atmUpDownStatusAndDownTimeService.fetchAtmUpdownStatus(atmId);

		assertEquals("Down", result.getStatus());
		long expectedTotalMinutes = java.time.Duration.between(createdTime, LocalDateTime.now()).toMinutes();
		long expectedHours = expectedTotalMinutes / 60;
		long expectedMinutes = expectedTotalMinutes % 60;
		String expectedDowntime = String.format("%02dh %02dm", expectedHours, expectedMinutes);

		assertEquals(expectedDowntime, result.getDownTime());
	}

	@Test
	void fetchAtmUpdownStatus_emptyTicketDetailsInResponse_shouldReturnOperational() {
		String atmId = "ATM005";
		AtmDetailsWithTickets atmDetails = new AtmDetailsWithTickets();
		atmDetails.setAtmId(atmId);
		atmDetails.setOpenticketdetails(Collections.emptyList());

		OpenTicketsResponse mockResponse = new OpenTicketsResponse();
		mockResponse.setAtmdetails(List.of(atmDetails));

		when(synergyService.getOpenTicketDetails(anyList())).thenReturn(mockResponse);

		AtmUpDownStatusAndDownTimeDto result = atmUpDownStatusAndDownTimeService.fetchAtmUpdownStatus(atmId);

		assertEquals("Operational", result.getStatus());
		assertEquals("00h 00m", result.getDownTime());
	}

	@Test
	void fetchAtmUpdownStatus_parsingErrorInCreatedDate_shouldReturnErrorDowntime() {
		String atmId = "ATM008";
		// LocalDateTime createdTime = LocalDateTime.now().minusHours(1); // This line
		// is not directly used after the fix

		TicketDetailsDto ticket1 = new TicketDetailsDto();
		ticket1.setEquipmentid(atmId);
		ticket1.setSrno("SR001");
		ticket1.setEventcode("BREAKDOWN_EVENT");
		ticket1.setCreateddate("INVALID DATE FORMAT"); // Intentional invalid date

		AtmDetailsWithTickets atmDetails = new AtmDetailsWithTickets();
		atmDetails.setAtmId(atmId);
		atmDetails.setOpenticketdetails(List.of(ticket1));

		OpenTicketsResponse mockResponse = new OpenTicketsResponse();
		mockResponse.setAtmdetails(List.of(atmDetails));

		when(synergyService.getOpenTicketDetails(anyList())).thenReturn(mockResponse);

		AtmTicketEvent breakdownEvent = new AtmTicketEvent();
		breakdownEvent.setIsBreakdown(1);
		when(userAtmDetailsService.getAtmTicketEvent(eq("testUser"), any(String.class)))
				.thenReturn(List.of(breakdownEvent));

		AtmUpDownStatusAndDownTimeDto result = atmUpDownStatusAndDownTimeService.fetchAtmUpdownStatus(atmId);

		assertEquals("Down", result.getStatus());
		assertEquals("Error calculating downtime", result.getDownTime());
	}

}
