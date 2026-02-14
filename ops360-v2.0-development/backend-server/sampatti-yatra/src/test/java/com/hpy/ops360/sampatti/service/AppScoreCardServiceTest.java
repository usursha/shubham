package com.hpy.ops360.sampatti.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.FinancialYearGroupDto;
import com.hpy.ops360.sampatti.entity.AppScoreCardEntity;
import com.hpy.ops360.sampatti.repository.AppScoreCardRepo;

@ExtendWith(MockitoExtension.class)
public class AppScoreCardServiceTest {

	@InjectMocks
	private AppScoreCardService service; 

	@Mock
	private AppScoreCardRepo repository; 

	@Mock
	private LoginUtil util;
	
	private String mockUsername;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockUsername = "rishabh.jain";
	    when(util.getLoggedInUserName()).thenReturn(mockUsername);
	}

	@Test
	void testGetCEScoreCardData_PositiveScenario() {

		when(util.getLoggedInUserName()).thenReturn(mockUsername);

		List<AppScoreCardEntity> mockData = Arrays.asList(new AppScoreCardEntity(2L,"October", 2023, 100, 96, 16, 0.0, null, null), // 2022-2023
				new AppScoreCardEntity(5L,"March", 2024, 104, 99, 12, 0.0, null, null), // 2023-2024
				new AppScoreCardEntity(3L,"February", 2025, 106, 103, 15, 0.0, 0.0, 0.0) // 2024-2025
		);
		when(repository.getCEScoreCardData(mockUsername)).thenReturn(mockData);
		List<FinancialYearGroupDto> result = service.getCEScoreCardData();
		Assertions.assertEquals(3, result.size());
		FinancialYearGroupDto fy1 = result.get(0);
		Assertions.assertEquals("Financial Year: 2022-2023", fy1.getTitle());
		Assertions.assertEquals(1, fy1.getFinancialYearData().size());

		FinancialYearGroupDto fy2 = result.get(1);
		Assertions.assertEquals("Financial Year: 2023-2024", fy2.getTitle());
		Assertions.assertEquals(1, fy2.getFinancialYearData().size());
		
		FinancialYearGroupDto fy3 = result.get(2);
		Assertions.assertEquals("Financial Year: 2024-2025", fy3.getTitle());
		Assertions.assertEquals(1, fy3.getFinancialYearData().size());
	}

	@Test
	void testGetCEScoreCardData_EmptyList() {
		when(util.getLoggedInUserName()).thenReturn(mockUsername);
		when(repository.getCEScoreCardData(mockUsername)).thenReturn(Collections.emptyList());
		List<FinancialYearGroupDto> result = service.getCEScoreCardData();
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testGetCEScoreCardData_NullList() {
		when(util.getLoggedInUserName()).thenReturn(mockUsername);
		when(repository.getCEScoreCardData(mockUsername)).thenReturn(null);
		Assertions.assertThrows(NullPointerException.class, () -> service.getCEScoreCardData());
	}

	@Test
	void testGetCEScoreCardData_InvalidMonthName() {
		when(util.getLoggedInUserName()).thenReturn(mockUsername);
		List<AppScoreCardEntity> mockData = List.of(new AppScoreCardEntity(5L,"FakeMonth", 2024, 80, 90, 1, 1000.0, 2000.55, 300.22));
		when(repository.getCEScoreCardData(mockUsername)).thenReturn(mockData);
		List<FinancialYearGroupDto> result = service.getCEScoreCardData();
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("Financial Year: 2024-2025", result.get(0).getTitle());
	}

}