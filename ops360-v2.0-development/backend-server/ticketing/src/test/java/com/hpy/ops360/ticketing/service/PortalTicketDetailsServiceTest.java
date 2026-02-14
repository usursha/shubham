package com.hpy.ops360.ticketing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketShortDetailsDto;
import com.hpy.ops360.ticketing.entity.AtmTicketEvent;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.AtmTicketEventRepository;

@ExtendWith(MockitoExtension.class)
class PortalTicketDetailsServiceTest {

	@InjectMocks
    private PortalTicketDetailsService ticketService;

    @Mock
    private SynergyService synergyService;

    @Mock
    private AtmTicketEventRepository atmTicketEventRepository;

    @Mock
    private LoginService loginService;

    @Test
    void testGetTicketTabDetails_success() {
        // Arrange
        TicketDetailsReqWithoutReqId request = new TicketDetailsReqWithoutReqId();
        request.setAtmid("BPCN105007");
        request.setTicketno("OPS-231024/299");

        TicketDetailsDto ticket = new TicketDetailsDto();
        ticket.setEquipmentid("ATM001");
        ticket.setSrno("SR123");
        ticket.setEventcode("EV123");
        ticket.setNextfollowup("nextFollow");
        ticket.setCalldate("2024-10-23 12:31:00");
        ticket.setCreateddate("23/10/2024 12:31");
        ticket.setStatus("Open");
        ticket.setSubcalltype("Data;ATM Transacting;");
        ticket.setVendor("HCMS");

        AtmTicketEvent event = new AtmTicketEvent();
        event.setCeName("vikash.shinde");
        event.setDownCall(1);
        event.setOwner("Verification Pending");
        event.setInternalRemark("ATM is Transcting ");
        event.setEtaDateTime(null); // Simulating empty string in result

        String uniqueCode = "ATM001|SR123|EV123|nextFollow|2024-10-23 12:31:00";

        // Mock behavior
        when(synergyService.getTicketDetails(request)).thenReturn(ticket);
        when(loginService.getLoggedInUser()).thenReturn("tester");
        when(atmTicketEventRepository.getAtmTicketEvent("tester", uniqueCode)).thenReturn(List.of(event));

        // Act
        TicketShortDetailsDto result = ticketService.getTicketTabDetails(request);

        // Assert
        assertEquals("vikash.shinde", result.getCeName());
        assertEquals("OPS-231024/299", result.getTicketNumber());
        assertEquals("BPCN105007", result.getAtmId());
        assertEquals("23 Oct 2024, 12:31 pm", result.getCreatedOn());
        assertEquals("Open", result.getStatus());
        assertEquals("Down", result.getTicketType());
        assertEquals("Verification Pending", result.getOwner());
        assertEquals("Data;ATM Transacting;", result.getSubcalltype());
        assertEquals("ATM is Transcting ", result.getInternalRemarks());
        assertEquals("", result.getLatestEtaDateTime());
        assertEquals("HCMS", result.getVendor());
    }
    
    @Test
    void testGetTicketTabDetails_whenTicketIsNull_shouldThrowException() {
        // Arrange
        TicketDetailsReqWithoutReqId request = new TicketDetailsReqWithoutReqId();
        request.setAtmid("ATM123");
        request.setTicketno("OPS-111");

        when(synergyService.getTicketDetails(request)).thenReturn(null);

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> ticketService.getTicketTabDetails(request)
        );

        assertEquals("Fetched ticket details cannot be null", exception.getMessage());
    }
    
    @Test
    void testGetTicketTabDetails_whenAtmEventListIsEmpty_shouldThrowException() {
        // Arrange
        TicketDetailsReqWithoutReqId request = new TicketDetailsReqWithoutReqId();
        request.setAtmid("ATM123");
        request.setTicketno("OPS-111");

        TicketDetailsDto ticket = new TicketDetailsDto();
        ticket.setEquipmentid("EQ1");
        ticket.setSrno("SR123");
        ticket.setEventcode("EVCODE1");
        ticket.setNextfollowup("Followup1");
        ticket.setCalldate("2024-01-01 10:00:00");
        ticket.setCreateddate("2024-01-01 10:00:00");

        when(synergyService.getTicketDetails(request)).thenReturn(ticket);
        when(loginService.getLoggedInUser()).thenReturn("testUser");
        when(atmTicketEventRepository.getAtmTicketEvent(eq("testUser"), anyString()))
            .thenReturn(Collections.emptyList());

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> ticketService.getTicketTabDetails(request)
        );

        assertTrue(exception.getMessage().contains("No AtmTicketEvent found for the given ticket"));
    }


}
