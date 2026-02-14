package com.hpy.ops360.sampatti.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.hpy.ops360.sampatti.dto.UserIncentiveMonthYearDto;
import com.hpy.ops360.sampatti.entity.UserIncentiveMonthYearEntity;
import com.hpy.ops360.sampatti.repository.UserIncentiveMonthYearRepository;

@ExtendWith(MockitoExtension.class)
class UserIncentiveMonthYearServiceTest {

	@InjectMocks
	private UserIncentiveMonthYearService service;

	@Mock
	private UserIncentiveMonthYearRepository repository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	// Positive case
	@Test
	void getIncentivesMonthYearSuccessfully() {
		UserIncentiveMonthYearEntity e1 = new UserIncentiveMonthYearEntity();
		e1.setMonthYear("Jan 2024");
		UserIncentiveMonthYearEntity e2 = new UserIncentiveMonthYearEntity();
		e2.setMonthYear("Feb 2024");

		when(repository.getUserIncentivesMonthYear()).thenReturn(Arrays.asList(e1, e2));
		List<UserIncentiveMonthYearDto> result = service.getIncentivesMonthYear();
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Jan 2024", result.get(0).getMonthyear());
		assertEquals("Feb 2024", result.get(1).getMonthyear());
		verify(repository, times(1)).getUserIncentivesMonthYear();
	}

	// Negative case - Empty list
	@Test
	void getIncentivesMonthYearReturnEmptyList() {
		when(repository.getUserIncentivesMonthYear()).thenReturn(Collections.emptyList());
		List<UserIncentiveMonthYearDto> result = service.getIncentivesMonthYear();
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(repository, times(1)).getUserIncentivesMonthYear();
	}

	// Negative case - Null result
	@Test
	void getIncentivesMonthYearNullReturnEmptyList() {
		when(repository.getUserIncentivesMonthYear()).thenReturn(null);
		List<UserIncentiveMonthYearDto> result = service.getIncentivesMonthYear();
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(repository, times(1)).getUserIncentivesMonthYear();
	}

	// Exception case -RuntimeException
//	@Test
//	void getIncentivesMonthYearExceptionThrownRuntimeException() {
//		when(repository.getUserIncentivesMonthYear()).thenThrow(new RuntimeException("Database failure"));
//		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//			service.getIncentivesMonthYear();
//		});
//
//		assertEquals("Failed to fetch incentive month-year data", exception.getMessage());
//		verify(repository, times(1)).getUserIncentivesMonthYear();
//	}
	


}