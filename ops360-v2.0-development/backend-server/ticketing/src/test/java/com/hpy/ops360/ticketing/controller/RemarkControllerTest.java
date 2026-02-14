//package com.hpy.ops360.ticketing.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import com.hpy.ops360.ticketing.dto.RemarkDto;
//import com.hpy.ops360.ticketing.service.RemarkService;
//
//@ExtendWith(MockitoExtension.class)
//class RemarkControllerTest {
//
//	@Mock
//	private RemarkService remarkService;
//
//	@InjectMocks
//	private RemarkController remarkController;
//
//	@Test
//	void testAddRemarks() throws Exception {
//
//		RemarkDto requestRemarkDto = new RemarkDto();
//		requestRemarkDto.setRemarkBy("sachin");
//		requestRemarkDto.setRemarks("ATM needs work asap");
//		requestRemarkDto.setTicketId(1L);
//
//		RemarkDto expectedRemarkDto = new RemarkDto();
//		expectedRemarkDto.setId(1L);
//		expectedRemarkDto.setRemarkBy("sachin");
//		expectedRemarkDto.setRemarks("ATM needs work asap");
//		expectedRemarkDto.setTicketId(1L);
//
//		when(remarkService.addRemark(requestRemarkDto)).thenReturn(expectedRemarkDto);
//
//		ResponseEntity<RemarkDto> response = remarkController.addRemarks(expectedRemarkDto);
//
//		assertEquals("sachin", expectedRemarkDto.getRemarkBy());
//		assertEquals("ATM needs work asap", expectedRemarkDto.getRemarks());
//
//	}
//
//	@Test
//	void testGetRemarksForTicket() {
//		Long ticketId = 1L;
//
//		RemarkDto r1 = new RemarkDto();
//		r1.setId(1L);
//		r1.setRemarkBy("sachin");
//		r1.setRemarks("ATM needs work asap");
//		r1.setTicketId(1L);
//
//		RemarkDto r2 = new RemarkDto();
//		r2.setId(1L);
//		r2.setRemarkBy("sagar");
//		r2.setRemarks("ATM needs work asap");
//		r2.setTicketId(1L);
//
//		RemarkDto r3 = new RemarkDto();
//		r3.setId(1L);
//		r3.setRemarkBy("deepak");
//		r3.setRemarks("ATM needs work asap");
//		r3.setTicketId(1L);
//
//		List<RemarkDto> expectedRemarkDtos = List.of(r1, r2, r3);
//		when(remarkService.getRemarksForTicket(ticketId)).thenReturn(expectedRemarkDtos);
//
//		ResponseEntity<List<RemarkDto>> response = remarkController.getRemarksForTicket(ticketId);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals("sachin", response.getBody().get(0).getRemarkBy());
//
//	}
//
//}
