package com.hpy.ops360.sampatti.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.sampatti.dto.FilterDataRequestDto;
import com.hpy.ops360.sampatti.dto.FilterDataResponseDto;
import com.hpy.ops360.sampatti.entity.FilterStateCityData;
import com.hpy.ops360.sampatti.entity.SortFilterData;
import com.hpy.ops360.sampatti.repository.FilterStateCityDataRepostiory;
import com.hpy.ops360.sampatti.repository.SortFilterDataRepostiory;
import com.hpy.ops360.sampatti.repository.ZoneDataRepository;

@ExtendWith(MockitoExtension.class)
class FilterDataServiceTest {
	
	@InjectMocks
	private FilterDataService filterDataService;
	
	@Mock
	private FilterStateCityDataRepostiory filterStateCityDataRepostiory;
	
	@Mock
	private ZoneDataRepository zoneDataRepository;
	
	@Mock
	private SortFilterDataRepostiory sortFilterDataRepostiory;

	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
    void testGetLeaderBoardCeFilterData() {
        // Mock request DTO
        FilterDataRequestDto requestDto = new FilterDataRequestDto();
        requestDto.setSelectedZone("Zone1");
        requestDto.setSelectedState("State1");
        requestDto.setSelectedCity("City1");
        requestDto.setRole("ce");

        // Mock data from repositories
        List<FilterStateCityData> mockFilterData = List.of(
            new FilterStateCityData(1L,1L, "ZONE", "Zone1", 10),
            new FilterStateCityData(2L, 2L, "STATE", "State1", 20),
            new FilterStateCityData(3L, 3L, "CITY", "City1", 30)
        );

        when(filterStateCityDataRepostiory.getFilterStateCityData(
            requestDto.getSelectedZone(),
            requestDto.getSelectedState(),
            requestDto.getSelectedCity(),
            requestDto.getRole()
        )).thenReturn(mockFilterData);

        List<SortFilterData> mockSortData = Arrays.asList(
            new SortFilterData(1L, "Sort1"),
            new SortFilterData(2L, "Sort2")
        );

        when(sortFilterDataRepostiory.findAll()).thenReturn(mockSortData);

        // Execute service method
        FilterDataResponseDto response = filterDataService.getLeaderBoardCeFilterData(requestDto);

        // Validate response
        assertNotNull(response);
        assertEquals(2, response.getSortingData().size());
        assertEquals(1, response.getZoneDataList().size());
        assertEquals(1, response.getStateDtoList().size());
        assertEquals(1, response.getCityDataDtoList().size());

        // Verify interactions with mocks
        verify(filterStateCityDataRepostiory, times(1)).getFilterStateCityData(
            requestDto.getSelectedZone(),
            requestDto.getSelectedState(),
            requestDto.getSelectedCity(),
            requestDto.getRole()
        );
        verify(sortFilterDataRepostiory, times(1)).findAll();
    }
	
	@Test
    void testGetLeaderBoardCeFilterData_withEmptyRepositoryData() {
        // Negative scenario: both repositories return empty lists.
        FilterDataRequestDto requestDto = new FilterDataRequestDto();
        requestDto.setSelectedZone("ZoneX");
        requestDto.setSelectedState("StateX");
        requestDto.setSelectedCity("CityX");
        requestDto.setRole("CE");
        
        when(filterStateCityDataRepostiory.getFilterStateCityData(anyString(), anyString(), anyString(),anyString()))
             .thenReturn(Collections.emptyList());
        when(sortFilterDataRepostiory.findAll()).thenReturn(Collections.emptyList());
             
        FilterDataResponseDto response = filterDataService.getLeaderBoardCeFilterData(requestDto);
    
        // Assert that all lists in the response are empty.
        assertNotNull(response);
        assertEquals(0, response.getZoneDataList().size());
        assertEquals(0, response.getStateDtoList().size());
        assertEquals(0, response.getCityDataDtoList().size());
        assertEquals(0, response.getSortingData().size());
    }
    
    @Test
    void testGetLeaderBoardCeFilterData_withInvalidRecordTypes() {
        // Negative scenario: repository returns records with unknown/invalid record types.
        FilterDataRequestDto requestDto = new FilterDataRequestDto();
        requestDto.setSelectedZone("ZoneX");
        requestDto.setSelectedState("StateX");
        requestDto.setSelectedCity("CityX");
        requestDto.setRole("ce");
    
        List<FilterStateCityData> mockData = List.of(
            new FilterStateCityData(10L, 10L, "UNKNOWN", "Data1", 0),
            new FilterStateCityData(11L, 11L, "INVALID", "Data2", 0)
        );
    
        when(filterStateCityDataRepostiory.getFilterStateCityData(anyString(), anyString(), anyString(), anyString()))
             .thenReturn(mockData);
    
        // Provide valid sort data to check that only the invalid records are filtered out.
        when(sortFilterDataRepostiory.findAll()).thenReturn(Arrays.asList(new SortFilterData(10L, "SortX")));
    
        FilterDataResponseDto response = filterDataService.getLeaderBoardCeFilterData(requestDto);
    
        // Since none of the records match 'ZONE', 'STATE', or 'CITY', these DTO lists should be empty.
        assertNotNull(response);
        assertEquals(0, response.getZoneDataList().size());
        assertEquals(0, response.getStateDtoList().size());
        assertEquals(0, response.getCityDataDtoList().size());
        // The sort list should still have data.
        assertEquals(1, response.getSortingData().size());
    }
    
    @Test
    void testGetLeaderBoardCeFilterData_whenFilterDataIsNull() {
        // Negative scenario: the filter repository returns a null list.
        FilterDataRequestDto requestDto = new FilterDataRequestDto();
        requestDto.setSelectedZone("Zone1");
        requestDto.setSelectedState("State1");
        requestDto.setSelectedCity("City1");
        requestDto.setRole("ce");
        
        when(filterStateCityDataRepostiory.getFilterStateCityData(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(null);
        when(sortFilterDataRepostiory.findAll()).thenReturn(Arrays.asList(new SortFilterData(10L, "SortX")));
    
        // Expect a NullPointerException due to calling stream() on a null value.
        assertThrows(NullPointerException.class, () -> {
            filterDataService.getLeaderBoardCeFilterData(requestDto);
        });
    }
    
    @Test
    void testGetLeaderBoardCeFilterData_whenSortDataIsNull() {
        // Negative scenario: the sort repository returns a null list.
        FilterDataRequestDto requestDto = new FilterDataRequestDto();
        requestDto.setSelectedZone("Zone1");
        requestDto.setSelectedState("State1");
        requestDto.setSelectedCity("City1");
        requestDto.setRole("ce");
        
        List<FilterStateCityData> mockFilterData = List.of(
            new FilterStateCityData(1L, 1L, "ZONE", "Zone1", 10)
        );
        
        when(filterStateCityDataRepostiory.getFilterStateCityData(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(mockFilterData);
        when(sortFilterDataRepostiory.findAll()).thenReturn(null);
    
        // Expect a NullPointerException when sort filter data (null) is processed.
        assertThrows(NullPointerException.class, () -> {
            filterDataService.getLeaderBoardCeFilterData(requestDto);
        });
    }
}
