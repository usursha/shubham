package com.hpy.ops360.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.dashboard.dto.TicketCategoryDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedResponseAll;
import com.hpy.ops360.dashboard.entity.TicketsRaisedEntity;
import com.hpy.ops360.dashboard.repository.TicketRaisedRepository;
import com.hpy.ops360.dashboard.service.TicketsRaisedService;
import com.hpy.ops360.dashboard.util.Helper;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketsRaisedService Tests")
public class TicketsRaisedCategoriesTest {

	@Mock
	private TicketRaisedRepository ticketRaisedRepository;

	@Mock
	private Helper helper;

	@InjectMocks
	private TicketsRaisedService ticketsRaisedService;

	private TicketsRaisedEntity mockEntity;
	private String testUserId;

	@BeforeEach
	void setUp() {
		testUserId = "nitin.waghmare";

		mockEntity = new TicketsRaisedEntity();
		mockEntity.setTotalOpenTickets("5");
		mockEntity.setTodaysTicketColor("#FF5722");
	}

	@Nested
	@DisplayName("Happy Path Scenarios")
	class HappyPathTests {

		@Test
		@DisplayName("Should return 8 categories with dynamic down category when repository returns data")
		void shouldReturnCategoriesWithDynamicDownCategory() {
			// Given
			List<TicketsRaisedEntity> repositoryResult = Arrays.asList(mockEntity);
			when(helper.getLoggedInUser()).thenReturn(testUserId);
			when(ticketRaisedRepository.getRaisedTicketsCategories(testUserId)).thenReturn(repositoryResult);

			// When
			TicketsRaisedResponseAll result = ticketsRaisedService.getTicketCategoriesForUser();

			// Then
			assertNotNull(result, "Result should not be null");
			assertNotNull(result.getData(), "Category list should not be null");
			assertEquals(8, result.getData().size(), "Should return exactly 8 categories");

			// Verify down category (dynamic data from database)
			TicketCategoryDTO downCategory = result.getData().get(0);
			assertEquals("down", downCategory.getTitle(), "First category should be 'down'");
			assertEquals("5", downCategory.getValue(), "Down category count should match entity data");
			assertEquals("#FF5722", downCategory.getColor(), "Down category color should match entity data");

			// Verify static categories
			verifyStaticCategories(result.getData());

			// Verify interactions
			verify(helper, times(1)).getLoggedInUser();
			verify(ticketRaisedRepository, times(1)).getRaisedTicketsCategories(testUserId);
		}

		@Test
		@DisplayName("Should return empty list when repository returns empty list")
		void shouldReturnEmptyListWhenRepositoryReturnsEmptyList() {
			// Given
			when(helper.getLoggedInUser()).thenReturn(testUserId);
			when(ticketRaisedRepository.getRaisedTicketsCategories(testUserId)).thenReturn(Collections.emptyList());

			// When
			TicketsRaisedResponseAll result = ticketsRaisedService.getTicketCategoriesForUser();

			// Then
			assertNotNull(result);
			assertEquals(0, result.getData().size(), "Should return empty list when repository returns empty list");

			verify(helper, times(1)).getLoggedInUser();
			verify(ticketRaisedRepository, times(1)).getRaisedTicketsCategories(testUserId);
		}

		@Nested
		@DisplayName("Exception Handling")
		class ExceptionHandlingTests {

			@Test
			@DisplayName("Should propagate RuntimeException from helper.getLoggedInUser()")
			void shouldPropagateHelperException() {
				// Given
				RuntimeException expectedException = new RuntimeException("Authentication failed");
				when(helper.getLoggedInUser()).thenThrow(expectedException);

				// When & Then
				RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
					ticketsRaisedService.getTicketCategoriesForUser();
				});

				assertEquals("Authentication failed", thrownException.getMessage());
				verify(helper, times(1)).getLoggedInUser();
				verify(ticketRaisedRepository, never()).getRaisedTicketsCategories(any());
			}

			@ParameterizedTest
			@NullAndEmptySource
			@ValueSource(strings = { "   ", "invalid-color", "#", "#GGGGGG" })
			@DisplayName("Should handle invalid color values from entity")
			void shouldHandleInvalidColorValues(String invalidColor) {
				// Given
				mockEntity.setTodaysTicketColor(invalidColor);

				when(helper.getLoggedInUser()).thenReturn(testUserId);
				when(ticketRaisedRepository.getRaisedTicketsCategories(testUserId))
						.thenReturn(Arrays.asList(mockEntity));

				// When
				TicketsRaisedResponseAll result = ticketsRaisedService.getTicketCategoriesForUser();

				// Then
				TicketCategoryDTO downCategory = result.getData().get(0);
				assertEquals(invalidColor, downCategory.getColor(),
						"Should preserve the original color value even if invalid");
			}
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = { "   ", "\t", "\n" })
		@DisplayName("Should handle invalid count values from entity")
		void shouldHandleInvalidCountValues(String invalidCount) {
			// Given
			mockEntity.setTotalOpenTickets(invalidCount);
			mockEntity.setTodaysTicketColor("#FF5722");

			when(helper.getLoggedInUser()).thenReturn(testUserId);
			when(ticketRaisedRepository.getRaisedTicketsCategories(testUserId)).thenReturn(Arrays.asList(mockEntity));

			// When
			TicketsRaisedResponseAll result = ticketsRaisedService.getTicketCategoriesForUser();

			// Then
			TicketCategoryDTO downCategory = result.getData().get(0);
			assertEquals(invalidCount, downCategory.getValue(),
					"Should preserve the original count value even if invalid");
		}

		// Helper method to verify static categories
		private void verifyStaticCategories(List<TicketCategoryDTO> categoryList) {
			// Verify all static categories are present with correct data
			String[] expectedCategories = { "SiteVisit", "Adhoc", "QC", "Incident", "Recon", "ESS", "IOT" };
			String[] expectedColors = { "#00BCD4", "#8BC34A", "#FFCC02", "#FF9800", "#9C27B0", "#2196F3", "#737373" };

			for (int i = 0; i < expectedCategories.length; i++) {
				TicketCategoryDTO category = categoryList.get(i + 1); // +1 because down is at index 0
				assertEquals(expectedCategories[i], category.getTitle(),
						"Category at position " + (i + 1) + " should be " + expectedCategories[i]);
				assertEquals("0", category.getValue(), "Static category count should be 0");
				assertEquals(expectedColors[i], category.getColor(), "Category color should match expected value");
			}
		}
	}
}
