package com.hpy.ops360.atmservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.atmservice.dto.TransactionSummaryResponse;
import com.hpy.ops360.atmservice.dto.WeekSummaryDto;
import com.hpy.ops360.atmservice.entity.TransactionSummaryEntity;
import com.hpy.ops360.atmservice.repository.TransactionSummaryRepository;
import com.hpy.ops360.atmservice.service.TransactionSummaryService;

@ExtendWith(MockitoExtension.class)
class TransactionSummaryServiceTest {

    @Mock
    private TransactionSummaryRepository repository;

    @InjectMocks
    private TransactionSummaryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFormattedSummary() {
        // Arrange
        TransactionSummaryEntity monthEntity = new TransactionSummaryEntity();
        monthEntity.setSrno(1);
        monthEntity.setSection("MonthSummary");
        monthEntity.setAchieved(100);
        monthEntity.setTarget(200);

        TransactionSummaryEntity weekEntity = new TransactionSummaryEntity();
        weekEntity.setSrno(2);
        weekEntity.setSection("WeekSummary");
        weekEntity.setNum(1);
        weekEntity.setName("Monday");
        weekEntity.setAchieved(50);
        weekEntity.setTarget(100);

        List<TransactionSummaryEntity> mockData = Arrays.asList(monthEntity, weekEntity);
        when(repository.getTransactionSummary("CM", "user123", "2025-06")).thenReturn(mockData);

        // Act
        TransactionSummaryResponse response = service.getFormattedSummary("CM", "user123", "2025-06");

        // Assert
        assertNotNull(response);
        assertNotNull(response.getMonthSummary());
        assertEquals("MonthSummary", response.getMonthSummary().getSection());
        assertEquals("1", response.getMonthSummary().getSrno());

        Map<String, List<WeekSummaryDto>> weeklySummaries = response.getWeeklySummaries();
        assertTrue(weeklySummaries.containsKey("week 1"));
        assertEquals(1, weeklySummaries.get("week 1").size());
        assertEquals("Monday", weeklySummaries.get("week 1").get(0).getWeekdayName());

        verify(repository, times(1)).getTransactionSummary("CM", "user123", "2025-06");
    }
    
    @Test
    void testGetFormattedSummaryWithEmptyData() {
        when(repository.getTransactionSummary("CM", "user123", "2024-11-30")).thenReturn(List.of());

        TransactionSummaryResponse response = service.getFormattedSummary("CM", "user123", "2024-11-30");

        assertNotNull(response);
        assertNull(response.getMonthSummary());
        assertTrue(response.getWeeklySummaries().isEmpty());
    }
    
    @Test
    void testGetFormattedSummaryWithInvalidUserType() {
        when(repository.getTransactionSummary("INVALID", "user123", "2024-11-30")).thenReturn(List.of());

        TransactionSummaryResponse response = service.getFormattedSummary("INVALID", "user123", "2024-11-30");

        assertNotNull(response);
        assertNull(response.getMonthSummary());
        assertTrue(response.getWeeklySummaries().isEmpty());
    }

}
