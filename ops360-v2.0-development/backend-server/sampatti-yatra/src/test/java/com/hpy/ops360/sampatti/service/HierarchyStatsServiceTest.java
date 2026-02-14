package com.hpy.ops360.sampatti.service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.HierarchyStatsDto;
import com.hpy.ops360.sampatti.entity.HierarchyStatsEntity;
import com.hpy.ops360.sampatti.repository.HierarchyStatsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HierarchyStatsServiceTest {

    @Mock
    private HierarchyStatsRepository repository;

    @Mock
    private LoginUtil loginUtil;

    @InjectMocks
    private HierarchyStatsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHierarchyStats() {
        // Given
        String mockUsername = "john.doe";
        HierarchyStatsEntity mockEntity = new HierarchyStatsEntity();
        mockEntity.setSrno(1L);
        mockEntity.setLevel("Zonal Head");
        mockEntity.setSuperiorUserId("super123");
        mockEntity.setTeamCount(5);
        mockEntity.setTotalCount(25);

        when(loginUtil.getLoggedInUserName()).thenReturn(mockUsername);
        when(repository.getHierarchyStats(mockUsername)).thenReturn(mockEntity);

        // When
        HierarchyStatsDto result = service.getHierarchyStats();

        // Then
        assertNotNull(result);
        assertEquals(1, result.getSrno());
        assertEquals("Zonal Head", result.getLevel());
        assertEquals("super123", result.getSuperiorUserId());
        assertEquals(5, result.getTeamCount());
        assertEquals(25, result.getTotalCount());

        verify(loginUtil, times(1)).getLoggedInUserName();
        verify(repository, times(1)).getHierarchyStats(mockUsername);
    }
}
