package com.hpy.ops360.sampatti.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hpy.ops360.sampatti.dto.LeaderboardMonthDto;
import com.hpy.ops360.sampatti.entity.LeaderboardMonthEntity;
import com.hpy.ops360.sampatti.repository.LeaderboardMonthRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaderboardMonthServiceTest {

    @InjectMocks
    private LeaderboardMonthService service;

    @Mock
    private LeaderboardMonthRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Scenario
    @Test
    void testGetLeaderboardMonthData_PositiveScenario() {
        List<LeaderboardMonthEntity> mockData = Arrays.asList(
            new LeaderboardMonthEntity(1L, "January"),
            new LeaderboardMonthEntity(2L, "February"),
            new LeaderboardMonthEntity(3L, "March")
        );

        when(repository.getLeaderboardMonthData()).thenReturn(mockData);

        List<LeaderboardMonthDto> result = service.getLeaderboardMonthData();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("January", result.get(0).getDisplayMonth());
        Assertions.assertEquals(1L, result.get(0).getSrno());
    }

    // Negative Scenario - Empty List
    @Test
    void testGetLeaderboardMonthData_EmptyList() {
        when(repository.getLeaderboardMonthData()).thenReturn(Collections.emptyList());

        List<LeaderboardMonthDto> result = service.getLeaderboardMonthData();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    // Negative Scenario - Null List
    @Test
    void testGetLeaderboardMonthData_NullList() {
        when(repository.getLeaderboardMonthData()).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> service.getLeaderboardMonthData());
    }

    // Edge Case - Unexpected Month Format
    @Test
    void testGetLeaderboardMonthData_InvalidMonthName() {
        List<LeaderboardMonthEntity> mockData = List.of(
            new LeaderboardMonthEntity(4L, "Month123")
        );

        when(repository.getLeaderboardMonthData()).thenReturn(mockData);

        List<LeaderboardMonthDto> result = service.getLeaderboardMonthData();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Month123", result.get(0).getDisplayMonth());
    }
}
