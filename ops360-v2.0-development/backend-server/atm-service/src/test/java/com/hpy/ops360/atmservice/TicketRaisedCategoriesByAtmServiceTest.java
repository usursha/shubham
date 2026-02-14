package com.hpy.ops360.atmservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.TicketRaisedCategoriesByAtm;
import com.hpy.ops360.atmservice.repository.TicketRaisedCategoriesByAtmRepository;
import com.hpy.ops360.atmservice.request.TicketRaisedCategoriesByAtmRequestDto;
import com.hpy.ops360.atmservice.response.TicketRaisedCategoriesByAtmResponseDto;
import com.hpy.ops360.atmservice.response.TicketRaisedResponseByAtm;
import com.hpy.ops360.atmservice.service.TicketRaisedCategoriesByAtmService;

@ExtendWith(MockitoExtension.class)
public class TicketRaisedCategoriesByAtmServiceTest {

	@Mock
	private TicketRaisedCategoriesByAtmRepository ticketRaisedCategoriesByAtmRepository;

	@Mock
	private LoginService loginService;

	@InjectMocks
	private TicketRaisedCategoriesByAtmService ticketRaisedCategoriesByAtmService;

	private TicketRaisedCategoriesByAtmRequestDto requestDto;
	private TicketRaisedCategoriesByAtm mockEntity;
	private String mockUserId;
	private String mockAtmId;

	@BeforeEach
	void setUp() {
		// Initialize test data
		mockUserId = "nitin.waghmare";
		mockAtmId = "APCN12509";

		// Create request DTO
		requestDto = new TicketRaisedCategoriesByAtmRequestDto();
		requestDto.setAtmId(mockAtmId);

		// Create mock entity
		mockEntity = new TicketRaisedCategoriesByAtm();
		mockEntity.setId("1");
		mockEntity.setTotalOpenTickets("5");
		mockEntity.setTodaysTicketsColor("#FF5733");
	}

	@Test
	@DisplayName("Test getTicketsCategories - Successful scenario with data")
	void testGetTicketsCategoriesSuccess() {
		// Arrange
		when(loginService.getLoggedInUser()).thenReturn(mockUserId);
		when(ticketRaisedCategoriesByAtmRepository.getTicketsByUserAndAtm(mockUserId, mockAtmId))
				.thenReturn(mockEntity);

		// Act
		TicketRaisedResponseByAtm response = ticketRaisedCategoriesByAtmService.getTicketsCategories(requestDto);

		// Assert
		assertNotNull(response);
		assertNotNull(response.getData());
		assertEquals(8, response.getData().size(), "Should return 8 categories (1 dynamic + 7 static)");

		// Verify the first category (down) has correct data from repository
		TicketRaisedCategoriesByAtmResponseDto downCategory = response.getData().get(0);
		assertEquals("down", downCategory.getTitle());
		assertEquals("5", downCategory.getValue());
		assertEquals("#FF5733", downCategory.getColor());

		// Verify the static categories are present
		List<String> expectedTitles = List.of("down", "SiteVisit", "Adhoc", "QC", "Incident", "Recon", "ESS", "IOT");
		for (int i = 0; i < expectedTitles.size(); i++) {
			assertEquals(expectedTitles.get(i), response.getData().get(i).getTitle());
		}
	}

	@Test
	@DisplayName("Test getTicketsCategories - Verify static category values")
	void testGetTicketsCategoriesStaticValues() {
		// Arrange
		when(loginService.getLoggedInUser()).thenReturn(mockUserId);
		when(ticketRaisedCategoriesByAtmRepository.getTicketsByUserAndAtm(mockUserId, mockAtmId))
				.thenReturn(mockEntity);

		// Act
		TicketRaisedResponseByAtm response = ticketRaisedCategoriesByAtmService.getTicketsCategories(requestDto);

		// Assert
		assertNotNull(response);
		List<TicketRaisedCategoriesByAtmResponseDto> categories = response.getData();

		// Verify SiteVisit category
		TicketRaisedCategoriesByAtmResponseDto siteVisit = categories.get(1);
		assertEquals("SiteVisit", siteVisit.getTitle());
		assertEquals("0", siteVisit.getValue());
		assertEquals("#00BCD4", siteVisit.getColor());

		// Verify Adhoc category
		TicketRaisedCategoriesByAtmResponseDto adhoc = categories.get(2);
		assertEquals("Adhoc", adhoc.getTitle());
		assertEquals("0", adhoc.getValue());
		assertEquals("#8BC34A", adhoc.getColor());

		// Verify IOT category (last one)
		TicketRaisedCategoriesByAtmResponseDto iot = categories.get(7);
		assertEquals("IOT", iot.getTitle());
		assertEquals("0", iot.getValue());
		assertEquals("#737373", iot.getColor());
	}

	@Test
	@DisplayName("Test getTicketsCategories - Different ATM ID")
	void testGetTicketsCategoriesWithDifferentAtmId() {
		// Arrange
		String differentAtmId = "DIFFERENT_ATM_123";
		TicketRaisedCategoriesByAtmRequestDto differentRequestDto = new TicketRaisedCategoriesByAtmRequestDto();
		differentRequestDto.setAtmId(differentAtmId);

		// Create a different entity for this ATM
		TicketRaisedCategoriesByAtm differentEntity = new TicketRaisedCategoriesByAtm();
		differentEntity.setId("2");
		differentEntity.setTotalOpenTickets("10");
		differentEntity.setTodaysTicketsColor("#00FF00");

		when(loginService.getLoggedInUser()).thenReturn(mockUserId);
		when(ticketRaisedCategoriesByAtmRepository.getTicketsByUserAndAtm(mockUserId, differentAtmId))
				.thenReturn(differentEntity);

		// Act
		TicketRaisedResponseByAtm response = ticketRaisedCategoriesByAtmService
				.getTicketsCategories(differentRequestDto);

		// Assert
		assertNotNull(response);
		assertNotNull(response.getData());
		assertEquals(8, response.getData().size());

		// Verify the first category (down) has correct data from repository for this
		// ATM
		TicketRaisedCategoriesByAtmResponseDto downCategory = response.getData().get(0);
		assertEquals("down", downCategory.getTitle());
		assertEquals("10", downCategory.getValue());
		assertEquals("#00FF00", downCategory.getColor());
	}

	@Test
	@DisplayName("Test getTicketsCategories - Different User ID")
	void testGetTicketsCategoriesWithDifferentUserId() {
		// Arrange
		String differentUserId = "different_user";

		when(loginService.getLoggedInUser()).thenReturn(differentUserId);
		// Simulate different data for a different user
		TicketRaisedCategoriesByAtm userSpecificEntity = new TicketRaisedCategoriesByAtm();
		userSpecificEntity.setId("3");
		userSpecificEntity.setTotalOpenTickets("15");
		userSpecificEntity.setTodaysTicketsColor("#0000FF");

		when(ticketRaisedCategoriesByAtmRepository.getTicketsByUserAndAtm(differentUserId, mockAtmId))
				.thenReturn(userSpecificEntity);

		// Act
		TicketRaisedResponseByAtm response = ticketRaisedCategoriesByAtmService.getTicketsCategories(requestDto);

		// Assert
		assertNotNull(response);
		assertNotNull(response.getData());

		// Verify the first category has user-specific data
		TicketRaisedCategoriesByAtmResponseDto downCategory = response.getData().get(0);
		assertEquals("15", downCategory.getValue());
		assertEquals("#0000FF", downCategory.getColor());
	}
}