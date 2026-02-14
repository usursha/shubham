//package com.hpy.ops360.ticketing.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import com.hpy.generic.Exception.EntityNotFoundException;
//import com.hpy.ops360.ticketing.dto.TicketDto;
//import com.hpy.ops360.ticketing.dto.TicketStatusDto;
//import com.hpy.ops360.ticketing.enums.TicketStatus;
//import com.hpy.ops360.ticketing.service.TicketService;
//
//@ExtendWith(MockitoExtension.class)
//class TicketControllerTest {
//
//	@Mock
//	private TicketService ticketService;
//
//	@InjectMocks
//	private TicketController ticketController;
//
//	@Test
//	void testCreateNewTicket() {
//		TicketDto requestTicketDto = new TicketDto();
//		requestTicketDto.setOwner("sachin");
//		TicketDto expectedTicketDto = new TicketDto();
//		expectedTicketDto.setId(1L);
//		expectedTicketDto.setStatus(TicketStatus.OPEN.toString());
//		expectedTicketDto.setOwner("sachin");
//		Mockito.when(ticketService.createNewTicket(requestTicketDto)).thenReturn(expectedTicketDto);
//
//		ResponseEntity<TicketDto> savedResponse = ticketController.createNewTicket(requestTicketDto);
//		System.out.println("savedResponse:" + savedResponse.getBody());
//		assertEquals(HttpStatus.OK, savedResponse.getStatusCode());
//		assertEquals(expectedTicketDto.getOwner(), savedResponse.getBody().getOwner());
//	}
//
//	@Test
//	void testGetTicketsByStatus() {
//		String status = "OPEN";
//		TicketDto t1 = new TicketDto();
//		t1.setOwner("sachin");
//		t1.setStatus("OPEN");
//
//		TicketDto t2 = new TicketDto();
//		t2.setOwner("sagar");
//		t2.setStatus("OPEN");
//
//		TicketDto t3 = new TicketDto();
//		t3.setOwner("deepak");
//		t3.setStatus("Inprogress");
//
//		List<TicketDto> tickets = List.of(t1, t2, t3);
//
//		List<TicketDto> expectedTickets = tickets.stream().filter(ticketDto -> ticketDto.getStatus().equals(status))
//				.collect(Collectors.toList());
//
//		Mockito.when(ticketService.getTicketsByStatus(status)).thenReturn(expectedTickets);
//
//		// Act
//		ResponseEntity<List<TicketDto>> response = ticketController.getTicketsByStatus(status);
//
//		System.out.println("status code:" + response.getStatusCode());
//		System.out.println("body:" + response.getBody());
//		// Assert
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals("OPEN", response.getBody().get(0).getStatus());
//	}
//
//	@Test
//	void testGetTicketById_ReturnsTicket() throws EntityNotFoundException {
//		Long ticketId = 1L;
//
//		TicketDto expectedTickets = new TicketDto();
//		expectedTickets.setId(ticketId);
//		expectedTickets.setTicketNumber("INFRA1");
//		expectedTickets.setOwner("sachin");
//		expectedTickets.setStatus("OPEN");
//
//		when(ticketService.getTicketById(ticketId)).thenReturn(expectedTickets);
//
//		ResponseEntity<TicketDto> response = ticketController.getTicketById(ticketId);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals("OPEN", response.getBody().getStatus());
//		assertEquals("sachin", response.getBody().getOwner());
//		assertEquals("INFRA1", response.getBody().getTicketNumber());
//	}
//
//	@Test
//	void testGetTicketById_TicketNotFound() throws EntityNotFoundException {
//		// Arrange
//		Long ticketId = 1L;
//		when(ticketService.getTicketById(ticketId)).thenThrow(EntityNotFoundException.class);
//
//		// Act
//		ResponseEntity<TicketDto> response = ticketController.getTicketById(ticketId);
//
//		// Assert
//		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	}
//
//	@Test
//	void testGetAllTickets() {
//
//		TicketDto t1 = new TicketDto();
//		t1.setOwner("sachin");
//		t1.setStatus("OPEN");
//
//		TicketDto t2 = new TicketDto();
//		t2.setOwner("sagar");
//		t2.setStatus("OPEN");
//
//		TicketDto t3 = new TicketDto();
//		t3.setOwner("deepak");
//		t3.setStatus("Inprogress");
//
//		List<TicketDto> expectedTickets = List.of(t1, t2, t3);
//
//		when(ticketService.getAllTickets()).thenReturn(expectedTickets);
//
//		ResponseEntity<List<TicketDto>> response = ticketController.getAllTickets();
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(3, response.getBody().size());
//		assertEquals(t1, response.getBody().get(0));
//	}
//
//	@Test
//	void testUpdateTicketStatus_ReturnsUpdatedTicket() throws EntityNotFoundException {
//
//		TicketStatusDto ticketStatusDto = new TicketStatusDto();
//		ticketStatusDto.setTicketId(1L);
//		ticketStatusDto.setStatus("CLOSED");
//
//		TicketDto expectedTicketDto = new TicketDto();
//		expectedTicketDto.setId(1L);
//		expectedTicketDto.setStatus("CLOSED");
//
//		when(ticketService.updateTicketStatus(ticketStatusDto)).thenReturn(expectedTicketDto);
//
//		ResponseEntity<TicketDto> response = ticketController.updateTicketStatus(ticketStatusDto);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(expectedTicketDto.getStatus(), response.getBody().getStatus());
//	}
//
//	@Test
//	void testUpdateTicketStatus_WhenEntityNotFoundException() throws EntityNotFoundException {
//		TicketStatusDto ticketStatusDto = new TicketStatusDto();
//		ticketStatusDto.setTicketId(1L);
//		ticketStatusDto.setStatus("CLOSED");
//
//		when(ticketService.updateTicketStatus(ticketStatusDto)).thenThrow(EntityNotFoundException.class);
//
//		ResponseEntity<TicketDto> response = ticketController.updateTicketStatus(ticketStatusDto);
//
//		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//	}
//
//}
