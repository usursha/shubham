package com.hpy.ops360.sampatti.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.sampatti.dto.GuidelinesDto;
import com.hpy.ops360.sampatti.entity.GuidelineDetails;
import com.hpy.ops360.sampatti.repository.GuidelineRepository;

@ExtendWith(MockitoExtension.class)
class GuidelineServiceTest {
	
    @InjectMocks
    private GuidelineService guidelineService;

    @Mock
    private GuidelineRepository guidelineRepo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Case
    @Test
    void saveGuidelinesDetailSaveSuccessfully() {
        String requestBody = "This is test content";
        GuidelineDetails saved = new GuidelineDetails();
        saved.setId(1L);
        saved.setContent(requestBody);

        when(guidelineRepo.save(any(GuidelineDetails.class))).thenReturn(saved);
        
        GuidelineDetails result = guidelineService.saveGuidelinesDetails(requestBody);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestBody, result.getContent());
        verify(guidelineRepo, times(1)).save(any(GuidelineDetails.class));
    }
	
	// Negative Test Case
    @Test
    void saveGuidelinesDetailsThrowRuntimeException() {
        String requestBody = "Failing content";
        when(guidelineRepo.save(any(GuidelineDetails.class)))
                .thenThrow(new RuntimeException("Database error"));
        
        Exception exception = assertThrows(RuntimeException.class, () ->
                guidelineService.saveGuidelinesDetails(requestBody));

        assertEquals("Database error", exception.getMessage());
        verify(guidelineRepo, times(1)).save(any(GuidelineDetails.class));
    }
	
	

    // Positive Test Case
    @Test
    void getGuidelinesDetailsReturnLatest() {
        GuidelineDetails latest = new GuidelineDetails();
        latest.setId(2L);
        latest.setContent("Latest content");

        when(guidelineRepo.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(latest));

        GuidelinesDto result = guidelineService.getGuidelinesDetails();
        assertNotNull(result);
        assertEquals("Latest content", result.getContent());
        verify(guidelineRepo, times(1)).findTopByOrderByCreatedAtDesc();
    }
    
 // Negative Test Case
    @Test
    void testGetGuidelinesDetails() {
        when(guidelineRepo.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.empty());
        
        GuidelinesDto result = guidelineService.getGuidelinesDetails();
        assertNotNull(result);
        assertEquals("", result.getContent());
        verify(guidelineRepo, times(1)).findTopByOrderByCreatedAtDesc();
    }

}