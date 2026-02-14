package com.hpy.ops360.sampatti.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import com.hpy.ops360.sampatti.dto.FilterUserMasterRequestDto;
import com.hpy.ops360.sampatti.dto.FilteredResponseDto;
import com.hpy.ops360.sampatti.entity.FilteredItemEntity;
import com.hpy.ops360.sampatti.entity.SortFilterDataEntity;
import com.hpy.ops360.sampatti.entity.TargetRange;
import com.hpy.ops360.sampatti.repository.FilterUserMasterRepository;
import com.hpy.ops360.sampatti.repository.SortFilterDataRepository;
import com.hpy.ops360.sampatti.repository.TargetRangeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FilterUserMasterServiceTest {

    @Mock
    private FilterUserMasterRepository repository;

    @Mock
    private SortFilterDataRepository sortFilterDataRepository;

    @Mock
    private TargetRangeRepository rangeRepository;

    @InjectMocks
    private FilterUserMasterService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetfilterData_withValidData() {
        // Mock input DTO
        FilterUserMasterRequestDto requestDto = new FilterUserMasterRequestDto();
        requestDto.setZoneList("ZoneA");
        requestDto.setStateList("StateA");
        requestDto.setCityList("CityA");
        requestDto.setDesignation("designationA");

        // Mock FilteredItemEntity list
        List<FilteredItemEntity> filteredEntities = Arrays.asList(
                createFilteredItem("ZONE", "Z1", "Zone One"),
                createFilteredItem("STATE", "S1", "State One"),
                createFilteredItem("CITY", "C1", "City One")
        );

        when(repository.getFilteredUserMaster("ZoneA", "StateA", "CityA","designationA"))
                .thenReturn(filteredEntities);

        // Mock sort filter data
        List<SortFilterDataEntity> sortList = Collections.singletonList(
                createSortFilterEntity(1L, "Sort A")
        );
        when(sortFilterDataRepository.findAll()).thenReturn(sortList);

        // Mock target range
        TargetRange range = new TargetRange(1, 5, 10);
        when(rangeRepository.findFirstByOrderBySrnoAsc()).thenReturn(Optional.of(range));

        // Act
        FilteredResponseDto response = service.getfilterData(requestDto);

        // Assert
        assertEquals(1, response.getZoneList().size());
        assertEquals("Z1", response.getZoneList().get(0).getId());

        assertEquals(1, response.getStateList().size());
        assertEquals("S1", response.getStateList().get(0).getId());

        assertEquals(1, response.getCityList().size());
        assertEquals("C1", response.getCityList().get(0).getId());

        assertEquals(1, response.getSortingData().size());
        assertEquals("Sort A", response.getSortingData().get(0).getFilterData());

        assertEquals(5, response.getMinMaxValues().getMinAchieved());
        assertEquals(10, response.getMinMaxValues().getMaxAchieved());
    }

    @Test
    void testGetfilterData_withNoData() {
        FilterUserMasterRequestDto requestDto = new FilterUserMasterRequestDto();
        requestDto.setZoneList("");
        requestDto.setStateList("");
        requestDto.setCityList("");
        requestDto.setDesignation("");

        when(repository.getFilteredUserMaster("", "", "","")).thenReturn(Collections.emptyList());
        when(sortFilterDataRepository.findAll()).thenReturn(Collections.emptyList());
        when(rangeRepository.findFirstByOrderBySrnoAsc()).thenReturn(Optional.empty());

        FilteredResponseDto response = service.getfilterData(requestDto);

        assertEquals(0, response.getZoneList().size());
        assertEquals(0, response.getStateList().size());
        assertEquals(0, response.getCityList().size());
        assertEquals(0, response.getSortingData().size());
        assertEquals(null, response.getMinMaxValues().getMinAchieved());
        assertEquals(null, response.getMinMaxValues().getMaxAchieved());
    }

    // --- Helper methods ---
    private FilteredItemEntity createFilteredItem(String type, String id, String name) {
        FilteredItemEntity entity = new FilteredItemEntity();
        entity.setRecordType(type);
        entity.setId(id);
        entity.setName(name);
        entity.setSrno(1L);
        return entity;
    }

    private SortFilterDataEntity createSortFilterEntity(Long id, String sortData) {
        SortFilterDataEntity entity = new SortFilterDataEntity();
        entity.setSortId(id);
        entity.setFilterData(sortData);
        return entity;
    }
}
