package com.hpy.ops360.ticketing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hpy.ops360.ticketing.cm.dto.AllocationDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.AllocationDetailsRequestDto;
import com.hpy.ops360.ticketing.cm.entity.AllocationDetails;
import com.hpy.ops360.ticketing.cm.repo.AllocationDetailsRepository;
import com.hpy.ops360.ticketing.cm.service.AllocationDetailsService;

class AllocationDetailsServiceTest {

    @InjectMocks
    private AllocationDetailsService allocationDetailsService;

    @Mock
    private AllocationDetailsRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllocationDetails_ReturnsAllocationDetails() {
        AllocationDetailsRequestDto request = new AllocationDetailsRequestDto();
        request.setTicket_number("2025-6-API-06670259-S1");
        request.setAtm_id("BPRH291701");

        AllocationDetails allocationDetails = new AllocationDetails();
        allocationDetails.setSrno(1l);
        allocationDetails.setFinal_allocation_type("Follow Up");
   //    allocationDetails.setCreated_date("2023-10-01");
       allocationDetails.setCreated_date(Timestamp.valueOf("2023-01-15 10:00:00"));
        allocationDetails.setCreatefilter("19 June, 2025");
        allocationDetails.setAllocation_owner("Assigned to FLM");
        allocationDetails.setSubcall_type("FLM;BLM");
        allocationDetails.setFollow_up("FollowUp1");
        allocationDetails.setStatus("Active");

        when(repository.getallocationDetails(any(String.class), any(String.class)))
                .thenReturn(Collections.singletonList(allocationDetails));

        List<AllocationDetailsDto> result = allocationDetailsService.getAllocationDetails(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        AllocationDetailsDto dto = result.get(0);
        assertEquals(1, dto.getSrno());
        assertEquals("Follow Up", dto.getFinal_allocation_type());
        assertEquals("15 January '23, 10:00 am", dto.getCreated_date());
        assertEquals("19 June, 2025", dto.getCreatefilter());
        assertEquals("Assigned to FLM", dto.getAllocation_owner());
        assertEquals("FLM;BLM", dto.getSubcall_type());
        assertEquals("FollowUp1", dto.getFollow_up());
        assertEquals("Active", dto.getStatus());
    }

    @Test
    void testGetAllocationDetails_ReturnsEmptyList_WhenRepositoryReturnsNull() {
        AllocationDetailsRequestDto request = new AllocationDetailsRequestDto();
        request.setTicket_number("2025-6-API-06670259-S1");
        request.setAtm_id("BPRH291701");

        when(repository.getallocationDetails(any(String.class), any(String.class)))
                .thenReturn(null);

        List<AllocationDetailsDto> result = allocationDetailsService.getAllocationDetails(request);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllocationDetails_ReturnsEmptyList_WhenRepositoryReturnsEmptyList() {
        AllocationDetailsRequestDto request = new AllocationDetailsRequestDto();
        request.setTicket_number("2025-6-API-06670259-S1");
        request.setAtm_id("BPRH291701");

        when(repository.getallocationDetails(any(String.class), any(String.class)))
                .thenReturn(Collections.emptyList());

        List<AllocationDetailsDto> result = allocationDetailsService.getAllocationDetails(request);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
