//package com.hpy.ops360.ticketing.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.hpy.ops360.ticketing.service.TicketAssetsService;
//
//@ExtendWith(MockitoExtension.class)
//class TicketAssetsControllerTest {
//
//	@Mock
//	private TicketAssetsService ticketAssetsService;
//
//	@InjectMocks
//	private TicketAssetsController ticketAssetsController;
//
//	@Test
//	void testHandleFileUpload() {
//		// Mocked remarkId and files
//		Long remarkId = 1L;
//		MockMultipartFile[] files = {
//				new MockMultipartFile("file1", "test1.txt", "text/plain", "Test file 1 content".getBytes()),
//				new MockMultipartFile("file2", "test2.txt", "text/plain", "Test file 2 content".getBytes()) };
//
//		// Mock service method to do nothing
//		doNothing().when(ticketAssetsService).uploadFiles(any(Long.class), any(MultipartFile[].class));
//
//		// Call controller method
//		ResponseEntity<String> responseEntity = ticketAssetsController.handleFileUpload(remarkId, files);
//
//		// Verify response status and message
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertEquals("Files uploaded and database updated successfully.", responseEntity.getBody());
//	}
//
//}
