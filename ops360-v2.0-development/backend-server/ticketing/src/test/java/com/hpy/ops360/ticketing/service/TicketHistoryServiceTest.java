//package com.hpy.ops360.ticketing.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//
//import com.hpy.ops360.ticketing.cm.dto.TicketHistoryDto;
//import com.hpy.ops360.ticketing.cm.dto.TicketHistoryGroupDto;
//import com.hpy.ops360.ticketing.cm.dto.TicketHistoryRequestDto;
//import com.hpy.ops360.ticketing.cm.dto.TicketHistoryResponseDto;
//import com.hpy.ops360.ticketing.cm.entity.TicketHistoryEntity;
//import com.hpy.ops360.ticketing.cm.repo.TicketHistoryRepository;
//import com.hpy.ops360.ticketing.cm.service.TicketHistoryService;
//
//@ExtendWith(MockitoExtension.class)
//public class TicketHistoryServiceTest {
//
//	@Mock
//	private TicketHistoryRepository ticketHistoryRepository;
//	
//	@Mock
//	private Logger log;
//	
//	@InjectMocks
//	private TicketHistoryService ticketHistoryService;
//	
//	private TicketHistoryRequestDto requestDto;
//	
//	  @BeforeEach
//	    void setUp() {
//	        requestDto = new TicketHistoryRequestDto();
//	        requestDto.setCm_user_id("user123");
//	        requestDto.setPageNumber(1);
//	        requestDto.setPageSize(10);
//	        requestDto.setFlag("0");
//	        requestDto.setSearchText("");
//	    }
//	  
//	  @Test
//	    void testGetTicketHistory_Success() {
//	        // Arrange
//	        List<TicketHistoryEntity> mockEntities = new ArrayList<>();
//	        TicketHistoryEntity entity = new TicketHistoryEntity();
//	        entity.setRno(1L);
//	        entity.setSrno("2024-12-API-02980278-S12");
//	        entity.setCustomer("axis-BNA");
//	        entity.setEquipmentId("CPRH03606");
//	        entity.setModel("SR7500");
//	        entity.setAtmCategory("Gold");
//	        entity.setAtmClassification("Metro");
//	        entity.setCallDate("2024-12-09 19:41:00");
//	        entity.setCreatedDate("2024-12-09 19:43:00");
//	        entity.setCallType("Assistance");
//	        entity.setSubCallType("HW;SLM;Hitachi");
//	        entity.setCompletionDateWithTime("2024-12-10 12:12:00.0");
//	        entity.setDowntime("16 hrs 31 mins");
//	        entity.setVendor("SIPL");
//	        entity.setServiceCode("FLM");
//	        entity.setDiagnosis("(Base24) - Balancing Started For The Terminal;\rClose: BALANCE COMPLETE FOR THE TERMINAL");
//	        entity.setEventCode("Balancing Started For The Terminal-1");
//	        entity.setHelpdeskName("Ahmedabad");
//	        entity.setLastAllocatedTime("2024-12-09 19:43:49.023");
//	        entity.setStatus("Close");
//	        entity.setRo("West");
//	        entity.setSite("OnSite");
//	        entity.setAddress("Panvel raje complex, plot no, 198 a, shivaji chowk, panvel 410206, maharashtra");
//	        entity.setCity("Pre-PANVEL");
//	        entity.setLocationName("Mangalore");
//	        entity.setState("Maharashtra");
//	        entity.setInsertDate("2025-07-28 17:38:32.927");
//	        entity.setCe_user_id("mahesh.patil");
//	        entity.setRemark("CCM0019781305 // As discussed to cra Nilesh 9561212919 site attend 7pm");
//	        entity.setAtm_address("PANVEL : RAJE COMPLEX, PLOT NO. 198 A, SHIVAJI CHOWK, PANVEL 410 206. MAHARASHTRA.");
//	        entity.setAtm_status("Down");
//	        entity.setFlag_status("0");
//	        entity.setTotal_records("955");
//	        mockEntities.add(entity);
//
//	        when(ticketHistoryRepository.getTicketHistoryDetails(anyString(), anyInt(), anyInt(), anyString(), anyString()))
//	                .thenReturn(mockEntities);
//
//	        // Act
//	        TicketHistoryDto response = ticketHistoryService.getTicketHistory(requestDto);
//
//	        // Assert
//	        assertNotNull(response);
//	        assertEquals(955, response.getTotalCount());
//	        assertEquals(1, response.getGroupedTicketHistory().size());
//	        TicketHistoryGroupDto group = response.getGroupedTicketHistory().get(0);
//	        assertEquals("10 Dec 2024", group.getDate());
//	        assertEquals(1, group.getTicketData().size());
//	        TicketHistoryResponseDto ticket = group.getTicketData().get(0);
//	        assertEquals("2024-12-API-02980278-S12", ticket.getTicketNumber());
//	        assertEquals("axis-BNA", ticket.getBank());
//	        assertEquals("CPRH03606", ticket.getEquipmentId());
//	        assertEquals("16 hrs 31 mins", ticket.getDowntimeInMins());
//	        verify(log, times(1)).info(eq("Ticket History Request: {}"), eq(requestDto));
//	        verify(log, times(1)).info(eq("Fetch ticket history details: {}"), eq(mockEntities));
//	        verify(log, times(1)).info(eq("Total count of data is: {}"), eq(955));
//	    }
//	  
//	    @Test
//	    void testGetTicketHistory_EmptyList() {
//	        // Arrange
//	        when(ticketHistoryRepository.getTicketHistoryDetails(anyString(), anyInt(), anyInt(), anyString(), anyString()))
//	                .thenReturn(new ArrayList<>());
//
//	        // Act
//	        TicketHistoryDto response = ticketHistoryService.getTicketHistory(requestDto);
//
//	        // Assert
//	        assertNotNull(response);
//	        assertEquals(0, response.getTotalCount());
//	        assertTrue(response.getGroupedTicketHistory().isEmpty());
//	        verify(log, times(1)).info(eq("Ticket History Request: {}"), eq(requestDto));
//	        verify(log, times(1)).info(eq("Fetch ticket history details: {}"), anyList());
//	        verify(log, times(1)).info(eq("Total count of data is: {}"), eq(0));
//	    }
//	    
////	    @Test
////	    void testGetTicketHistory_InvalidCompletionDate() {
////	        // Arrange
////	        List<TicketHistoryEntity> mockEntities = new ArrayList<>();
////	        TicketHistoryEntity entity = new TicketHistoryEntity();
////	        entity.setRno(1L);
////	        entity.setSrno("2024-12-API-02980278-S12");
////	        entity.setCompletionDateWithTime("invalid-date");
////	        entity.setTotal_records("100");
////	        mockEntities.add(entity);
////
////	        when(ticketHistoryRepository.getTicketHistoryDetails(anyString(), anyInt(), anyInt(), anyString(), anyString()))
////	                .thenReturn(mockEntities);
////
////	        // Act
////	        TicketHistoryDto response = ticketHistoryService.getTicketHistory(requestDto);
////
////	        // Assert
////	        assertNotNull(response);
////	        assertEquals(100, response.getTotalCount());
////	        assertEquals(1, response.getGroupedTicketHistory().size());
////	        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")), response.getGroupedTicketHistory().get(0).getDate());
////	        verify(log, times(1)).error(eq("Error parsing completion date: {}. Skipping entity."), eq("invalid-date"), any(Exception.class));
////	        verify(log, times(1)).info(eq("Total count of data is: {}"), eq(100));
////	    }
//}
