package com.hpy.ops360.ticketing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.ticketing.dto.AllFormattedETADatesReqDTO;
import com.hpy.ops360.ticketing.dto.AllFormattedETADatesResponseDTO;
import com.hpy.ops360.ticketing.entity.AllFormattedETADatesEntity;
import com.hpy.ops360.ticketing.repository.AllFormattedETADatesRepository;
import com.hpy.ops360.ticketing.service.AllFormattedETADatesService;

@ExtendWith(MockitoExtension.class)
public class AllFormattedETADatesTest {
	
	
	 @Mock
	 private AllFormattedETADatesRepository allFormattedETADatesRepository;
	 
	 @InjectMocks
	private  AllFormattedETADatesService allFormattedETADatesService;
	 
	 
	
	  @Test
	    void testAllFormattedETADatesDropdownList_Success() {
	        String ticketNumber = "Ticket_TEST_001"; 
	        String atmId = "AtmId_TEST_001"; 

	        // Create the request DTO instance
	        AllFormattedETADatesReqDTO requestDTO = new AllFormattedETADatesReqDTO();
	        requestDTO.setTicketNumber(ticketNumber);
	        requestDTO.setAtmId(atmId);

	        List<AllFormattedETADatesEntity> mockEntities = Arrays.asList(
	                new AllFormattedETADatesEntity("1", "28 Jan 2025,11:24 AM"),
	                new AllFormattedETADatesEntity("2", "30 Jan 2025,08:60 AM"),
	                new AllFormattedETADatesEntity("3", "31 Jan 2025,11:24 PM")
	        );

	      
	        Mockito.when(allFormattedETADatesRepository.getAllFormattedETADates(atmId, ticketNumber)).thenReturn(mockEntities);

	        // Call the service method with the created request DTO
	        List<AllFormattedETADatesResponseDTO> result = allFormattedETADatesService.getAllFormattedETADates(requestDTO);

	        assertNotNull(result);
	        assertEquals(3, result.size());
	        assertEquals("28 Jan 2025,11:24 AM", result.get(0).getFormattedEtaDateTime());
	        assertEquals("30 Jan 2025,08:60 AM", result.get(1).getFormattedEtaDateTime());
	        assertEquals("31 Jan 2025,11:24 PM", result.get(2).getFormattedEtaDateTime());

	        // Verify the repository method was called with the correct arguments
	        Mockito.verify(allFormattedETADatesRepository, Mockito.times(1)).getAllFormattedETADates(atmId, ticketNumber);
	    
	}

	  @Test
	    void testAllFormattedETADatesDropdownList_EmptyResponse() {

	        String ticketNumber = "No_Ticket_TEST_001";
	        String atmId = "No_AtmId_TEST_001";

	        // Create the request DTO
	        AllFormattedETADatesReqDTO requestDTO = new AllFormattedETADatesReqDTO();
	        requestDTO.setTicketNumber(ticketNumber);
	        requestDTO.setAtmId(atmId);

	        // Mock the repository to return an empty list
	        List<AllFormattedETADatesEntity> mockEntities = Collections.emptyList(); // More explicit for empty list

	        // Ensure the arguments here match how the service calls the repository (atmId, ticketNumber)
	        Mockito.when(allFormattedETADatesRepository.getAllFormattedETADates(atmId, ticketNumber)).thenReturn(mockEntities);

	        // Call the service method with the created request DTO
	        List<AllFormattedETADatesResponseDTO> result = allFormattedETADatesService.getAllFormattedETADates(requestDTO);

	        assertNotNull(result);
	        assertTrue(result.isEmpty());
	        assertEquals(0, result.size());

	        // Verify the repository method was called with the correct arguments
	        Mockito.verify(allFormattedETADatesRepository, Mockito.times(1)).getAllFormattedETADates(atmId, ticketNumber);
	    }

	    @Test
	    void testAllFormattedETADatesDropdownList_NullParameters() { // Renamed for clarity

	        String ticketNumber = null;
	        String atmId = null;

	        // Create the request DTO with null values
	        AllFormattedETADatesReqDTO requestDTO = new AllFormattedETADatesReqDTO();
	        requestDTO.setTicketNumber(ticketNumber); // Will set null
	        requestDTO.setAtmId(atmId);             // Will set null

	        // Mock entity and expected formatted date/time
	        // Corrected the mock entity to return something you intend to assert against
	        List<AllFormattedETADatesEntity> mockEntities = Arrays.asList(
	                new AllFormattedETADatesEntity("1", "Some Null Case Date,12:00 PM") // Example value
	        );

	        // Mock the repository call with null arguments (matching what the service would pass)
	        // Ensure the arguments here match how the service calls the repository (atmId, ticketNumber)
	        Mockito.when(allFormattedETADatesRepository.getAllFormattedETADates(atmId, ticketNumber)).thenReturn(mockEntities);

	        // Call the service method with the created request DTO
	        List<AllFormattedETADatesResponseDTO> result = allFormattedETADatesService.getAllFormattedETADates(requestDTO);

	        assertNotNull(result);
	        assertEquals(1, result.size());
	        // Assert against the value you actually put in the mock entity
	        assertEquals("Some Null Case Date,12:00 PM", result.get(0).getFormattedEtaDateTime());

	        // Verify the repository method was called with the correct (null) arguments
	        Mockito.verify(allFormattedETADatesRepository, Mockito.times(1)).getAllFormattedETADates(atmId, ticketNumber);
	    }

}
